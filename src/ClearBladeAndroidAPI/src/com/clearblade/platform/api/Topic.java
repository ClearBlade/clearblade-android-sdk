package com.clearblade.platform.api;

public class Topic {
	
	private int qos;
	private String topic;
	
	public Topic(String topic){
		this.topic = topic;
		qos = 0;
	}
	
	public Topic(String topic, int qos){
		this.topic = topic;
		this.qos = qos;
	}
	
	private void subscribe(){
		
	}
	
	public void publish() {
		
	}
	
	public void unsubscribe(){
		
	}
}
