package me.dnevado.cabify.challenge.domain;

import java.util.Optional;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import me.dnevado.cabify.challenge.domain.model.ReturnMessage;


import javax.ws.rs.core.Response;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/* import me.dnevado.cabify.challenge.domain.model.Journeys;
import me.dnevado.cabify.challenge.domain.model.Item; */

public interface CabifyRepository {

    Optional<ReturnMessage> createAvailableCars(String  eventAvailableCars);
    Optional<ReturnMessage> assignJourney(String eventJourney);
    Optional<ReturnMessage> locateJourney(String eventGroupId);
    Optional<ReturnMessage> serviceStatus();
    Optional<ReturnMessage> dropOff(String eventGroupId);
    Optional<ReturnMessage> test(String test);
    //CompletionStage<Response> test();    
    
    
    /* 
     * Optional<ReturnMessage> createAvailableCars(JsonArray AvailableCars);
    Optional<ReturnMessage> assignJourney(JsonObject journey);
    Optional<ReturnMessage> locateJourney(Long groupId);
    Optional<ReturnMessage> serviceStatus();
    Optional<ReturnMessage> dropOff(Long groupId);
     */

}
