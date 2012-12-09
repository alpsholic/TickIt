package com.anudeep.eventmanager.model;

import java.util.Date;

public class Ticket {

	private long id;
	private long eventId;
	private String state;
	private String barcode;
	private Date creationTime;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getEventId() {
		return eventId;
	}
	public void setEventId(long eventId) {
		this.eventId = eventId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public Date getCreationTime() {
		return creationTime;
	} 
	
	@Override
	public String toString() {
		return "TicketNo:"+ id+", State:"+state;
	}
	
}
