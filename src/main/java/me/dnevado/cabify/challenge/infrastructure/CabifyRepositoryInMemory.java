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

import java.util.Map;
import java.util.stream.Stream;

@ApplicationScoped
public class CabifyRepositoryInMemory implements CabifyRepository {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());

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
    public Optional<ReturnMessage> createAvailableCars(JsonArray availablecars) {
        log.trace("createAvailableCars {}", availablecars);      
        availableCars.clear();
        peopleGroups.clear();        
        journeyCars.clear();              
        ReturnMessage message = new ReturnMessage("1","1");
        for (int i = 0; i < availablecars.size(); i++) {
        	JsonObject jsonCar = availablecars.getJsonObject(i);        	
        	Car  car = new Gson().fromJson(jsonCar.toString(), Car.class);        	        
        	availableCars.put(car.getCarId(),car);        	
        }
        return Optional.of(message);
    }

    
    @Override
    public Optional<ReturnMessage> serviceStatus() {
        log.trace("serviceStatus");        
        boolean serviceOK = availableCars.size() > 0;
        ReturnMessage message;
        if (serviceOK)
        	message =  new ReturnMessage("200","OK");
        else
            message = new ReturnMessage("201","NOOK");
        
        return Optional.of(message);
    }
    
    @Override
    public Optional<ReturnMessage> dropOff(Long groupId) {
    	log.trace("assignJourney {}", groupId);
        ReturnMessage message = new ReturnMessage("404", "Not Found");        
        if (this.journeyCars.containsKey(groupId))
        {
        	JourneyCar journey = this.journeyCars.get(groupId);
        	this.journeyCars.remove(groupId);
        	Car foundCar = availableCars.get(journey.getCarId());
        	Long  groupSize  = peopleGroups.get(groupId);    
        	peopleGroups.remove(groupId);
        	message.setStatusCode("200");
    		message.setStatusDescription("OK");    		
    		// add  new available cars seats
    		int finalReservedSeats = foundCar.getReservedSeats() - groupSize.intValue();
    		foundCar.setReservedSeats(finalReservedSeats); 
    		availableCars.put(foundCar.getCarId(), foundCar);        	
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
    
	@Override
	public Optional<ReturnMessage> assignJourney(JsonObject journey) {
		// TODO Auto-generated method stub
        log.trace("assignJourney {}", journey);
        ReturnMessage message = new ReturnMessage("400", "Bad Request");
        Group  group = new Gson().fromJson(journey.toString(), Group.class);              
        // order preserved                 
        long foundCarId = -1;
        for (Long carKey : availableCars.keySet()) {        	
        	foundCarId =  isReachable(availableCars.get(carKey),group.getPeople());   
        	if (foundCarId!=-1)
        	{
        		message.setStatusCode("200");
        		message.setStatusDescription("OK");
        		Car foundCar = availableCars.get(carKey);
        		// reduce available cars seats
        		int finalReservedSeats = foundCar.getReservedSeats() + group.getPeople();
        		foundCar.setReservedSeats(finalReservedSeats);
        		// add journey to people
        		JourneyCar journeyCar = new JourneyCar(foundCarId, group.getId());
        		journeyCars.put(group.getId(), journeyCar);
        		break;
        	}
        }
        
        
                
        return Optional.of(message);
	}


	@Override
	public Optional<ReturnMessage> locateJourney(Long groupId) {
		// TODO Auto-generated method stub
        log.trace("locateJourney {}", groupId);
        ReturnMessage message = null;
        Long  carId; 
        if (this.journeyCars.containsKey(groupId))
        {
        	JourneyCar journey = this.journeyCars.get(groupId);
        	carId = journey.getCarId();
        	message =  new ReturnMessage("200", String.valueOf(carId));
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
