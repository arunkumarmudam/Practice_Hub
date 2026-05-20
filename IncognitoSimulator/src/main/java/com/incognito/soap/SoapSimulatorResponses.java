package com.incognito.soap;

//comment removed
public class SoapSimulatorResponses {

    public static String loginResponse(String token) {
    	return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
    		       "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
    		       "<soapenv:Body>" +
    		       "<n:loginResponse xmlns:n=\"http://bcc.incognito.com/IncEntitySecurity\" xmlns:n0=\"http://www.incognito.com/IncTypes\">" +
    		       "<n:authorizationToken>" +
    		       "<n0:token>" + token + "</n0:token>" +
    		       "</n:authorizationToken>" +
    		       "</n:loginResponse>" +
    		       "</soapenv:Body>" +
    		       "</soapenv:Envelope>";
    }

    public static String createResponse(String id, String entityId, String name, String mac) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
               "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
               "<soapenv:Body>\n" +
               "<n:createRecordsResponse xmlns:n=\"http://bcc.incognito.com/MPService\" xmlns:n0=\"http://www.incognito.com/IncTypes\">" +
               "<n:cableModems>" +
               "<n:values>" +
               "<n0:name>" + name + "</n0:name>" +
               "<n0:description>Internet Subscriber</n0:description>" +
               "<n0:creationTime>2025-04-16T09:39:12.000Z</n0:creationTime>" +
               "<n0:lastModified>2025-04-16T09:39:12.000Z</n0:lastModified>" +
               "<n0:lastModifiedBy>magnatest</n0:lastModifiedBy>" +
               "<n0:id>" + id + "</n0:id>" +
               "<n0:entityId>" + entityId + "</n0:entityId>" +
               "<n:macAddress>" + mac + "</n:macAddress>" +
               "<n:fqdn/>" +
               "<n:maxCPEs>0</n:maxCPEs>" +
               "<n:clientClasses>30M</n:clientClasses>" +
               "<n:clientClasses>MAX-CPEs-1</n:clientClasses>" +
               "<n:staticAddresses/>" +
               "</n:values>" +
               "</n:cableModems>" +
               "</n:createRecordsResponse>" +
               "</soapenv:Body>" +
               "</soapenv:Envelope>";
    }

    public static String permissionDenied() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
               "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
               "<soapenv:Body>" +
               "<n:searchRecordsResponse xmlns:n=\"http://bcc.incognito.com/MPService\" xmlns:n0=\"http://www.incognito.com/IncTypes\">" +
               "<n:errorCode>" +
               "<n0:hasError>true</n0:hasError>" +
               "<n0:status>STATUS_ERROR</n0:status>" +
               "<n0:problemCode>13</n0:problemCode>" +
               "<n0:problemMessage>Permission denied</n0:problemMessage>" +
               "</n:errorCode>" +
               "<n:nextOffset>0</n:nextOffset>" +
               "<n:totalRecordCount>0</n:totalRecordCount>" +
               "</n:searchRecordsResponse>" +
               "</soapenv:Body>" +
               "</soapenv:Envelope>";
    }
    
    public static String permissionDeniedInvalidMAC() {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soapenv:Body>" +
                "<n:createRecordsResponse xmlns:n=\"http://bcc.incognito.com/MPService\" xmlns:n0=\"http://www.incognito.com/IncTypes\">" +
                "<n:errors>" +
                "<n0:values>" +
                "<n0:errorCode>" +
                "<n0:hasError>true</n0:hasError>" +
                "<n0:status>STATUS_ERROR</n0:status>" +
                "<n0:problemCode>22</n0:problemCode>" +
                "<n0:problemMessage>Invalid argument</n0:problemMessage>" +
                "</n0:errorCode>" +
                "<n0:id>" +
                "<n0:id></n0:id>" +
                "<n0:dbTable>DOCSIS_CABLEMODEM</n0:dbTable>" +
                "</n0:id>" +
                "</n0:values>" +
                "</n:errors>" +
                "</n:createRecordsResponse>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";
    }
    
    public static String searchRecordsResponse(boolean success, int totalRecords) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
               "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:n=\"http://bcc.incognito.com/MPService\" xmlns:n0=\"http://www.incognito.com/IncTypes\">" +
               "<soapenv:Body>" +
               "<n:searchRecordsResponse>" +
               "<n:errorCode>" +
               "<n0:hasError>" + ("false") + "</n0:hasError>" +
               "<n0:status>" + ("STATUS_SUCCESS") + "</n0:status>" +
               "</n:errorCode>" +
               "<n:nextOffset>0</n:nextOffset>" +
               "<n:totalRecordCount>" + totalRecords + "</n:totalRecordCount>" +
               "</n:searchRecordsResponse>" +
               "</soapenv:Body>" +
               "</soapenv:Envelope>";
    }
    
    public static String incSearchRecordsResponse() {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soapenv:Body>" +
                "<n:searchRecordsResponse xmlns:n=\"http://bcc.incognito.com/MPService\" xmlns:n0=\"http://www.incognito.com/IncTypes\">" +
                "<n:errorCode>" +
                "<n0:hasError>false</n0:hasError>" +
                "<n0:status>STATUS_SUCCESS</n0:status>" +
                "</n:errorCode>" +
                "<n:cableModems>" +
                "<n:values>" +
                "<n0:name>ARUN</n0:name>" +
                "<n0:description>Internet Subscriber</n0:description>" +
                "<n0:creationTime>2025-05-08T07:10:58.000Z</n0:creationTime>" +
                "<n0:lastModified>2025-05-08T07:10:58.000Z</n0:lastModified>" +
                "<n0:lastModifiedBy>QCTester</n0:lastModifiedBy>" +
                "<n0:id>AAAOKEYAAAOKAYAAAOKAYAAAOKAYAAA</n0:id>" +
                "<n0:entityId>AAAOKEYAAAOKAYAAAOKAYAAAOKAYAAA</n0:entityId>" +
                "<n:macAddress>001DCE39C147</n:macAddress>" +
                "<n:fqdn></n:fqdn>" +
                "<n:maxCPEs>0</n:maxCPEs>" +
                "<n:clientClasses>30M</n:clientClasses>" +
                "<n:clientClasses>MAX-CPEs-1</n:clientClasses>" +
                "<n:staticAddresses></n:staticAddresses>" +
                "</n:values>" +
                "</n:cableModems>" +
                "<n:nextOffset>0</n:nextOffset>" +
                "<n:totalRecordCount>27144</n:totalRecordCount>" +
                "</n:searchRecordsResponse>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";
    }
    
    public static String mpcreateResponse(String name, String id, String entityId, String notes, String accountId) {
        String timestamp = java.time.ZonedDateTime.now().toString();

        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                + "<soapenv:Body>"
                + "<n:createRecordsResponse xmlns:n=\"http://bcc.incognito.com/IncEntitySecurity\" xmlns:n0=\"http://www.incognito.com/IncTypes\">"
                + "<n:entities>"
                + "<n:values>"
                + "<n0:name>" + name + "</n0:name>"
                + "<n0:description>Internet Subscriber</n0:description>"
                + "<n0:creationTime>" + timestamp + "</n0:creationTime>"
                + "<n0:lastModified>" + timestamp + "</n0:lastModified>"
                + "<n0:lastModifiedBy>magnatest</n0:lastModifiedBy>"
                + "<n0:id>" + id + "</n0:id>"
                + "<n0:entityId>" + entityId + "</n0:entityId>"
                + "<n:accountId>" + accountId + "</n:accountId>"
                + "<n:address>"
                + "<n0:city/>"
                + "<n0:state_province/>"
                + "<n0:zipcode_postalcode/>"
                + "<n0:country/>"
                + "</n:address>"
                + "<n:entityTypeId>3B8634E61BE011DFA78D001A64A3084E</n:entityTypeId>"
                + "<n:notes>" + notes + "</n:notes>"
                + "</n:values>"
                + "</n:entities>"
                + "</n:createRecordsResponse>"
                + "</soapenv:Body>"
                + "</soapenv:Envelope>";
    }
    
    public static String mpUpdateRecordResponse(String request) {
    	return request;
    }
    public static String incDeleteRecordResponse() {
    	return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
    			+ "<soapenv:Body>"
    			+ "<n:deleteRecordsResponse xmlns:n=\"http://bcc.incognito.com/IncEntitySecurity\"/>"
    			+ "</soapenv:Body>"
    			+ "</soapenv:Envelope>";
    }
    public static String mpDeleteRecordResponse() {
    	return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
    			+ "<soapenv:Body>"
    			+ "<n:deleteRecordsResponse xmlns:n=\"http://bcc.incognito.com/MPService\"/>"
    			+ "</soapenv:Body>"
    			+ "</soapenv:Envelope>";
    }

}


