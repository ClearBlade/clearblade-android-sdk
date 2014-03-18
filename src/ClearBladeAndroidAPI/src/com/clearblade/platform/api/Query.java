package com.clearblade.platform.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.clearblade.platform.api.internal.DataTask;
import com.clearblade.platform.api.internal.PlatformCallback;
import com.clearblade.platform.api.internal.PlatformResponse;
import com.clearblade.platform.api.internal.RequestEngine;
import com.clearblade.platform.api.internal.RequestProperties;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 * This class consists exclusively of instance methods that operate on ClearBlade Collections and Items.
 * <p>
 * ClearBlade Query are objects that can query Cloud platform collections for Items.
 * A typical example would be:
 * <pre> 
 * 		Query query = new Query(collectionId);
 * 		query.equalTo("firstName", "John").greaterThan("age",40);
 *		query.fetch(new DataCallback(){
 *
 *			@Override
 *			public void done(Item[] items) {
 *				String msg = "";
 *				for (int i=0;i<items.length;i++){
 *					msg = msg + items[i].toString()+",";
 *				}	
 *			}
 *
 *			@Override
 *			public void error(ClearBladeException exception) {
 *				// TODO Auto-generated method stub				
 *			}
 *		});
 * </pre>
 * </p>
 *
 * @author  Clyde Byrd, Aaron Allsbrook
 * @see Item
 * @see Query
 * @see ClearBladeException
 * @since   1.0
 * 
 */

public class Query {
	private String collectionId;
	private QueryObj queryObj = new QueryObj();
	private ArrayList<QueryObj> queryObjs = new ArrayList<QueryObj>();
	private int pageSize;
	private int pageNum;
	
	private RequestEngine request;	// used to make API requests

	/**
	 * Constructs a new Query object for modifying a collection
	 * A collection id must be set
	 */
	public Query(){
		this.request = new RequestEngine();
	}
	
	/**
	 * Constructs a new Query object for modifying a collection
	 * @param collectionId - The id of the collection to be queried
	 */
	public Query(String collectionId){
		this.request = new RequestEngine();
		this.setCollectionId(collectionId);
	}
	
	/**
	 * Creates an equality clause in the query object 
	 * <pre>
	 * Query query = new Query(collectionId);
	 * query.equalTo('name', 'John');
	 * query.fetch(new DataCallback{
	 * 	  public void done(Item[] items){
	 *       //your logic here
	 *    }
	 * });
	 * //will only match if an item has an attribute 'name' that is equal to 'John'
	 * </pre>
	 * @param field - name of the column to be used for query criteria
	 * @param value - the value of the search criteria
	 * @return modified Query object for chaining purposes.
	 */
	public Query equalTo(String field, Object value) {
		FieldValue fv = new FieldValue(field, value);
		if (queryObj.EQ==null){
			queryObj.EQ = new ArrayList<FieldValue>();
		}
		queryObj.EQ.add(fv);
		return this;
	}
	
	/**
	 * Creates an non equality clause in the query object 
	 * <pre>
	 * Query query = new Query(collectionId);
	 * query.notEqualTo('name', 'John');
	 * query.fetch(new DataCallback{
	 * 	  public void done(Item[] items){
	 *       //your logic here
	 *    }
	 * });
	 * //will only match if an item has an attribute 'name' that is not equal to 'John'
	 * </pre>
	 * @param field - name of the column to be used for query criteria
	 * @param value - the value of the search criteria
	 * @return modified Query object for chaining purposes.
	 */
	public Query notEqual(String field, Object value) {
		FieldValue fv = new FieldValue(field, value);
		if (queryObj.NEQ==null){
			queryObj.NEQ = new ArrayList<FieldValue>();
		}
		queryObj.NEQ.add(fv);
		return this;
	}
	
	/**
	 * Creates an greater than clause in the query object 
	 * <pre>
	 * Query query = new Query(collectionId);
	 * query.greaterThan('age', '18');
	 * query.fetch(new DataCallback{
	 * 	  public void done(Item[] items){
	 *       //your logic here
	 *    }
	 * });
	 * </pre>
	 * @param field - name of the column to be used for query criteria
	 * @param value - the value of the search criteria
	 * @return modified Query object for chaining purposes.
	 */
	public Query greaterThan(String field, Object value) {
		FieldValue fv = new FieldValue(field, value);
		if (queryObj.GT==null){
			queryObj.GT = new ArrayList<FieldValue>();
		}
		queryObj.GT.add(fv);
		return this;
	}
	
	/**
	 * Creates an greater than or equal to clause in the query object 
	 * <pre>
	 * Query query = new Query(collectionId);
	 * query.greaterThanEqualTo('age', '18');
	 * query.fetch(new DataCallback{
	 * 	  public void done(Item[] items){
	 *       //your logic here
	 *    }
	 * });
	 * </pre>
	 * @param field - name of the column to be used for query criteria
	 * @param value - the value of the search criteria
	 * @return modified Query object for chaining purposes.
	 */
	public Query greaterThanEqualTo(String field, Object value){
		FieldValue fv = new FieldValue(field, value);
		if (queryObj.GTE==null){
			queryObj.GTE = new ArrayList<FieldValue>();
		}
		queryObj.GTE.add(fv);
		return this;
	}
	
	/**
	 * Creates a less than to clause in the query object 
	 * <pre>
	 * Query query = new Query(collectionId);
	 * query.lessThan('age', '18');
	 * query.fetch(new DataCallback{
	 * 	  public void done(Item[] items){
	 *       //your logic here
	 *    }
	 * });
	 * </pre>
	 * @param field - name of the column to be used for query criteria
	 * @param value - the value of the search criteria
	 * @return modified Query object for chaining purposes.
	 */
	public Query lessThan(String field, Object value) {
		FieldValue fv = new FieldValue(field, value);
		if (queryObj.LT==null){
			queryObj.LT = new ArrayList<FieldValue>();
		}
		queryObj.LT.add(fv);
		return this;
	}
	
	/**
	 * Creates a less than to clause in the query object 
	 * <pre>
	 * Query query = new Query(collectionId);
	 * query.lessThanEqualTo('age', '18');
	 * query.fetch(new DataCallback{
	 * 	  public void done(Item[] items){
	 *       //your logic here
	 *    }
	 * });
	 * </pre>
	 * @param field - name of the column to be used for query criteria
	 * @param value - the value of the search criteria
	 * @return modified Query object for chaining purposes.
	 */
	public Query lessThanEqualTo(String field, Object value){
		FieldValue fv = new FieldValue(field, value);
		if (queryObj.LTE==null){
			queryObj.LTE = new ArrayList<FieldValue>();
		}
		queryObj.LTE.add(fv);
		return this;
	}
	
	/**
	 * Creates an ascending clause in the query object 
	 * <pre>
	 * Query query = new Query(collectionId);
	 * query.ascending('age');
	 * query.fetch(new DataCallback{
	 * 	  public void done(Item[] items){
	 *       //your logic here
	 *    }
	 * });
	 * </pre>
	 * @param field - name of the column to be used for sorting in ascending manner
	 */
	public void ascending(String field){
		
	}
	
	/**
	 * Creates an descending clause in the query object 
	 * <pre>
	 * Query query = new Query(collectionId);
	 * query.descending('age');
	 * query.fetch(new DataCallback{
	 * 	  public void done(Item[] items){
	 *       //your logic here
	 *    }
	 * });
	 * </pre>
	 * @param field - name of the column to be used for sorting in descending manner
	 */
	public void descending(String field){
		
	}
	
	public void or(Query orQuery){
		queryObjs.add(orQuery.queryObj);
		//queryObj = orQuery.queryObj;
	}
	
	/**
	 * Creates an limit in the query object on the total number of results
	 * <pre>
	 * Query query = new Query(collectionId);
	 * query.limit(50);
	 * query.fetch(new DataCallback{
	 * 	  public void done(Item[] items){
	 *       //your logic here
	 *    }
	 * });
	 * </pre>
	 * @param field - name of the column to be used for sorting in descending manner
	 */
	public void setPageSize(int pageSize){
		this.pageSize = pageSize;
	}
	
	/**
	 * Creates an offset in the query object on position to show return results
	 * <pre>
	 * Query query = new Query(collectionId);
	 * query.offset(75);
	 * query.fetch(new DataCallback{
	 * 	  public void done(Item[] items){
	 *       //your logic here
	 *    }
	 * });
	 * </pre>
	 * @param field - name of the column to be used for sorting in descending manner
	 */
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	
	/**
	 * Creates an offset in the query object on position to show return results
	 * <pre>
	 * Query query = new Query(collectionId);
	 * query.offset(75);
	 * query.fetch(new DataCallback{
	 * 	  public void done(Item[] items){
	 *       //your logic here
	 *    }
	 * });
	 * </pre>
	 * @param field - name of the column to be used for sorting in descending manner
	 */
	public void fetch(final DataCallback callback) {
		fetchSetup();
		DataTask asyncFetch = new DataTask(new PlatformCallback(this, callback) {

			@Override
			public void done(String response) {
				//return json element so dev can access paging data included in response
				callback.done(new JsonParser().parse(response));
			}

			@Override
			public void error(ClearBladeException exception) {
				callback.error(exception);
			}
			
		});
		asyncFetch.execute(request);
	
	}
	
	public Item[] fetchSync() throws ClearBladeException{

		fetchSetup();
		PlatformResponse resp = request.execute();
		Item[] ret;
		if(resp.getError()) {
			throw new ClearBladeException("Call to fetch failed:"+resp.getData());
		} else {
			ret = convertJsonArrayToItemArray((String)resp.getData());
		}
		return ret;
	}
	
	private void fetchSetup(){
		String queryParam = getFetchURLParameter();
		RequestProperties headers = new RequestProperties.Builder().method("GET").endPoint("api/" +collectionId+ queryParam).build();
		request.setHeaders(headers);
	}
	
//	public Item[] fetch(){
//		return null;
//	}
	
	protected String filtersAsJsonString() {
		ArrayList<QueryObj> temp = queryObjs;
		if (queryObjs.size()==0) {
			//we havent done an or, so just build up an array of the queryObj
			//we can use the queryObjs list becuase the user may continue to build on the Query
			//for future use
			temp = new ArrayList<QueryObj>();
		}
		temp.add(queryObj);
		String param = "";//gson.toJson(temp);
		Iterator<QueryObj> it = temp.iterator();
		while(it.hasNext())
		{
		    QueryObj obj = it.next();
		    param = param+stringifyQuery(obj);
		    if (it.hasNext()){
		    	//there is an or
		    	param=param+",";
		    }
		}
		if (param.length()>0) {
			param="["+param+"]";
		}
		
		return param;
	}
	
	/**
	 * Internal only, made public for test and verification.  Returns the query string parameter necessary to implement the fetch query
	 * @return String
	 */
	public String getFetchURLParameter(){
		String param = "{";
		//add filters to url param
		param += "\"FILTERS\":" + filtersAsJsonString();
		//if defined add page num
		if(this.pageNum >= 0){
			param += ",\"PAGENUM\":" + this.pageNum;
		}
		//if defined add page size
		if(this.pageSize > 0){
			param += ",\"PAGESIZE\":" + this.pageSize;
		}
		//if defined add sort
		param += "}";
		System.out.println(param);
		try {
			param = URLEncoder.encode(param, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (param.length()>0){
			param = "?query="+param;
		}
		return param;
	}
	
	protected String queryAsJsonString() {
		ArrayList<QueryObj> temp = queryObjs;
		if (queryObjs.size()==0) {
			//we havent done an or, so just build up an array of the queryObj
			//we can use the queryObjs list becuase the user may continue to build on the Query
			//for future use
			temp = new ArrayList<QueryObj>();
			temp.add(queryObj);
		}
		String param = "";//gson.toJson(temp);
		Iterator<QueryObj> it = temp.iterator();
		while(it.hasNext())
		{
		    QueryObj obj = it.next();
		    param = param+stringifyQuery(obj);
		    if (it.hasNext()){
		    	//there is an or
		    	param=param+",";
		    }
		}
		if (param.length()>0) {
			param="["+param+"]";
			//add extra brackets for bug in platform
			param = "["+param+"]";
		}
		return param;
	}
	
	/**
	 * Internal only, made public for test and verification.  Returns the query string parameter necessary to implement the query
	 * @return String
	 */
	public String getURLParameter(){
		String param = queryAsJsonString();
		try {
			param = URLEncoder.encode(param, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (param.length()>0){
			param = "?query="+param;
		}
		return param;
	}
	
	private String stringifyQuery(QueryObj obj){
		String ret = "";
		ret= ret + stringifyParam("EQ", obj.EQ);
		ret= ret + stringifyParam("GT", obj.GT);
		ret= ret + stringifyParam("GTE", obj.GTE);
		ret= ret + stringifyParam("LT", obj.LT);
		ret= ret + stringifyParam("LTE", obj.LTE);
		ret= ret + stringifyParam("NEQ", obj.NEQ);
		if (ret.length()>0) {
			ret = "{"+ ret+ "}";
		}
		return ret;
	}
	
	private String stringifyParam(String paramType, ArrayList<FieldValue> params){
		if (params == null || params.size()==0){return "";}
		
		String ret="\""+paramType+"\":[";
		Iterator<FieldValue> iter = params.iterator();
		while(iter.hasNext()){
			FieldValue fv = (FieldValue) iter.next();
			Class valueClass = fv.value.getClass();
			if(valueClass.getName().toLowerCase().equalsIgnoreCase("java.lang.string")){
				ret = ret + "{\""+fv.field+"\":\""+fv.value+"\"}";
			}else if(valueClass.getName().toLowerCase().equalsIgnoreCase("java.lang.integer")){
				ret = ret + "{\""+fv.field+"\":"+Integer.toString((Integer) fv.value)+"}";
			}
			if (iter.hasNext()){
				ret=ret+",";
			}
		}
		ret=ret+"]";
		return ret;
	}
	
	private HashMap<String,Object> changes = new HashMap<String,Object>();
	
	/**
	 * Adds a change set to the query.  When update is run all of the changes
	 * are honored across the matching criteria 
	 * @param name - the name of the column to be modified
	 * @param value - the new value to insert into the column
	 */
	public void addChange(String name, Object value){
		changes.put(name, value);
	}
	
	/**
	 * Clears the changes currently set in they query.  Update will perform no action after this 
	 * is executed.
	 */
	public void clearChanges(){
		changes = new HashMap<String, Object>();
	}
	
	/**
	 * Call an update on all items matching the query criteria to conform to the changes that have been added via the addChange method
	 * <pre>
	 * Query query = new Query(collectionText.getText().toString());
	 *	query.equalTo("name", "John");
	 *	query.addChange("name", "Johan");
	 *	query.update( new DataCallback() {
	 *
	 *		@Override
	 *		public void done(Item[] response) {
	 *		}
	 *
	 *		@Override
	 *		public void error(ClearBladeException exception) {
	 *			
	 *		}
	 *	});
	 * </pre>
	 * @param callback
	 */
	public void update(final DataCallback callback) {
		updateSetup();
		DataTask asyncFetch = new DataTask(new PlatformCallback(this, callback){

			@Override
			public void done(String response) {
				Item[] ret = convertJsonArrayToItemArray(response);
				callback.done(ret);
			}

			@Override
			public void error(ClearBladeException exception) {
				callback.error(exception);
			}
			
		});
		asyncFetch.execute(request);
		changes = new HashMap<String,Object>();
	}
	
	public Item[] updateSync() throws ClearBladeException{
		updateSetup();
		PlatformResponse resp = request.execute();
		Item[] ret;
		if(resp.getError()) {
			throw new ClearBladeException("Call to fetch failed:"+resp.getData());
		} else {
			ret = convertJsonArrayToItemArray((String)resp.getData());
		}
		return ret;
	}
	
	private void updateSetup(){
		JsonObject payload = new JsonObject();
		payload.addProperty("$set", changeSetMapAsJsonString());
		//JsonObject query = new JsonObject();
		JsonElement toObject = new JsonParser().parse(queryAsJsonString());
		payload.add("query", toObject);
		RequestProperties headers = new RequestProperties.Builder().method("PUT").endPoint("api/" + collectionId).body(payload).build();
		request.setHeaders(headers);
	}
	
	private String changeSetMapAsJsonString(){
		
		String jsonString = "{";
		
		for (Map.Entry<String, Object> entry : changes.entrySet()) {
		    String key = entry.getKey();
		    Object value = entry.getValue();
		    if(value.getClass().getName().equalsIgnoreCase("java.lang.string")){
		    	jsonString += "\"" + key + "\":" + "\"" + value + "\"";
		    }else if(value.getClass().getName().equalsIgnoreCase("java.lang.integer")){
		    	jsonString += "\"" + key + "\":" + value.toString();
		    }
		    jsonString += ",";
		}
		
		//remove trailing comma
		jsonString = jsonString.substring(0, jsonString.length()-1);
		
		jsonString += "}";
		
		return jsonString;
		
	}
	
	/**
	 * Removes on all items matching the query criteria within a Collection
	 * <pre>Query query = new Query(collectionText.getText().toString());
	 *	query.equalTo("name", "John");
	 *	query.remove( new DataCallback() {
	 *
	 *		@Override
	 *		public void done(Item[] response) {
	 *		}
	 *
	 *		@Override
	 *		public void error(ClearBladeException exception) {
	 *			
	 *		}
	 *	});
	 * </pre>
	 * @param callback
	 */
	public void remove(final DataCallback callback)  {

		String queryParam = getURLParameter();
		
		RequestProperties headers = new RequestProperties.Builder().method("DELETE").endPoint("api/" +collectionId+ queryParam).build();
		request.setHeaders(headers);
		
		DataTask asyncFetch = new DataTask(new PlatformCallback(this, callback){

			@Override
			public void done(String response) {
				Item[] ret = convertJsonArrayToItemArray(response);
				callback.done(ret);
			}

			@Override
			public void error(ClearBladeException exception) {
				callback.error(exception);
			}
			
		});
		asyncFetch.execute(request);
	}
	
	public Item[] removeSync() throws ClearBladeException{
		removeSetup();
		PlatformResponse resp = request.execute();
		Item[] ret;
		if(resp.getError()) {
			throw new ClearBladeException("Call to fetch failed:"+resp.getData());
		} else {
			ret = convertJsonArrayToItemArray((String)resp.getData());
		}
		return ret;
	}
	
	private void removeSetup(){
		String queryParam = getURLParameter();
		
		RequestProperties headers = new RequestProperties.Builder().method("DELETE").endPoint("api/" +collectionId+ queryParam).build();
		request.setHeaders(headers);
	}

	/**
	 * @return the collectionId
	 */
	public String getCollectionId() {
		return collectionId;
	}

	/**
	 * @param collectionId the collectionId to set
	 */
	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}
	
	private class FieldValue{
		public String field;
		public Object value;
		
		public FieldValue(String field, Object value){
			this.field = field;
			this.value = value;
		}
	}
	private class QueryObj{
		public ArrayList<FieldValue> EQ;
		public ArrayList<FieldValue> GT;
		public ArrayList<FieldValue> GTE;
		public ArrayList<FieldValue> LT;
		public ArrayList<FieldValue> LTE;
		public ArrayList<FieldValue> NEQ;
		
	}
	
	/**
	 * Converts a JSON Array in String format to an Item Array.
	 * @private
	 * @param json A JSON Array in string format
	 * @return Item[] An array of Items
	 * @throws ClearBladeException will be thrown on error!
	 */
	private Item[] convertJsonArrayToItemArray(String json) {
		// Parse the JSON string in to a JsonElement
		JsonElement jsonElement = new JsonParser().parse(json);
		// Store the JsonElement as a JsonArray
		JsonArray array = jsonElement.getAsJsonArray();
		ArrayList<Item> items = new ArrayList<Item>();// new Item[array.size()];
		Iterator<JsonElement> iter = array.iterator();
		while(iter.hasNext()){
			
			JsonElement val = iter.next();
			if (val.isJsonObject()){
				JsonObject temp = val.getAsJsonObject();
				if (temp.entrySet().size()==0){
					return (new Item[0]);
				}else {
					items.add(new Item(temp, getCollectionId()));
//					for (Entry<String, JsonElement> entry : temp.entrySet()) {
//					    JsonObject elementTemp = entry.getValue().getAsJsonObject();//.getAsJsonArray("unterfeld");
//					    
//					    items.add(new Item(entry, getCollectionId()));
//					    System.out.println("lets take a peak at the member");
//					}
				}
			} 
		}
		
		Item[] ret = new Item[items.size()];
		ret = (Item[]) items.toArray(ret);
		return ret;
	}
	
	
}
