package me.dnevado.cabify.challenge;


import java.util.Optional;

import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;

import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RoutingExchange;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import me.dnevado.cabify.challenge.domain.CabifyRepository;
import me.dnevado.cabify.challenge.domain.model.ReturnMessage;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class CabifyApplication {
	
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    @Inject
    CabifyRepository service;

    private Response responseBuilder(Optional<ReturnMessage> message)
    {
    	int statusCode = Integer.parseInt(message.get().getStatusCode());    
    	//StatusType statusCode = StatusType.
    	return Response.status(statusCode).entity(message.get().getStatusDescription()).build();
    	
    }
    
    @Path("/journey")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response journey(String journey) {
    	log.info(journey);
    	Optional<ReturnMessage> message = service.assignJourney(journey)  ;  	
    	return responseBuilder(message);
    }
    
    @Path("/cars")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response cars(String cars) {
    	log.info(cars);
    	Optional<ReturnMessage> message = service.createAvailableCars(cars);
    	return responseBuilder(message);
    }
    
    @GET
    @Path("/status")
    public Response status(){    
    	log.info("status:");
    	Optional<ReturnMessage> message = service.serviceStatus();    	
    	return responseBuilder(message);    	
    }

   
    @GET
    @Path("/test")
    public Response test(){    
    	return Response.status(404).entity("Task not provided").build();
    }
    
    @POST
    @Path("/locate")
    public Response locate(@FormParam("ID") String id ){
    	log.info("locate:", id);
    	Optional<ReturnMessage> message = service.locateJourney(id);    	
    	return responseBuilder(message);
    	
    }
    
    @POST
    @Path("/dropoff")
    public Response dropoff(@FormParam("ID") String id ){
    	log.info("dropoff:", id);
    	Optional<ReturnMessage> message = service.dropOff(id);    	
    	return responseBuilder(message);
    }
    
}
