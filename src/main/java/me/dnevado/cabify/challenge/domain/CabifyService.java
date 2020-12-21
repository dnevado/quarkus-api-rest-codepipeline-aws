package me.dnevado.cabify.challenge.domain;

import io.quarkus.vertx.ConsumeEvent;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import me.dnevado.cabify.challenge.domain.model.Car;
import me.dnevado.cabify.challenge.domain.model.ReturnMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Calendar;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * This class returns <p>CompletionStage<Basket></p> instead of Optional due to
 * <p>io.quarkus.vertx.ConsumeEvent</p> and Message does not know
 * anything about Optional. For that <br>null</br> is used as empty
 *
 * For that methods return <p>CompletionStage<Basket></p> instead of Optional.
 *
 * @author javigs82
 */
@ApplicationScoped
public class CabifyService {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    @Inject
    private CabifyRepository cabifyRepository;
    

    @ConsumeEvent(value = "create-available-cars")
    public CompletionStage<ReturnMessage> createAvailableCars(String event) {
        log.debug("createAvailableCars");
        JsonArray cars = new JsonArray();        
        JsonObject car1 = new JsonObject();
        car1.put("id", "5");
        car1.put("seats", 93);
        cars.add(car1);
        JsonObject car2 = new JsonObject();
        car2.put("id", "6");
        car2.put("seats", 69);
        cars.add(car2);
        return CompletableFuture.supplyAsync(() ->
                this.cabifyRepository.createAvailableCars(cars).get()
        );
    }
    
    @ConsumeEvent(value = "status")    
    public CompletionStage<ReturnMessage> serviceStatus(String event) {
        log.debug("serviceStatus");
        return CompletableFuture.supplyAsync(() ->
             this.cabifyRepository.serviceStatus().orElse(null)
        );
    }
    
    @ConsumeEvent(value = "create-journey")
    public CompletionStage<ReturnMessage> assignJourney(String event) {
        log.debug("createAvailableCars");
        JsonObject journey = new JsonObject();
        journey.put("id", "5");
        journey.put("people", 93);         
        return CompletableFuture.supplyAsync(() ->
                this.cabifyRepository.assignJourney(journey).get()
        );
    }
    
    @ConsumeEvent(value = "dropoff")    
    public CompletionStage<ReturnMessage> dropOff(String event) {
        log.debug("dropoff");
        return CompletableFuture.supplyAsync(() ->
             this.cabifyRepository.dropOff().orElse(null)
        );
    }
    
    @ConsumeEvent(value = "locate")
    public CompletionStage<ReturnMessage> locate(String journeyId) {
        log.debug("getting locate by code {}", journeyId);
        return CompletableFuture.supplyAsync(() ->
                this.cabifyRepository.locateJourney(journeyId).orElse(null)
        );
    }
    
    /* 
    private String  _CARS_MESSAGGE = "create-available-cars";
    private String  _JOURNEY_MESSAGGE = "create-journey";
    private String  _STATUS_MESSAGGE = "status";
    private String  _DROPOFF_MESSAGGE = "dropoff";
    private String  _LOCATE_MESSAGGE = "locate";
    
    
    Optional<ReturnMessage> assignJourney(JsonObject journey);
    Optional<ReturnMessage> locateJourney(String journeyId);
    Optional<ReturnMessage> serviceStatus();
    Optional<ReturnMessage> dropOff();
    
    
    //calculate here final price to ensure final price comes from server
    @ConsumeEvent(value = "get-basket-event")
    public CompletionStage<CarsOld> getBasketByCode(String code) {
        log.debug("getting basket by code {}", code);
        return CompletableFuture.supplyAsync(() ->
                this.basketRepository.getBasketByCode(code).orElse(null)
        );
    }

    @ConsumeEvent(value = "add-item-basket-event")
    public CompletionStage<CarsOld> addItemBasket(AddItemToBasketEvent event) {
        log.debug("adding item {} to basket {}", event.itemCode, event.basketCode);
        //check if item exists
        Optional<Item> item = this.itemPort.getItemByCode(event.itemCode);
        if (!item.isPresent())
            return CompletableFuture.completedFuture(null);

        Optional<Journeys> discount = this.discountPort.getItemByItemCode(item.get().getCode());
        log.debug("getting discount from item basket {}", event.basketCode);
        return CompletableFuture.supplyAsync(() ->
                this.basketRepository
                        .addItemToBasket(event.basketCode, item.get(), discount)
                        .orElse(null)
        );
    }*/

}
