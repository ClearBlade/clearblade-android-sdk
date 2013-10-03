package com.clearblade.platform.api;


public abstract class DataCallback {
	public abstract void done(Item[] response);
	public void error(ClearBladeException exception){
		
	}
	
}
