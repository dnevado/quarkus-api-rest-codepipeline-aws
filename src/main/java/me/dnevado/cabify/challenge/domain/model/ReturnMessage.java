package me.dnevado.cabify.challenge.domain.model;

public class ReturnMessage {

    private String statusCode;    
    //amount of seats 
    private String statusDescription;
    

    public ReturnMessage(String statusCode, String statusDescription) {
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;        
    }


	public String getStatusCode() {
		return statusCode;
	}


	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}


	public String getStatusDescription() {
		return statusDescription;
	}


	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

  

}
