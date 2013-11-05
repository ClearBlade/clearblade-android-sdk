package com.clearblade.platform.api;

public abstract class MessageCallback {
	public abstract void done(String topic, String message);
	public void error(ClearBladeException exception){
		
	}
}
