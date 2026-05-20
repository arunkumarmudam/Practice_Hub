package com.incognito.soap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

/**
 * @author arunkumar.mudam 5.18.0.0
 *
 */

@WebServlet("/soap")
public class SoapSimulatorServlet extends HttpServlet {

    private long tokenExpiryMillis = 5 * 60 * 1000; // default: 5 minutes
    private long firstConnectionTime = 0;

    private Map<String, Long> recentTokens = new LinkedHashMap<>();
    private static final int MAX_TOKENS = 5;

    private Set<String> validMacAddresses = new HashSet<>();

    @Override
    public void init() throws ServletException {
        super.init();
        try (InputStream input = getServletContext().getResourceAsStream("/WEB-INF/simulator.properties")) {
            Properties props = new Properties();
            if (input != null) {
                props.load(input);

                String macList = props.getProperty("valid.mac.addresses");
                if (macList != null && !macList.trim().isEmpty()) {
                    validMacAddresses.addAll(Arrays.asList(macList.trim().split("\\s*,\\s*")));
                }

                String expiry = props.getProperty("token.expiry.millis");
                if (expiry != null && !expiry.trim().isEmpty()) {
                    tokenExpiryMillis = Long.parseLong(expiry.trim());
                }
            }
        } catch (IOException e) {
            throw new ServletException("Unable to load config.properties", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            response.setContentType("text/html");
            pw.println("<html><head>");
            pw.println("<link href='https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap' rel='stylesheet'>");
            pw.println("</head><body style='background-color: #f4f4f9;'>");
            pw.println("<center>");
            pw.println("<p style='font-size: 24px; color: #3498db; font-family: Poppins, Arial, sans-serif; font-weight: bold; margin-top: 50px;'>");
            pw.println("Incognito Simulator</p>");
            pw.println("<hr style='width: 50%; border: 2px solid #3498db;'>");
            pw.println("<p style='font-size: 18px; color: #2ecc71; font-family: Poppins, Arial, sans-serif; font-weight: normal;'>");
            pw.println("Running on port: <span style='color: #e74c3c;'>" + request.getLocalPort() + "</span></p>");
            pw.println("</center>");
            pw.println("</body></html>");

        } catch (Exception e) {
            if (pw != null) pw.println("<errores>INTERNAL ERROR</errores>");
        } finally {
            if (pw != null) pw.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String xmlRequest = readBody(request);
        String soapResponse;
        //for login request generates new token and stores in recenttokens list until expiry
        if (xmlRequest.contains("<inc:login>")) {

            if (firstConnectionTime == 0)
                firstConnectionTime = System.currentTimeMillis() / 1000;

            if ((System.currentTimeMillis() / 1000) - firstConnectionTime > tokenExpiryMillis) {
                firstConnectionTime = System.currentTimeMillis() / 1000;
                recentTokens.clear();
            }

            String newToken = UUID.randomUUID().toString().replace("-", "");
            recentTokens.put(newToken, System.currentTimeMillis());

            if (recentTokens.size() > MAX_TOKENS) {
                Iterator<String> it = recentTokens.keySet().iterator();
                if (it.hasNext()) {
                    it.next();
                    it.remove();
                }
            }

            soapResponse = SoapSimulatorResponses.loginResponse(newToken);

        } else if (xmlRequest.contains("<mps:createRecords>")) {
            String token = extractTagValue(xmlRequest, "inc:token");
            String entityId = extractTagValue(xmlRequest, "inc:entityId");
            String name = extractTagValue(xmlRequest, "inc:name");
            String mac = extractTagValue(xmlRequest, "mps:macAddress");

            if (isTokenValid(token)) {
            	if (validMacAddresses.contains(mac)) {
                String id = UUID.randomUUID().toString().replace("-", "").substring(0, 32).toUpperCase();
                soapResponse = SoapSimulatorResponses.createResponse(id, entityId, name, mac);
            	}else {
            		soapResponse = SoapSimulatorResponses.permissionDeniedInvalidMAC();
            	}
            } else {
                soapResponse = SoapSimulatorResponses.permissionDenied();
            }

        } else if (xmlRequest.contains("<mps:searchRecords>")) {
            String token = extractTagValue(xmlRequest, "inc:token");
            String criteria = extractTagValue(xmlRequest, "mps:criteria");

            if (isTokenValid(token)) {
                if (criteria != null && criteria.startsWith("MACADDRESS=")) {
                    String mac = criteria.split("=")[1];
                    if (validMacAddresses.contains(mac)) {
                        soapResponse = SoapSimulatorResponses.searchRecordsResponse(false, 0);
                    } else {
                        soapResponse = SoapSimulatorResponses.permissionDeniedInvalidMAC();
                    }
                } else {
                    soapResponse = SoapSimulatorResponses.permissionDeniedInvalidMAC();
                }
            } else {
                soapResponse = SoapSimulatorResponses.permissionDenied();
            }

        } else if (xmlRequest.contains("<inc:createRecords>")) {
            String token = extractTagValue(xmlRequest, "inc1:token");
            String accountId = extractTagValue(xmlRequest, "inc:accountId");
            String name = extractTagValue(xmlRequest, "inc1:name");
            String notes = extractTagValue(xmlRequest, "inc:notes");
//            String macAddress=extractTagValue(xmlRequest, "mps:macAddress");

            if (isTokenValid(token)) {
//            	if (validMacAddresses.contains(macAddress)) {
            		String id = UUID.randomUUID().toString().replace("-", "").substring(0, 32).toUpperCase();
                    String entityId = UUID.randomUUID().toString().replace("-", "").substring(0, 32).toUpperCase();
                    soapResponse = SoapSimulatorResponses.mpcreateResponse(name, id, entityId, notes, accountId);
//            	}else {
//            		soapResponse = SoapSimulatorResponses.permissionDeniedInvalidMAC();
//            	}
                
            } else {
                soapResponse = SoapSimulatorResponses.permissionDenied();
            }

        } else if (xmlRequest.contains("<mps:updateRecords>")) {
            soapResponse = SoapSimulatorResponses.mpUpdateRecordResponse(xmlRequest);

        } else if (xmlRequest.contains("<inc:deleteRecords>")) {
            soapResponse = SoapSimulatorResponses.incDeleteRecordResponse();

        } else if (xmlRequest.contains("<mps:deleteRecords>")) {
            soapResponse = SoapSimulatorResponses.mpDeleteRecordResponse();

        } else if (xmlRequest.contains("<inc:searchRecords>")) {
        	soapResponse = SoapSimulatorResponses.incSearchRecordsResponse();
        }else {
            soapResponse = SoapSimulatorResponses.permissionDenied();
        }

        response.setContentType("text/xml;charset=UTF-8");
        response.getWriter().write(soapResponse);
    }

    private boolean isTokenValid(String token) {
        Long createdTime = recentTokens.get(token);
        if (createdTime == null) return false;

        long currentTime = System.currentTimeMillis();
        if (currentTime - createdTime > tokenExpiryMillis) {
            recentTokens.remove(token); // Expired token
            return false;
        }
        return true;
    }

    private String readBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null)
            sb.append(line);
        return sb.toString();
    }

    private String extractTagValue(String xml, String tag) {
        int start = xml.indexOf("<" + tag + ">");
        int end = xml.indexOf("</" + tag + ">");
        if (start == -1 || end == -1)
            return "";
        return xml.substring(start + tag.length() + 2, end).trim();
    }
}
