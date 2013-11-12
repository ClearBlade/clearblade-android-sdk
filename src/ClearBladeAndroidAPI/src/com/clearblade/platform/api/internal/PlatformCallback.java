package com.clearblade.platform.api.internal;

import com.clearblade.platform.api.ClearBladeException;
import com.clearblade.platform.api.Collection;
import com.clearblade.platform.api.Item;
import com.clearblade.platform.api.DataCallback;
import com.clearblade.platform.api.Query;

public abstract class PlatformCallback {
	public Collection _collection;
	public DataCallback _callback;
	public Item _item;
	public Query _query;
	
	public PlatformCallback(Query query, DataCallback callback){
		_query = query;
		_callback = callback;
	}
	
	public PlatformCallback(Collection collection, DataCallback callback){
		_collection = collection;
		_callback = callback;
	}
	
	public PlatformCallback(Item item, DataCallback callback){
		_item = item;
		_callback = callback;
	}
	
	public abstract void done(String response);
	public void error(ClearBladeException exception){
		
	}	
}
