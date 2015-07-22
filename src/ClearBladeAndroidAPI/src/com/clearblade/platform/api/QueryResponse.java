package com.clearblade.platform.api;

import com.google.gson.JsonArray;

public class QueryResponse {

	//data pulled from server response
	private int CURRENTPAGE;
	private String NEXTPAGEURL;
	private String PREVPAGEURL;
	private int TOTAL;
	private JsonArray DATA;
	
	//we will convert the JsonArray into an array of items 
	private Item[] dataItems;
	
	public QueryResponse() {
		
	}

	public int getCurrentPage() {
		return CURRENTPAGE;
	}
	public String getNextPageURL() {
		return NEXTPAGEURL;
	}
	public String getPrevPageURL() {
		return PREVPAGEURL;
	}
	public int getTotalCount() {
		return TOTAL;
	}
	public Item[] getDataItems() {
		return dataItems;
	}
	public void setDataItems(Item[] items) {
		dataItems = items;
	}
	public String getDataJsonAsString() {
		return DATA.toString();
	}
	
}
