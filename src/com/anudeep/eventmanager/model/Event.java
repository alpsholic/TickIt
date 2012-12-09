package com.anudeep.eventmanager.model;

import java.util.Date;

public class Event {

	public Event(){}
	public Event(long id,String code, String name, String venue) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.venue = venue;
	}

	private long id;
	private String code;
	private String name;
	private String venue;
	private Date startTime;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getVenue() {
		return venue;
	}
	public void setVenue(String venue) {
		this.venue = venue;
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return "Event:"+ name+", Venue:"+venue;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getStartTime() {
		return startTime;
	}
	
}
