package me.dnevado.cabify.challenge.domain.model;

public class Car {

    private long id ;      
    private int seats; // total seats
    private int reservedSeats; // assigned seats
    
    

    public Car(long carId, int availableSeats, int reservedSeats) {
        this.id = carId;
        this.seats = availableSeats;        
        this.reservedSeats = reservedSeats;
    }

	public long getCarId() {
		return id;
	}
	public void setCarId(long carId) {
		this.id = carId;
	}
	public int getAvailableSeats() {
		return seats;
	}
	public void setAvailableSeats(short seats) {
		this.seats = seats;
	}
	public int getReservedSeats() {
		return reservedSeats;
	}
	public void setReservedSeats(int reservedSeats) {
		this.reservedSeats = reservedSeats;
	}
}
