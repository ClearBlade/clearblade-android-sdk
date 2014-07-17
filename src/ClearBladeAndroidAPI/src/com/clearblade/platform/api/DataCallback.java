package com.clearblade.platform.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public abstract class DataCallback {
	public void done(Item[] response){
		
	}
	public void done(JsonElement response){
		
	}
    public void done(JsonObject response) {

    }
	public void error(ClearBladeException exception){
		
	}
	public void done(QueryResponse resp) {
		
	}
	
}
