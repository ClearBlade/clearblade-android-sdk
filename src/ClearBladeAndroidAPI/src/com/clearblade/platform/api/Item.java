package com.clearblade.platform.api;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.clearblade.platform.api.internal.DataTask;
import com.clearblade.platform.api.internal.PlatformCallback;
import com.clearblade.platform.api.internal.PlatformResponse;
import com.clearblade.platform.api.internal.RequestEngine;
import com.clearblade.platform.api.internal.RequestProperties;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * This class consists exclusively of instance methods that operate on ClearBlade Items.
 * <p>
 * ClearBlade Items are objects that create, modify, and delete data in the Cloud Platform.
 * A typical example would be:
 * <pre> 
 * Item item = new Item("CollectionId");
 * item.set("property", "value");
 * item.save( new DataCallback(){
 * 		@Override
 * 		public void done(Item[] items){
 * 			//your logic here
 * 		}
 * 
 * 		public void error(ClearBladeException exception){
 * 			//error logic here
 * 		}
 * });
 * 
 * 
 * for (Entry<String, String> pairs : itemArray[0].entrySet()) {
 *     Log.v(TAG, pairs.getKey + ":" + pairs.getValue());
 * }
 * 
 * </pre>
 * </p>
 *
 * @author  Clyde Byrd
 * @author  Aaron Allsbrook
 * @see Collection
 * @see Query
 * @see ClearBladeException
 * @since   1.0
 * 
 */
public class Item {
	
	protected JsonObject json;			// All properties are stored in this variable
	private JsonObject changes;			// All changes made to json variable are recorded here.
	private final String collectionId; 	// collection the item belongs to.
	private RequestEngine request;		// API caller
	

	/**
	 * Constructs a new Item Object that can be used to interact with the ClearBlade Cloud
	 * @param collectionType collection that this Item will be saved in.
	 */
	public Item(String collectionId) {
		this.json = new JsonObject();
		this.collectionId = collectionId;
		//long _created = new Date().getTime();
		//this.json.addProperty("_created", _created);
		//this.json.addProperty("_lastModified", _created);
		this.changes = new JsonObject();
		this.request = new RequestEngine();
	}
	
	/**
	 * Prototype to run items through the messaging
	 * @param collectionId
	 */
	public void populateFromMessaging(String value) {
		convertJsonToJsonObject(value);
	}

	/**
	 * Constructs a new Item Object that can be used to interact with the ClearBlade Cloud
	 * <p>Used only for internal use.</p>
	 * @protected
	 * @param collectionType collection that this Item will be saved in.
	 */
	protected Item(String json, String collectionId) {

		this.json = convertJsonToJsonObject(json);
		this.collectionId = collectionId;
		JsonElement itemId = this.json.get("itemId");
		JsonElement created = this.json.get("_created");

		if(itemId == null){
			this.json.addProperty("itemId", "");
		}

		if(created == null){
			//long _created = new Date().getTime();
			//this.json.addProperty("_created", _created);
			//this.json.addProperty("_lastModified", _created);
		}
		this.changes = new JsonObject();
		this.request = new RequestEngine();

	}
	
	protected Item (JsonObject json, String collectionId){
		this.collectionId = collectionId;
		this.json = json;
		JsonElement itemId = this.json.get("itemId");
		//JsonElement created = this.json.get("_created");

		if(itemId == null){
			this.json.addProperty("itemId", "");
		}

		//if(created == null){
		//	long _created = new Date().getTime();
		//	this.json.addProperty("_created", _created);
		//	this.json.addProperty("_lastModified", _created);
		//}
		this.changes = new JsonObject();
		this.request = new RequestEngine();
	}

	/**
	 * method used to clear the changes object.
	 * <p>It should be used after the changes have been saved to the database</p>
	 * @private
	 */
	private void clearChanges() {
		for (Entry<String, JsonElement> pairs : this.changes.entrySet()) {
			changes.remove(pairs.getKey());
		}
	}

	/**
	 * Method used to convert a JSON Array of length one, in string format in to a JsonObject
	 * @param json - JSON Array in string format
	 * @return JsonObject will be most up to date JsonObject for this Item
	 * @throws ClearBladeException will be thrown if the Item was not found
	 */
	private JsonObject convertJsonArrayToJsonObject(String json) throws ClearBladeException {
		// parse JSON in to Json Element
		JsonElement toJsonElement = new JsonParser().parse(json);
		// Get store JsonElement as JsonArray
		JsonArray array = toJsonElement.getAsJsonArray();
		// If the array size is 0, then no item was found; Throw ClearBladeExcepetion
		if(array.size() == 0){
			throw new ClearBladeException("Item Was Not Found");
		}
		
		return array.get(0).getAsJsonObject();
	}
	/**
	 * Method used to convert a JSON object in string format in to a JsonObject
	 * @param json - JSON Object in string format
	 * @return JsonObject will be most up to date JsonObject for this Item
	 * @throws ClearBladeException will be thrown if the Item was not found
	 */

	private JsonObject convertJsonToJsonObject(String json) {
		// parse json string in to JsonElement
		try {
			JsonElement toObject = new JsonParser().parse(json);
			return toObject.getAsJsonObject();
		}catch(JsonSyntaxException mfe){
			return null;
		}
	}

	/**
	 * Deletes an Item from the collection it belongs to in the Cloud asynchronously.
	 * @throws ClearBladeException will be thrown if the Item was not found or API call failed
	 */
	public void destroy (DataCallback callback)  {
		
		Query query= new Query(collectionId);
		query.equalTo("itemId", this.getString("itemId"));
		query.remove(callback);
	}

	/**
	 * Returns a Set that contains all the keys-value pairs of the Item
	 * @return Set<Map.Entry<String,String>> contains all key-value pairs of the Item
	 * @see Set
	 */
	public Set<Map.Entry<String, String>> entrySet() {

		HashMap<String, String> entry = new HashMap<String, String>();

		for (Entry<String, JsonElement> pairs : this.json.entrySet()) {
			entry.put(pairs.getKey(), pairs.getValue().getAsString());
		}
		return entry.entrySet();
	}

	/**
	 * If the object given is a reference to this Item it will return true,
	 * false otherwise.
	 * returns isEqual - 
	 */
	@Override 
	public boolean equals(Object object) {
		return object == this;
	}
    /**
     * returns An array in the Item under the property as a typeOfT array.
     * If property not found null is returned
     * <strong>*Only retrieve types like Integer, Double,Boolean,String, and Long*</strong>
     * @param property A key in the Item
     * @param typeOfT The type of Array you want returned
     * @return Object[] An array that was stored under Property 
     */
	public Object[] getArray(String property, Type typeOfT) {
		if(this.hasProperty(property)) {
			String arrayToDecode = this.getString(property);
			return new Gson().fromJson(arrayToDecode, typeOfT);  
		}
		return null;
	}

	/**
	 * returns a boolean in the Item under the property given.
	 * If property is not found it will return false;
	 * @param property key to look for value
	 * @return value the value of the property
	 */
	public boolean getBoolean(String property){
		if(this.hasProperty(property)) {
			return this.json.get(property).getAsBoolean();
		}
		return false;
	}
	
	/**
	 * returns a double in the Item under the property given.
	 * If property is not found it will return 0.0;
	 * @param property key to look for value
	 * @return value the value of the property
	 */
	public double getDouble(String property) {
		if(this.hasProperty(property)) {
			return this.json.get(property).getAsDouble();
		}
		return 0.0;
	}

	/**
	 * returns the ItemId for the Item
	 * If ItemId is not set, it will return null;
	 * @return itemId the Id of the Item
	 */
	public String getId() {
		if(this.hasProperty("itemId")){
			return this.getString("itemId");
		}
		return null;
	}

	/**
	 * returns an int in the Item under the property given.
	 * If property is not found it will return 0;
	 * @param property key to look for value
	 * @return value the value of the property
	 */
	public int getInt(String property) {
		if(this.hasProperty(property)) {
			return this.json.get(property).getAsInt();
		}
		return 0;
	}

	/**
	 * returns a long in the Item under the property given.
	 * If property is not found it will return 0;
	 * @param property key to look for value
	 * @return value the value of the property
	 */
	public long getLong(String property) {
		if(this.hasProperty(property)) {
			return this.json.get(property).getAsLong();
		}
		return 0;
	}

	/**
	 * returns a String in the Item under the property given.
	 * If property is not found it will return null;
	 * @param property key to look for value
	 * @return value the value of the property
	 */
	public String getString(String property) {
		if(this.hasProperty(property)) {
			return this.json.get(property).getAsString();
		}
		return null;
	}

	/**
	 * returns a boolean that is true if the property is set in the Item, false otherwise
	 * @param property key to look for value
	 * @return value true if property is set, false otherwise
	 */
	public boolean hasProperty(String property) {
		return this.json.get(property) != null;
	}

	/**
	 * Retrieves and loads the value of the Item specified by the ItemId given from the Cloud Synchronously
	 * <strong>Use only if you plan to do your own threading</strong>
	 * @param itemId the Id of the Item to retrieve from the ClearBlade Cloud
	 * @throws ClearBladeException will be thrown if no Item is found or if the API call failed
	 */
	
	public void load (String itemId, final DataCallback callback ) throws ClearBladeException {
		// create the query string to look for the object
		loadSetup(itemId);
//		PlatformResponse<String> result = request.execute();
//		if(result.getError()) {
//			throw new ClearBladeException("Call to Load failed:"+result.getData());
//		} else {
//			this.json = convertJsonArrayToJsonObject(result.getData());
//		}
		DataTask asyncFetch = new DataTask(new PlatformCallback(this, callback){
			@Override
			public void done(String response) {
				_item.json = convertJsonToJsonObject(response);
				Item[] ret = {_item}; 
				callback.done(ret);
			}

			@Override
			public void error(ClearBladeException exception) {
				callback.error(exception);
			}
			
		});
		asyncFetch.execute(request);
	}
	
	public void loadSync(String itemId) throws ClearBladeException{
		loadSetup(itemId);
		PlatformResponse<String> result = request.execute();
		if(result.getError()) {
			throw new ClearBladeException("Call to Load failed:"+result.getData());
		} else {
			this.json = convertJsonArrayToJsonObject(result.getData());
		}
	}
	
	private void loadSetup(String itemId){
		JsonObject queryString = new JsonObject();
		queryString.addProperty("itemId", itemId);
		RequestProperties headers = new RequestProperties.Builder().method("GET").endPoint("api/" + collectionId).qs(queryString).build();
		request.setHeaders(headers);
		
	}

	/**
	 * If Item has not been saved to the Cloud, it will save it, otherwise it
	 * will modify the Item in the cloud.
	 * It loads the value of the Item from the Cloud with the most up-to-date
	 * Item. After creation of an Item it will have a ItemId.
	 * This function operates Synchronously.
	 * <strong>Use only if you plan to do your own threading</strong>
	 * @throws ClearBladeException will be thrown if no Item is found or if the API call failed
	 */
	public void save(final DataCallback callback)  {
		
		saveSetup();
		
		DataTask asyncFetch = new DataTask(new PlatformCallback(this, callback){

			@Override
			public void done(String response) {
				_item.json = convertJsonToJsonObject(response);
				Item[] ret = {_item}; 
				callback.done(ret);
			}

			@Override
			public void error(ClearBladeException exception) {
				callback.error(exception);
			}
			
		});
		asyncFetch.execute(request);
        
		clearChanges();
	}
	
	public Item[] saveSync() throws ClearBladeException{
		saveSetup();
		PlatformResponse<String> result = request.execute();
		if(result.getError()) {
			throw new ClearBladeException("Call to Load failed:"+result.getData());
		} else {
			this.json = convertJsonArrayToJsonObject(result.getData());
		}
		Item[] ret = {this};
		return ret; 
	}
	
	private void saveSetup(){
		RequestProperties headers = null;
		if(this.getString("itemId") == null ) {
			headers = new RequestProperties.Builder().method("POST").endPoint("api/" + collectionId).body(this.json).build();
		} else {
			// Create Payload object
			JsonObject payload = new JsonObject();
			payload.addProperty("$set", this.changes.toString());
			JsonObject query = new JsonObject();
			query.addProperty("itemId", this.getString("itemId"));
			payload.addProperty("query", query.toString());
			headers = new RequestProperties.Builder().method("PUT").endPoint("api/" + collectionId).body(payload).build();
		}

		request.setHeaders(headers);
	}

	/**
	 * Sets the given boolean as the value for the given Property
	 * @param property name to store value under
	 * @param value The value to store
	 */
	public void set(String property, boolean value){
		if(this.hasProperty(property)) {
			this.changes.addProperty(property, value);
		}
		this.json.addProperty(property, value);
	}

	/**
	 * Sets the given double as the value for the given Property
	 * @param property name to store value under
	 * @param value The value to store
	 */
	public void set(String property, double value) {
		if(this.hasProperty(property)) {
			this.changes.addProperty(property, value);
		}
		this.json.addProperty(property, value);
	}

	/**
	 * Sets the given int as the value for the given Property
	 * @param property name to store value under
	 * @param value The value to store
	 */
	public void set(String property, int value){
		if(this.hasProperty(property)) {
			this.changes.addProperty(property, value);
		}
		this.json.addProperty(property, value);
	}


	/**
	 * Sets the given long as the value for the given Property
	 * @param property name to store value under
	 * @param value The value to store
	 */
	public void set(String property, long value) {
		if(this.hasProperty(property)) {
			this.changes.addProperty(property, value);
		}
		this.json.addProperty(property, value);
	}

	/**
	 * Sets the given Object Array as the value for the given Property
	 * <strong>*Only Store types like Integer, Double,Boolean,String, and Long*</strong>
	 * @param property name to store value under
	 * @param value The value to store
	 */
	public void set(String property, Object[] value) {
		// Write the array as json
		String arrayVal = new Gson().toJson(value);

		if(this.hasProperty(property)) {
			this.changes.addProperty(property, arrayVal);
		}

		// store it
		this.json.addProperty(property, arrayVal);
	}

	/**
	 * Sets the given String as the value for the given Property
	 * @param property name to store value under
	 * @param value The value to store
	 */
	public void set(String property, String value) {
		if(this.hasProperty(property)) {
			this.changes.addProperty(property, value);
		}
		this.json.addProperty(property, value);
	}
	/**
	 * returns the Item as a JSON string
	 */
	@Override
	public String toString() {
		return this.json.toString();
	}

	/**
	 * Removes the given property and it's value from the Item
	 * 
	 * @param property name to store value under
	 */
	public void unset(String property) {
		if(this.changes.has(property)){
			this.changes.remove(property);
		}
		this.json.remove(property);
	}
}
