package com.anudeep.eventmanager.util;

import java.util.UUID;

public class BarcodeHelper {

	
	public String getUUId(){
		//unique id - pseudo random number 
		return UUID.randomUUID().toString();
	}
}
