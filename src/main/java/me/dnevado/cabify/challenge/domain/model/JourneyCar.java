package me.dnevado.cabify.challenge.domain.model;

public class JourneyCar {

    private long  carId ;      
    private long  groupId ;
    private short assigned;        
    

    public JourneyCar(long carId, long groupId) {
        this.carId = carId;
        this.groupId = groupId;                
    }


	public long getCarId() {
		return carId;
	}


	public void setCarId(long carId) {
		this.carId = carId;
	}


	public long getGroupId() {
		return groupId;
	}


	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}


	public short getAssigned() {
		return assigned;
	}


	public void setAssigned(short assigned) {
		this.assigned = assigned;
	}



}
