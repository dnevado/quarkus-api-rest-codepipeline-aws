package me.dnevado.cabify.challenge.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import me.dnevado.cabify.challenge.domain.CabifyRepository;
import me.dnevado.cabify.challenge.domain.model.Car;
import me.dnevado.cabify.challenge.domain.model.ReturnMessage;

import javax.enterprise.context.ApplicationScoped;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class CabifyRepositoryInMemory implements CabifyRepository {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    /* id car
     * available seats 
     */
    private final Map<String, Long> availableCars = new ConcurrentHashMap<>();    
    /* id group 
     * group people  
     */
    private final Map<String, Long> journeys = new ConcurrentHashMap<>();
    
    /*  id group  
     *  id car
     */
    private final Map<String, Long> journeyCars = new ConcurrentHashMap<>();
 

    @Override
    public Optional<ReturnMessage> createAvailableCars(JsonArray availablecars) {
        log.trace("createAvailableCars {}", availablecars);      
        ReturnMessage message = new ReturnMessage("1","1");
        for (int i = 0; i < availablecars.size(); i++) {
        	JsonObject jsonCar = availablecars.getJsonObject(i);
        	String id = jsonCar.getString("id");
        	Long seats  = jsonCar.getLong("seats");        	
        	availableCars.put(id,seats);
        	
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
    public Optional<ReturnMessage> dropOff() {
        log.trace("dropOff");        
        availableCars.clear();
        journeys.clear();        
        journeyCars.clear();        
        ReturnMessage message;
        message =  new ReturnMessage("200","OK");        
        return Optional.of(message);
    }


	@Override
	public Optional<ReturnMessage> assignJourney(JsonObject journey) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Optional<ReturnMessage> locateJourney(String journeyId) {
		// TODO Auto-generated method stub
        log.trace("locateJourney {}", journeyId);
        ReturnMessage message = null;
        Long  carId ; 
        if (this.journeyCars.containsKey(journeyId))
        {
        	carId = this.journeyCars.get(journeyId);
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
