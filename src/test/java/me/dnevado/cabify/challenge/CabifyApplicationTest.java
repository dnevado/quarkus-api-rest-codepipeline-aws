package me.dnevado.cabify.challenge;

import io.quarkus.test.junit.QuarkusTest;
import me.dnevado.cabify.challenge.domain.model.Car;
import me.dnevado.cabify.challenge.domain.model.ReturnMessage;

import org.junit.jupiter.api.Test;

import javax.json.bind.JsonbBuilder;

import static io.restassured.RestAssured.given;


@QuarkusTest
public class CabifyApplicationTest {

    @Test
    public void testCreateCarsEndpoint() {
        given()
                .when().put("/cars")
                .then()
                .statusCode(200);
    }

    @Test
    public void testServiceStatus() {
    	
        given()
                .when().get("/status")
                .then()
                .statusCode(200);
        

    }
    
    @Test
    public void testDropOff() {
    	
        given()
                .when().post("/dropoff")
                .then()
                .statusCode(200);
        

    }
 
 
 /*
    @Test //only test if does not exist
    public void testDeleteBasketNotFoundEndpoint() {
        String delete404 = "not_exist_basket";
        given()
                .when().delete("/basket/" + delete404)
                .then()
                .statusCode(404);
    }

    @Test
    public void testGetBasketByCodeEndpoint() {
        //create one
        CarsOld basket = createBasket();
        //getById
        given()
                .when().get("/basket/" + basket.getCode())
                .then()
                .statusCode(200);
    }

    @Test
    public void testGetBasketByCodeNotFoundEndpoint() {
        //create one
        String code = "not_exist_basket";
        //getById
        given()
                .when().get("/basket/" + code)
                .then()
                .statusCode(404);
    }

    @Test
    public void testAddItemToBasketEndpoint() {
        //create one
        CarsOld basket = createBasket();
        //getById
        given()
                .when().put("/basket/" + basket.getCode() + "/item/PEN")
                .then()
                .statusCode(200);
    }

    @Test
    public void testAddItemToBasketNotFoundEndpoint() {
        //getById
        given()
                .when().put("/basket/not-found-basket/item/PEN")
                .then()
                .statusCode(404);
    }

    @Test
    public void testAddItemNotExistToBasketEndpoint() {
        //create one
        CarsOld basket = createBasket();
        String item404 = "not_exist_item";
        //getById
        given()
                .when().put("/basket/" + basket.getCode() + "/item/" + item404)
                .then()
                .statusCode(404);
    }

    @Test
    public void testAddItemNotExistToBasketNotFoundEndpoint() {
        //create one
        CarsOld basket = createBasket();
        String item404 = "not_exist_item";
        //getById
        given()
                .when().put("/basket/" + item404 + "/item/" + item404)
                .then()
                .statusCode(404);
    }
*/

    private ReturnMessage createCars() {
        return JsonbBuilder.create().fromJson(
                given()
                        .when().post("/cars")
                        .then()
                        .extract().response().asString(), ReturnMessage.class);
    }

}