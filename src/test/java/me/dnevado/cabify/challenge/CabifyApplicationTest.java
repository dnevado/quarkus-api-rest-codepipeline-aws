package me.dnevado.cabify.challenge;

import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.JsonObject;
import me.dnevado.cabify.challenge.domain.model.Car;
import io.restassured.http.ContentType;
import me.dnevado.cabify.challenge.domain.model.ReturnMessage;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;


@QuarkusTest
@TestMethodOrder(OrderAnnotation.class)

public class CabifyApplicationTest {

    
    @Test
    public void test200OKEndpoint() {
        given()
                .when().put("/cars")
                .then()
                .statusCode(200);
    }
    @Test
    @Order(1)
    public void testCreateCarsEndpoint() {    	
        	      	
    	String carsPayLoad = "[{\n" +
    		        "  \"id\": 1,\n" +    		      
    		        "  \"seats\": 4\n" +
    		        "}]";
    	
    	
        given()
        		.accept(ContentType.JSON)
        		.contentType(ContentType.JSON).body(carsPayLoad)        		
                .when().put("/cars")
                .then()
                .statusCode(200).body(containsString("OK"));
    }
    @Test
    public void testFailCreateCarsEndpoint() {    	
    
    	String carsPayLoad = "[{\n" +
		        "  \"id\": 1,\n" +    		      
		        "  \"seat\": 4\n" +
		        "}]";
	
	
    	given()
    		.accept(ContentType.JSON)
    		.contentType(ContentType.JSON).body(carsPayLoad)        		
            .when().put("/cars")
            .then()
            .statusCode(200).body(containsString("Bad Request"));
    
    }
    
    @Test    
    @Order(2)
    public void testCreateJourneyEndpoint() {    	
    	
    	String peoplePayLoad = "{\n" +
		        "  \"id\": 1,\n" +    		      
		        "  \"people\": 4\n" +
		        "}";
	
	
    	given()
    		.accept(ContentType.JSON)
    		.contentType(ContentType.JSON).body(peoplePayLoad)        		
            .when().post("/journey")
            .then()
            .statusCode(200).body(containsString("OK"));
    	
    	
    	
    }
    @Test
    public void testFailCreateJourneyEndpoint() {    	
    
    	String peoplePayLoad = "{\n" +
		        "  \"id\": 1,\n" +    		      
		        "  \"peopl\": 4\n" +
		        "}";
	
	
    	given()
    		.accept(ContentType.JSON)
    		.contentType(ContentType.JSON).body(peoplePayLoad)        		
            .when().post("/journey")
            .then()
            .statusCode(200).body(containsString("Bad Request"));
    }
    
    @Test
    public void testServiceStatus() {
    	
        given()
                .when().get("/status")
                .then()
                .statusCode(200);
        

    }
    
    @Test
    public void test404OKDropOff() {
    	
        given()
        		.param("ID", 99999999)
                .when().post("/dropoff")
                .then()
                	.statusCode(200).body(containsString("Not Found"));                	
        

    }
    @Test
    public void testBadRequestDropOff() {
    	
        given()
        		.param("ID", -1)
                .when().post("/dropoff")
                .then()
                	.statusCode(200).body(containsString("Bad Request"));                	
        

    }
    
    @Test
    @Order(4)
    public void test200OKDropOff() {
    	
    	    
        given()
        		.param("ID", "1")
                .when().post("/dropoff")
                .then()
                .statusCode(200);        		
       
    } 
    
    @Test
    public void test404OKLocate() {
    	
        given()
        		.param("ID", "-1")
                .when().post("/locate")
                .then()                	
                	.statusCode(200).body(containsString("Bad Request"));
        

    }
    
    @Test
    @Order(3)
    public void test200OKLocate() {
    	
    	  given()
    	  	  .param("ID", "1")
	          .when().post("/locate")
	          .then()	          	
	          	.statusCode(200).body(containsString("1"));	          		
       
    } 
    
    
}