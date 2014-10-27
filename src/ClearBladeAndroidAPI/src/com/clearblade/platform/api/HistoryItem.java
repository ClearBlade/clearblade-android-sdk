package com.clearblade.platform.api;

public class HistoryItem {
	String userId;
	String message;
	String sendDate; 
	
	public HistoryItem(String userId, String message, String sendDate) {
		this.userId = userId;
		this.message = message;
		this.sendDate = sendDate;
	}
}
