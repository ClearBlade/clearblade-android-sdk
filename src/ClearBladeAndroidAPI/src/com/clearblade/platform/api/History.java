package com.clearblade.platform.api;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class History {
	private HistoryItem[] historyArray;
	
	public HistoryItem[] loadHistoryJSON(String json) throws ClearBladeException{
		// parse JSON in to Json Element
		JsonElement toJsonElement = new JsonParser().parse(json);
		// Get store JsonElement as JsonArray
		JsonArray array = toJsonElement.getAsJsonArray();
		// If the array size is 0, then no item was found; Throw ClearBladeExcepetion
		if(array.size() == 0){
			throw new ClearBladeException("Failed to parse history response");
		}
		ArrayList<HistoryItem> al = new ArrayList<HistoryItem>();
		
		for (int i = 0; i < array.size(); i++) {
			JsonElement elem = array.get(i);
			
			JsonObject jo = elem.getAsJsonObject();
			String ui =jo.get("user-id").getAsString();
			String msg = jo.get("message").getAsString();
			String sd = jo.get("send-date").getAsString();
			al.add(new HistoryItem(ui, msg, sd));
		}
		historyArray = new HistoryItem[al.size()];
		historyArray = al.toArray(historyArray); 
		return historyArray;	
	}
}
