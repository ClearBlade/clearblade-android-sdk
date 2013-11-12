package com.clearblade.platform.api;

public class MessageCallback {
	public void done(String topic, String message){
		//override to get a string message
	}
	public void error(ClearBladeException exception){
		//override to catch an error
	}
	
	public void done(String topic, Item message){
		//override to get an Item message
	}
	
	public void done(String topic, byte[] message){
		//override to get a byte[] message
	}
}
