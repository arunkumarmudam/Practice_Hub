package com.simulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@WebServlet(urlPatterns = {
		 "/",
		    "/stb/v1",
		    "/stb/v1/allocate",
		    "/stb/v1/deallocate",
		    "/stb/v1/disassociate",
		    "/subscription/v1",
		    "/subscription/v1/disassociate",
		    "/subscription/v1/suspend",
		    "/subscription/v1/activate",
		    "/fingerprints/v1/settings",
		    "/subscriber/v1",
		    "/subscriber/v1/*"
})
public class TDMSigmaServer extends HttpServlet {

	private void showHomePage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html");

		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();

		response.getWriter().println(
				"<html><body style='font-family:Segoe UI;text-align:center;padding:30px'>"
				+ "<h1 style='color:#2196F3'>TDMSigma Simulator</h1>"
				+ "<p style='color:green;font-size:18px'><b>Simulator Running Successfully</b></p>" 
				+ "<hr width='60%'>"
				+ "<h3>Available APIs</h3>" 
				+ "<pre style='display:inline-block;text-align:left;font-size:14px'>"
				+ "POST   " + baseUrl + "/stb/v1\n"
				+ "POST   " + baseUrl + "/stb/v1/allocate\n"
				+ "POST   " + baseUrl + "/stb/v1/deallocate\n"
				+ "POST   " + baseUrl + "/stb/v1/disassociate\n"
				+ "POST   " + baseUrl + "/subscription/v1\n"
				+ "POST   " + baseUrl + "/subscription/v1/disassociate\n"
				+ "PUT    " + baseUrl + "/subscription/v1/suspend\n"
				+ "PUT    " + baseUrl + "/subscription/v1/activate\n"
				+ "POST   " + baseUrl + "/fingerprints/v1/settings\n"
				+ "GET    " + baseUrl + "/subscriber/v1/{subId}\n"
				+ "DELETE " + baseUrl + "/subscriber/v1/{subId}\n"
				+ "</pre>"
				+ "<hr width='60%'>" 
				+ "<p>Host: <b>" + request.getServerName() + "</b> | Port: <b>"
				+ request.getLocalPort() + "</b></p>" 
				+ "</body></html>");
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String uri = req.getRequestURI();

        if (uri.endsWith("/stb/v1")) {
            createStb(req, resp);
        }
        else if (uri.endsWith("/stb/v1/allocate")) {
            allocate(req, resp);
        }else if (uri.endsWith("/subscriber/v1")) {
            subscription(req, resp);
        }
        else if (uri.endsWith("/subscription/v1")) {
            subscription(req, resp);
        }
        else if (uri.endsWith("/stb/v1/deallocate")) {
            deallocate(req, resp);
        }
        else if (uri.endsWith("/stb/v1/disassociate")) {
            deallocate(req, resp);
        }
        else if (uri.endsWith("/subscription/v1/disassociate")) {
            deallocate(req, resp);
        }
        else if (uri.endsWith("/fingerprints/v1/settings")) {
        	createFingerprint(req, resp);
        }else {
            sendError(resp,"Unsupported Endpoint : " + uri);
        }
    }

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
        String uri = request.getRequestURI();
        // Homepage
        if (uri.equals(request.getContextPath() + "/")) {

            showHomePage(request, response);
            return;
        }
        // Subscriber API
        if (uri.contains("/subscriber/v1/")) {
            getSubscriber(request, response);
            return;
        }

        response.sendError(404, "Invalid Endpoint");
    }

	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        deleteSubscriber(req, resp);
    }
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String uri = req.getRequestURI();

        if (uri.endsWith("/subscription/v1/suspend") || uri.endsWith("/subscription/v1/activate")) {
        	
        	String response =
                    "{"
                  + "\"data\":[{"
                  + "\"deviceId\":\"123456\""
                  + "}],"
                  + "\"ec\":0"
                  + "}";
            send(resp, response);
        }else {
            sendError(resp,"Unsupported Endpoint : " + uri);
        }
    }

	private void createStb(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	try {
                JSONObject request =(JSONObject) new JSONParser().parse(readBody(req));

                String subId = (String) request.get("subId");
                String deviceId = (String) request.get("deviceId");
                String macAddr = (String) request.get("macAddr");
                String vendorId = (String) request.get("vendorId");
                String id = (String) request.get("id");

                String response =
                        "{"
                        + "\"data\":{"
                        + "\"subId\":\"" + subId + "\","
                        + "\"stbVendor\":\"" + vendorId + "\","
                        + "\"fpStatus\":null,"
                        + "\"id\":\"" + id + "\","
                        + "\"modelNo\":\"\","
                        + "\"deviceId\":\"" + deviceId + "\","
                        + "\"status\":\"in_stock\","
                        + "\"macAddr\":\"" + macAddr + "\""
                        + "},"
                        + "\"ec\":0"
                        + "}";

                send(resp, response);

            } catch (Exception e) {
                sendError(resp, e.getMessage());
            }
            }

		private void allocate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        
			try {
			JSONObject request = (JSONObject) new JSONParser().parse(readBody(req));
			JSONArray arr = (JSONArray) request.get("deviceIds");
			String deviceId = (String) arr.get(0);

			String response =
				    "{\"data\":[{\"deviceId\":\""
				    + deviceId +
				    "\"}],\"ec\":0}";

            send(resp, response);

			} catch (Exception e) {
            sendError(resp, e.getMessage());
        }
    }

	private void subscription(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        send(resp,"{\"data\":[],\"ec\":0}");
    }

	private void deallocate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {

            JSONObject request =(JSONObject) new JSONParser().parse(readBody(req));

            String deviceId = (String) request.get("deviceId");

            String response =
				    "{\"data\":[{\"deviceId\":\""
				    + deviceId +
				    "\"}],\"ec\":0}";

            send(resp, response);

        } catch (Exception e) {
            sendError(resp, e.getMessage());
        }
    }

	private void getSubscriber(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String subId = req.getPathInfo().replace("/", "");
        String response =
                "{"
                        + "\"data\":{"
                        + "\"firstName\":\"WRWERW\","
                        + "\"lastName\":\"WERWER\","
                        + "\"addedAt\":\"0001-01-01T00:00:00Z\","
                        + "\"address\":\"FDDD\","
                        + "\"phone\":\"98675433\","
                        + "\"fpStatus\":null,"
                        + "\"id\":\""
                        + subId
                        + "\","
                        + "\"email\":\"sharabha@gnaud\","
                        + "\"updatedAt\":\"0001-01-01T00:00:00Z\""
                        + "},"
                        + "\"ec\":0}";

        send(resp, response);
    }

	private void deleteSubscriber(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String response ="{\"ec\":0}";

        send(resp, response);
    }

	private String readBody(HttpServletRequest req) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = req.getReader();
        String line;

        while ((line = br.readLine())
                != null) {

            sb.append(line);
        }

        return sb.toString();
    }

    private void send(HttpServletResponse resp,String response)throws IOException {

        resp.setContentType("application/json");
        resp.setStatus(200);
        resp.getWriter().write(response);
    }
    
	private void createFingerprint(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		try {

			JSONObject request = (JSONObject) new JSONParser().parse(readBody(req));

			String name = (String) request.get("name");

			Long px = (Long) request.get("px");
			Long py = (Long) request.get("py");
			Long fontSize = (Long) request.get("fontSize");

			String textColor = (String) request.get("textColor");

			String bgColor = (String) request.get("bgColor");

			Boolean displayBackground = (Boolean) request.get("displayBackground");
			String id = UUID.randomUUID().toString();

			String response = "{" 
			        + "\"ec\":0,"
                    + "\"data\":{"
                    + "\"id\":\"" + id + "\","
                    + "\"name\":\"" + name + "\","
                    + "\"px\":" + px + ","
                    + "\"py\":" + py + ","
                    + "\"fontSize\":" + fontSize + ","
                    + "\"textColor\":\"" + textColor + "\","
                    + "\"bgColor\":\"" + bgColor + "\","
                    + "\"displayBackground\":"
                    + displayBackground + ","
                    + "\"createdAt\":\"0001-01-01T00:00:00Z\","
                    + "\"updatedAt\":\"0001-01-01T00:00:00Z\""
                    + "}"
                    + "}";

			send(resp, response);

		} catch (Exception e) {
		    sendError(resp, e.getMessage());
		}
	}

	private void sendError(HttpServletResponse resp, String message) throws IOException {

		send(resp, "{\"ec\":1,\"msg\":\"" + message.replace("\"", "") + "\"}");
	}

	private String getRequired(JSONObject json, String field) throws Exception {

		String value = (String) json.get(field);

		if (value == null || value.trim().length() == 0) {
			throw new Exception(field + " is mandatory");
		}

		return value;
	}
    
}