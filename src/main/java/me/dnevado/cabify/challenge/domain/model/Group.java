package me.dnevado.cabify.challenge.domain.model;

public class Group {

    private long id ;      
    private short  people;        
    

    public Group(long id, short people) {
        this.id = id;
        this.people = people;                
    }


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public short getPeople() {
		return people;
	}


	public void setPeople(short people) {
		this.people = people;
	}



}
