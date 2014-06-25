package com.clearblade.platform.api;

import com.google.gson.JsonElement;


public abstract class DataCallback {
	public void done(Item[] response){
		
	}
	public void done(JsonElement response){
		
	}
	public void error(ClearBladeException exception){
		
	}
	public void done(QueryResponse resp) {
		
	}
	
}
