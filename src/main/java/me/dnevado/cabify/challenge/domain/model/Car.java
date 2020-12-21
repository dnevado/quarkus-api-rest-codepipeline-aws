package me.dnevado.cabify.challenge.domain.model;

public class Car {

    private final String Id;    
    //amount of seats 
    private final int seats;
    

    public Car(String Id, int seats) {
        this.Id = Id;
        this.seats = seats;        
    }

    public String getId() {
        return Id;
    }

    public int getSeats() {
        return seats;
    }

}
