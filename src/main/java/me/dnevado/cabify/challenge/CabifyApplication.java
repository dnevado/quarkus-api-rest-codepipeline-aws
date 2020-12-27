package me.dnevado.cabify.challenge;

import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.quarkus.vertx.web.RoutingExchange;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.eventbus.EventBus;

import me.dnevado.cabify.challenge.domain.CabifyService;
import me.dnevado.cabify.challenge.domain.model.Car;
import me.dnevado.cabify.challenge.domain.model.ReturnMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;
/**
 * This class provides http resources for the basket.
 * It is built on top of vertx routes and I/O non blocking paradigm.
 *
 * LogLevel info due to monitoring.
 *
 * @author dnevado
 */

@ApplicationScoped
@RouteBase(produces = "application/json")
public class CabifyApplication {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    @Inject
    private EventBus bus;

    @Inject
    private CabifyApplication cabifyApplication;
    
    private String  _CARS_MESSAGGE = "create-available-cars";
    private String  _JOURNEY_MESSAGGE = "create-journey";
    private String  _STATUS_MESSAGGE = "status";
    private String  _DROPOFF_MESSAGGE = "dropoff";
    private String  _LOCATE_MESSAGGE = "locate";
    
    
    
    @Route(path = "/journey", methods = HttpMethod.POST)
    void journey(RoutingExchange ex) {
        log.info("POST /journey/");   
        String groupContent = ex.context().getBodyAsString();
        bus.<ReturnMessage>request(_JOURNEY_MESSAGGE,groupContent)
        .subscribeAsCompletionStage()
        .thenAccept(s -> ex.ok(JsonbBuilder.create().toJson(s.body())));
    }
    
    @Route(path = "/dropoff", methods = HttpMethod.POST)
    void dropoff(RoutingExchange ex) {
        log.info("POST /dropoff/"); 
        String groupId = ex.getParam("ID").get();
        bus.<ReturnMessage>request(_DROPOFF_MESSAGGE, groupId)
        .subscribeAsCompletionStage()
        .thenAccept(s -> ex.ok(JsonbBuilder.create().toJson(s.body())));
    }
    
    @Route(path = "/locate", methods = HttpMethod.POST)
    void locate(RoutingExchange ex) {
        log.info("post /locate/");
        String groupId = ex.getParam("ID").get();
        bus.<ReturnMessage>request(_LOCATE_MESSAGGE, groupId)
        .subscribeAsCompletionStage()
        .thenAccept(s -> {
            if (s.body() != null) {
                ex.ok(JsonbBuilder.create().toJson(s.body()));
            } else {
                ex.notFound().end("");
            }
        });
    }
    
    @Route(path = "/status", methods = HttpMethod.GET)
    void getStatus(RoutingExchange ex) {    	
        log.info("get /status/");       
         bus.<ReturnMessage>request(_STATUS_MESSAGGE, "")
         .subscribeAsCompletionStage()
        .thenAccept(s -> ex.ok(JsonbBuilder.create().toJson(s.body()))); 
    }
    
    @Route(path = "/cars", methods = HttpMethod.PUT)
    void createAvailableCars(RoutingExchange ex) {
        log.debug("PUT /cars/");
        String carsContent = ex.context().getBodyAsString();
        bus.<ReturnMessage>request(_CARS_MESSAGGE, carsContent)
        .subscribeAsCompletionStage()
        .thenAccept(s -> ex.ok(JsonbBuilder.create().toJson(s.body())));
    }
/* 
    //If basket is not present then throw 404.
    @Route(path = "/basket/:code", methods = HttpMethod.DELETE)
    void deleteBasket(RoutingExchange ex) {
        String code = ex.getParam("code").get();
        log.info("DELETE /basket/{}", code);
        bus.<CarsOld>request("delete-basket-event", code)
                .subscribeAsCompletionStage()
                .thenAccept(s -> {
                    if (s.body() != null) {
                        ex.ok("");
                    } else {
                        ex.notFound().end("");
                    }
                });
    }

    //If basket is not present then throw 404.
    @Route(path = "/basket/:code", methods = HttpMethod.GET)
    void getBasketByCode(RoutingExchange ex) {
        String code = ex.getParam("code").get();
        log.info("GET /basket/{}", code);
        bus.<CarsOld>request("get-basket-event", code)
                .subscribeAsCompletionStage()
                .thenAccept(s -> {
                    if (s.body() != null) {
                        ex.ok(JsonbBuilder.create().toJson(s.body()));
                    } else {
                        ex.notFound().end("");
                    }
                });
    }

    //If basket is not present then throw 404.
    @Route(path = "/basket/:code/item/:itemCode", methods = HttpMethod.PUT)
    void addItemToBasket(RoutingExchange ex) {
        String code = ex.getParam("code").get();
        String itemCode = ex.getParam("itemCode").get();
        log.info("GET /basket/{}/item/{}", code, itemCode);
        AddItemToBasketEvent event = new AddItemToBasketEvent(code,itemCode);
        bus.<CarsOld>request("add-item-basket-event", event)
                .subscribeAsCompletionStage()
                .thenAccept(s -> {
                    if (s.body() != null) {
                        ex.ok(JsonbBuilder.create().toJson(s.body()));
                    } else {
                        ex.notFound().end("");
                    }
                });
    }*/

}
