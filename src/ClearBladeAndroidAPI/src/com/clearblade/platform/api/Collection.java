package com.clearblade.platform.api;

import java.util.Iterator;


/**
 * This class consists exclusively of instance methods that operate on ClearBlade Collections.
 * <p>
 * ClearBlade Collections are objects that can query Cloud backend collections for Items.
 * A typical example would be:
 * <pre> 
 * 		Collection collection = new Collection(collectionId);
 *		collection.fetchAll(new DataCallback(){
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
public class Collection implements Iterable<Item>{

	private class ArrayIterator implements Iterator<Item> {
		// since itemArray.length is N, we decrement it immediately to N - 1 in next()
		private int i = itemArray.length; 
	
		public boolean hasNext() { return i > 0;}
		public Item next() { return itemArray[--i];} 
		public void remove() {
			throw new UnsupportedOperationException("remove is not supported.");
		}
	}
	private final String TAG = "CLEARBLADECOLLECTION";
	private String collectionId;			// Type of collection
	private Query query;			// string to filter data by
	private Item[] itemArray;		// array that stores all Items

	//private RequestEngine request;	// used to make API requests

	/**
	 * Constructs a new ClearBladeCollection of the specified type
	 * 
	 * @param id the Id of the Collection
	 */
	public Collection(String id) {
		this.collectionId = id;
		this.query = null;
		//this.request = new RequestEngine();
		this.itemArray = null;
	}
	
	/** 
	 * Deletes all Items that are saved in the collection in the Cloud synchronously.
	 * <p>Deleted Items will be stored locally in the Collection.</p>
	 * <strong>*Overrides previously stored Items*</strong>
	 * <strong>*Runs in its own asynchronous task*</strong>
	 * @throws ClearBladeException will be returned in the callback error function
	 */
	public void clear(DataCallback callback) {
		Query query = new Query(collectionId);
		query.remove(callback);
	}

	/**
	 * Returns all items in the collection as item[].
	 * @private
	 * @param json A JSON Array in string format
	 * @return Item[] An array of Items
	 * @throws ClearBladeException will be thrown if Collection was Empty!
	 */
	public Item[] getItems(){
		return itemArray;
	}
//	private Item[] convertJsonArrayToItemArray(String json) {
//		// Parse the JSON string in to a JsonElement
//		JsonElement jsonArrayString = new JsonParser().parse(json);
//		// Store the JsonElement as a JsonArray
//		JsonArray array = jsonArrayString.getAsJsonArray();
//		// If array size is 0, the Collection was empty; Throw Error
//		if(array.size() == 0) {
//		//	throw new ClearBladeException("Collection was Empty!");
//		}
//		
//		// Create Item Array and initialize its values
//		Item[] items = new Item[array.size()];
//		
//		for(int i = 0, len = array.size(); i < len; i++){
//			items[i] = new Item(array.get(i).getAsJsonObject().toString(), this.id);
//		}
//		
//		return items;
//	}

	
	/** 
	 * Gets all Items that match Query criteria from the platform in the Cloud.
	 * <p>Retrieved Items will be stored locally in the Collection.</p>
	 * <strong>*Overrides previously stored Items*</strong>
	 * <strong>*Runs in its own asynchronous task*</strong>
	 * @throws ClearBladeException will be returned in callback.error() if the collection was empty
	 */
	public void fetch(Query query, final DataCallback callback) {
	
		query.setCollectionId(collectionId);
		query.fetch(new DataCallback(){

			@Override
			public void done(Item[] response) {
				itemArray = response;
				callback.done(response);
			}

			@Override
			public void error(ClearBladeException exception) {
				callback.error(exception);
			}
			
		});
	}
	
	/** 
	 * Gets all Items that match Query criteria from the platform in the Cloud.
	 * <p>Retrieved Items will be stored locally in the Collection.</p>
	 * <strong>*Overrides previously stored Items*</strong>
	 * <strong>*Runs in its own asynchronous task*</strong>
	 * @throws ClearBladeException will be returned in callback.error() if the collection was empty
	 */
	public void fetch(final DataCallback callback) {
		fetch(query, callback);
	}
	
	/** 
	 * Gets all Items that are saved in the collection in the Cloud.
	 * <p>Retrieved Items will be stored locally in the Collection.</p>
	 * <strong>*Overrides previously stored Items*</strong>
	 * <strong>*Runs in its own asynchronous task*</strong>
	 * @throws ClearBladeException will be returned in callback.error() if the collection was empty
	 */
	public void fetchAll(final DataCallback callback) {
		Query query = new Query(collectionId);
		query.fetch(new DataCallback(){

			@Override
			public void done(Item[] response) {
				itemArray = response;
				callback.done(response);
			}

			@Override
			public void error(ClearBladeException exception) {
				callback.error(exception);
			}
			
		});
	}
	
	public Item[] fetchAllSync() throws ClearBladeException{
		Query query = new Query(collectionId);
	
		return query.fetchSync();
	}

	/**
	 * Returns the query to be performed during a call to ClearBladeCollection.fetch(). 
	 * will be null if not set by setQuery().
	 * @return query The conditional query to perform on a Cloud Collection
	 */
	public Query getQuery() {
		return this.query;
	}

	/**
	 * Returns an Item Iterator for the ClearBladeC ollection.
	 * <p>Will point at the first Item in the collection that is retrieved from a ClearBladeCollection.fetch() or ClearBladeCollection.clear(). </p>
	 * @throws RuntimeException will be thrown if a call to ClearBladeCollection.fetch() or ClearBladeCollection.clear() has not been made 
	 */
	@Override
	public Iterator<Item> iterator() {
		// If itemArray is null; Throw a ClearBladeException
		if(itemArray == null){
			throw new RuntimeException("You can not Iterate over an Empty Collection");
		}
		return new ArrayIterator();

	}

	/**
	 * Sets the query to be performed during a call to ClearBladeCollection.get()
	 * @param query A conditional String that will determine what Items to retrieve from the Cloud Database
	 */
	public void setQuery(Query query) {
		this.query = query;
	}
	

	/**
	 * Helper method for debugging collection contents
	 */
	public String toString() {
		String ret = "";
		Iterator<Item> iter = iterator();
		
		while (iter.hasNext()){
			Item temp = iter.next();
			ret = ret + temp.toString();
		}
		return ret;
	}
	
}
