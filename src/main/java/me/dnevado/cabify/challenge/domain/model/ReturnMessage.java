package me.dnevado.cabify.challenge.domain.model;

public class ReturnMessage {

    private final String statusCode;    
    //amount of seats 
    private final String statusDescription;
    

    public ReturnMessage(String statusCode, String statusDescription) {
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;        
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

}
