package me.dnevado.cabify.challenge.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import me.dnevado.cabify.challenge.domain.CabifyRepository;
import me.dnevado.cabify.challenge.domain.model.Car;
import me.dnevado.cabify.challenge.domain.model.Group;
import me.dnevado.cabify.challenge.domain.model.JourneyCar;
import me.dnevado.cabify.challenge.domain.model.ReturnMessage;

import javax.enterprise.context.ApplicationScoped;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.stream.Stream;

@ApplicationScoped
public class CabifyRepositoryInMemory implements CabifyRepository {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    
    /* True : when cars processing is over and ready */
    private  boolean serviceReady = Boolean.TRUE;

    /* id car
     * available seats 
     */	
    /* use of thread safe to preserve order and atomic operations instead of ConcurrentHashMap */    
    private final Map<Long, Car> availableCars = Collections.synchronizedMap(new LinkedHashMap<Long, Car>());

    //private final Map<String, Long> availableCars = new ConcurrentHashMap<>();    
    /* id group 
     * group people  
     */
    /* use of thread safe to preserve order and atomic operations instead of ConcurrentHashMap */    
    private final Map<Long, Long> peopleGroups = Collections.synchronizedMap(new LinkedHashMap<Long, Long>());
    //private final Map<String, Long> journeys = new ConcurrentHashMap<>();
    
    /*  id group  
     *  id car
     */
    /* use of thread safe to preserve order and atomic operations instead of ConcurrentHashMap */    
    private final Map<Long, JourneyCar> journeyCars = Collections.synchronizedMap(new LinkedHashMap<Long, JourneyCar>());
    //private final Map<String, Long> journeyCars = new ConcurrentHashMap<>();
    
        
    
    @Override
    public Optional<ReturnMessage> createAvailableCars(String availablecars) {
    	/* blocking process */
    	serviceReady = Boolean.FALSE;
        log.info("createAvailableCars {}", availablecars); 
        ReturnMessage message = new ReturnMessage("200","OK");
        // avoiding overritten of original variable in case of error 
        Map<Long, Car> temporaryAvailableCars = Collections.synchronizedMap(new LinkedHashMap<Long, Car>());
        /* Previous validation of all car entries before processing  */ 
        boolean syntaxError = Boolean.FALSE;
        boolean previousAssigned = Boolean.FALSE;
        try 
        {
            JsonArray jAvailablecars  = new JsonArray(availablecars);
            for (int i = 0; i < jAvailablecars.size(); i++) {
            	JsonObject jsonCar = jAvailablecars.getJsonObject(i);        	
            	Car  car = new Gson().fromJson(jsonCar.toString(), Car.class);
            	syntaxError = !(car.getCarId()!=0 &&  car.getReservedSeats() ==0 && car.getAvailableSeats()>0);
            	// exists and assgined , not permitted  
            	if (availableCars.containsKey(car.getCarId()))
            	{
            		Car existingCar = availableCars.get(car.getCarId());
            		previousAssigned = existingCar.getReservedSeats() >0;
            	}	
            	if (!syntaxError && !previousAssigned)
            		temporaryAvailableCars.put(car.getCarId(),car);
            	else
            	{
            		temporaryAvailableCars.clear();
            		message.setStatusCode("400");
            		message.setStatusDescription("Bad Request");
            		log.debug("Error formatting createAvailableCars {}", car); 
            		break;
            	}
            		
            }
            // everything OK?
            if (!syntaxError)
            {
	            availableCars.clear();
	            peopleGroups.clear();        
	            journeyCars.clear();
	            availableCars.putAll(temporaryAvailableCars);
	            temporaryAvailableCars.clear();
            }
            serviceReady =  Boolean.TRUE;
        }
        catch (Exception e)
        {        	
        	message.setStatusCode("400");
    		message.setStatusDescription("Bad Request");
    		serviceReady =  Boolean.TRUE;
        }   
       
        return Optional.of(message);
    }

    
    @Override
    public Optional<ReturnMessage> serviceStatus() {
        log.trace("serviceStatus");        
        boolean serviceOK = serviceReady; // up and running 
        ReturnMessage message;
        if (serviceOK)
        	message =  new ReturnMessage("200","OK");
        else
            message = new ReturnMessage("201","NOOK");
        
        return Optional.of(message);
    }
    
    private boolean isNumeric(String value)
    {
    	return  Stream.of(value)
                .filter(s -> s != null && !s.isEmpty())
                .filter(Pattern.compile("\\D").asPredicate().negate())
                .mapToLong(Long::valueOf)
                .boxed()
                .findAny()
                .isPresent();
    }
    
    /* 
     * 200 OK or 204 No Content When the group is unregistered correctly.
	   404 Not Found When the group is not to be found.
	   400 Bad Request When there is a failure in the request format or the 	payload can't be unmarshalled.
     */
    @Override
    public Optional<ReturnMessage> dropOff(String groupId) {    	
    	log.trace("dropOff {}", groupId);
        ReturnMessage message = new ReturnMessage("404", "Not Found");
        Long parsedGroupId;
        try 
        {
    	    if (!isNumeric(groupId))
            {
	           	message.setStatusCode("400");
	      		message.setStatusDescription("Bad Request");
      		
            }    	    
    	    else
    	    {
    	    	parsedGroupId = Long.parseLong(groupId);
    	    	if (this.journeyCars.containsKey(parsedGroupId))
    	        {
    	        	JourneyCar journey = this.journeyCars.get(parsedGroupId);
    	        	this.journeyCars.remove(parsedGroupId);
    	        	Car foundCar = availableCars.get(journey.getCarId());
    	        	Long  groupSize  = peopleGroups.get(parsedGroupId);    
    	        	peopleGroups.remove(parsedGroupId);
    	        	message.setStatusCode("200");
    	    		message.setStatusDescription("OK");    		
    	    		// add  new available cars seats
    	    		int finalReservedSeats = foundCar.getReservedSeats() - groupSize.intValue();
    	    		foundCar.setReservedSeats(finalReservedSeats);     	    		       
    	        }
    	    }
        }
	    catch (Exception e)
	    {
	    	message.setStatusCode("400");
      		message.setStatusDescription("Bad Request");
	    }
        return Optional.of(message);
    }
    
    /* return 
     * 		carId if assigned or -1 
     */
    private long isReachable(Car car, short neededSeats)
    {
    	long carId = -1;
    	// availibilty 
    	if (car.getAvailableSeats() -car.getReservedSeats()>=neededSeats)
    		carId = car.getCarId();
    	return carId;
    }
    
    /* 
     * 200 OK or 202 Accepted When the group is registered correctly
	   400 Bad Request When there is a failure in the request format or the
		payload can't be unmarshalled.
    */
	@Override
	public Optional<ReturnMessage> assignJourney(String journey) {
		// TODO Auto-generated method stub        
        log.info("assignJourney {}", journey); 
        ReturnMessage message = new ReturnMessage("200", "OK");
        try 
        {        		      	    	       
	        Group  group = new Gson().fromJson(journey, Group.class);
	        boolean syntaxError = !(group.getId()!=0 &&  group.getPeople()>0);
        	if (syntaxError)
        	{
        		message.setStatusCode("400");
        		message.setStatusDescription("Bad Request");
        	}
        	else
        	{	  		
		        // order preserved                 
		        long foundCarId = -1;
		        for (Long carKey : availableCars.keySet()) {        	
		        	foundCarId =  isReachable(availableCars.get(carKey),group.getPeople());   
		        	if (foundCarId!=-1)
		        	{		        		
		        		Car foundCar = availableCars.get(carKey);
		        		// reduce available cars seats
		        		int finalReservedSeats = foundCar.getReservedSeats() + group.getPeople();
		        		foundCar.setReservedSeats(finalReservedSeats);
		        		// add journey to people
		        		JourneyCar journeyCar = new JourneyCar(foundCarId, group.getId());
		        		journeyCar.setAssigned(Short.valueOf(String.valueOf(finalReservedSeats)));
		        		journeyCars.put(group.getId(), journeyCar);
		        		// add group to list 
		        		peopleGroups.put(group.getId(), Long.valueOf(group.getPeople()));
		        		break;
		        	}
		        }
		        if (foundCarId==-1)
		        {
		        	message.setStatusCode("400");
	        		message.setStatusDescription("Bad Request");
		        }
        	}
		}
		catch (Exception e)
        {
	    	message.setStatusCode("400");
	  		message.setStatusDescription("Bad Request");
        }
                        
        return Optional.of(message);
	}

	/* 
	 * 200 OK With the car as the payload when the group is assigned to a car.

	204 No Content When the group is waiting to be assigned to a car.
	
	404 Not Found When the group is not to be found.
	
	400 Bad Request When there is a failure in the request format or the
	payload can't be unmarshalled.
	 */
	@Override
	public Optional<ReturnMessage> locateJourney(String groupId) {
		// TODO Auto-generated method stub
        log.trace("locateJourney {}", groupId);
        ReturnMessage message = new ReturnMessage("400", "Bad Request");
        Long parsedGroupId;
        try 
        {
    	    if (isNumeric(groupId))
    	    {
    	    	 parsedGroupId = Long.parseLong(groupId);
    	    	 if (!this.peopleGroups.containsKey(parsedGroupId))
		         {	
    	    		 	message.setStatusCode("404");
    	    		 	message.setStatusDescription("Not Found");
		         }
    	    	 else
    	    	 {   
    	    		  // assigned or not 
			    	  Long  carId; 
			          if (this.journeyCars.containsKey(parsedGroupId))
			          {
				          JourneyCar journey = this.journeyCars.get(parsedGroupId);
				          carId = journey.getCarId();
				          message.setStatusCode("200");
	    	    		  message.setStatusDescription(String.valueOf(carId));				          
			          }
			          else
			          {
			        	  message.setStatusCode("204");
	    	    		  message.setStatusDescription("No Content");
			          }
    	    	 }
		          
    	    }
	    }
		catch (Exception e)
        {
	    	message.setStatusCode("400");
	  		message.setStatusDescription("Bad Request");
        }
        
      
        return Optional.of(message);
	}

/*    @Override
    public Optional<CarsOld> getBasketByCode(String code) {
        log.trace("getBasketByCode {}", code);
        return Optional.ofNullable(this.baskets.get(code));
    }

    @Override
    public Optional<CarsOld> addItemToBasket(String code, Item item, Optional<Journeys> discount) {
        log.trace("addItem {} ToBasket {}", code, item.getCode());
        return Optional.ofNullable(baskets.computeIfPresent(code, (c, b) -> {
            b.addItem(item);
            discount.ifPresent( d -> {
                log.info("addDiscount {} ToItem {}", d.getPercentage(), item.getCode());
                b.addItemDiscount(item, d);
            });
            return b;
        }));
    }*/
}
