package com.clearblade.platform.test;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import com.clearblade.platform.api.ClearBlade;
import com.clearblade.platform.api.ClearBladeException;
import com.clearblade.platform.api.DataCallback;
import com.clearblade.platform.api.InitCallback;
import com.clearblade.platform.api.Item;
import com.clearblade.platform.api.Query;

import android.test.AndroidTestCase;
import junit.framework.TestCase;

public class QueryTestCase extends AndroidTestCase {
	
	private static String systemKey = "c2c895af0af087bea2e1f2a4fb0b";
	private static String systemSecret = "C2C895AF0A98FAE0CEF2A4AF890B";
	
	private static String queryCollectionID = "f8a0f1b00a90c9969eadd695d21d";

	private void initClearBladeSDK() throws Throwable{
		
		//make sure auth token isn't lingering from other tests
		if(ClearBlade.getCurrentUser() != null){
			ClearBlade.getCurrentUser().setAuthToken(null);
		}		
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		HashMap<String,Object> initOptions = new HashMap<String,Object>();
		
		initOptions.put("email", "android@test.com");
		initOptions.put("password", "android_test");
		
		ClearBlade.initialize(systemKey, systemSecret, initOptions, new InitCallback(){

			@Override
			public void done(boolean results) {
				signal.countDown();
			}
			@Override
			public void error(ClearBladeException e){
				fail(e.getMessage());
				signal.countDown();
			}
			
		});
		
		signal.await();
		
	}
	
	public void testQueryEqualToString() throws Throwable{
		
		initClearBladeSDK();
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		Query testQuery = new Query(queryCollectionID);
		testQuery.equalTo("firstname", "Michael");
		testQuery.fetch(new DataCallback(){
			@Override
			public void done(Item[] items) {
				assertEquals(1, items.length);
				assertEquals("Michael", items[0].getString("firstname"));
				assertEquals(22, items[0].getInt("age"));
				signal.countDown();
			}
			@Override
			public void error(ClearBladeException e){
				fail("Failed to query item: " + e.getMessage());
				signal.countDown();
			}
		});
		
		signal.await();
	}
	
	public void testQueryEqualToInt() throws Throwable{
		
		initClearBladeSDK();
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		Query testQuery = new Query(queryCollectionID);
		Query temp = new Query(queryCollectionID);
		temp.equalTo("age", 18);
		testQuery.equalTo("age", 22);
		testQuery.or(temp);
		testQuery.fetch(new DataCallback(){
			@Override
			public void done(Item[] items) {
				assertEquals(1, items.length);
				assertEquals("Michael", items[0].getString("firstname"));
				assertEquals(22, items[0].getInt("age"));
				signal.countDown();
			}
			@Override
			public void error(ClearBladeException e){
				fail("Failed to query item: " + e.getMessage());
				signal.countDown();
			}
		});
		
		signal.await();
	}
	
	public void testQueryNotEqualToString() throws Throwable{
		
		initClearBladeSDK();
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		Query testQuery = new Query(queryCollectionID);
		testQuery.notEqual("firstname", "Michael");
		testQuery.fetch(new DataCallback(){
			@Override
			public void done(Item[] items){
				assertEquals(5, items.length);
				signal.countDown();
			}
			@Override
			public void error(ClearBladeException e){
				fail("Failed to query item: " + e.getMessage());
				signal.countDown();
			}
		});
		
		signal.await();
	}
	
	public void testQueryNotEqualToInt() throws Throwable{
		
		initClearBladeSDK();
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		Query testQuery = new Query(queryCollectionID);
		testQuery.notEqual("age", 22);
		testQuery.fetch(new DataCallback(){
			@Override
			public void done(Item[] items){
				assertEquals(5, items.length);
				signal.countDown();
			}
			@Override
			public void error(ClearBladeException e){
				fail("Failed to query item: " + e.getMessage());
				signal.countDown();
			}
		});
		
		signal.await();
	}
	
	public void testQueryGreaterThanString() throws Throwable{
		
		initClearBladeSDK();
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		Query testQuery = new Query(queryCollectionID);
		testQuery.greaterThan("firstname", "Michael");
		testQuery.fetch(new DataCallback(){
			@Override
			public void done(Item[] items){
				assertEquals(1, items.length);
				assertEquals("Trevor", items[0].getString("firstname"));
				assertEquals(32, items[0].getInt("age"));
				signal.countDown();
			}
			@Override
			public void error(ClearBladeException e){
				fail("Failed to query item: " + e.getMessage());
				signal.countDown();
			}
		});
		
		signal.await();
	}
	
	public void testQueryGreaterThanInt() throws Throwable{
		
		initClearBladeSDK();
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		Query testQuery = new Query(queryCollectionID);
		testQuery.greaterThan("age", 40);
		testQuery.fetch(new DataCallback(){
			@Override
			public void done(Item[] items){
				assertEquals(1, items.length);
				assertEquals("Eric", items[0].getString("firstname"));
				assertEquals(45, items[0].getInt("age"));
				signal.countDown();
			}
			@Override
			public void error(ClearBladeException e){
				fail("Failed to query item: " + e.getMessage());
				signal.countDown();
			}
		});
		
		signal.await();
	}
	
	public void testQueryGreaterThanEqualToString() throws Throwable{
		
		initClearBladeSDK();
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		Query testQuery = new Query(queryCollectionID);
		testQuery.greaterThanEqualTo("firstname", "Trevor");
		testQuery.fetch(new DataCallback(){
			@Override
			public void done(Item[] items){
				assertEquals(1, items.length);
				assertEquals("Trevor", items[0].getString("firstname"));
				assertEquals(32, items[0].getInt("age"));
				signal.countDown();
			}
			@Override
			public void error(ClearBladeException e){
				fail("Failed to query item: " + e.getMessage());
				signal.countDown();
			}
		});
		
		signal.await();
	}
	
	public void testQueryGreaterThanEqualToInt() throws Throwable{
		
		initClearBladeSDK();
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		Query testQuery = new Query(queryCollectionID);
		testQuery.greaterThanEqualTo("age", 45);
		testQuery.fetch(new DataCallback(){
			@Override
			public void done(Item[] items){
				assertEquals(1, items.length);
				assertEquals("Eric", items[0].getString("firstname"));
				assertEquals(45, items[0].getInt("age"));
				signal.countDown();
			}
			@Override
			public void error(ClearBladeException e){
				fail("Failed to query item: " + e.getMessage());
				signal.countDown();
			}
		});
		
		signal.await();
	}
	
	public void testQueryLessThanString() throws Throwable{
		
		initClearBladeSDK();
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		Query testQuery = new Query(queryCollectionID);
		testQuery.lessThan("firstname", "Alex");
		testQuery.fetch(new DataCallback(){
			@Override
			public void done(Item[] items){
				assertEquals(1, items.length);
				assertEquals("Aaron", items[0].getString("firstname"));
				assertEquals(35, items[0].getInt("age"));
				signal.countDown();
			}
			@Override
			public void error(ClearBladeException e){
				fail("Failed to query item: " + e.getMessage());
				signal.countDown();
			}
		});
		
		signal.await();
	}
	
	public void testQueryLessThanInt() throws Throwable{
		
		initClearBladeSDK();
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		Query testQuery = new Query(queryCollectionID);
		testQuery.lessThan("age", 20);
		testQuery.fetch(new DataCallback(){
			@Override
			public void done(Item[] items){
				assertEquals(1, items.length);
				assertEquals("Brian", items[0].getString("firstname"));
				assertEquals(18, items[0].getInt("age"));
				signal.countDown();
			}
			@Override
			public void error(ClearBladeException e){
				fail("Failed to query item: " + e.getMessage());
				signal.countDown();
			}
		});
		
		signal.await();
	}
	
	public void testQueryLessThanEqualToString() throws Throwable{
		
		initClearBladeSDK();
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		Query testQuery = new Query(queryCollectionID);
		testQuery.lessThanEqualTo("firstname", "Aaron");
		testQuery.fetch(new DataCallback(){
			@Override
			public void done(Item[] items){
				assertEquals(1, items.length);
				assertEquals("Aaron", items[0].getString("firstname"));
				assertEquals(35, items[0].getInt("age"));
				signal.countDown();
			}
			@Override
			public void error(ClearBladeException e){
				fail("Failed to query item: " + e.getMessage());
				signal.countDown();
			}
		});
		
		signal.await();
		
	}
	
	public void testQueryLessThanEqualToInt() throws Throwable{
		
		initClearBladeSDK();
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		Query testQuery = new Query(queryCollectionID);
		testQuery.lessThanEqualTo("age", 18);
		testQuery.fetch(new DataCallback(){
			@Override
			public void done(Item[] items){
				assertEquals(1, items.length);
				assertEquals("Brian", items[0].getString("firstname"));
				assertEquals(18, items[0].getInt("age"));
				signal.countDown();
			}
			@Override
			public void error(ClearBladeException e){
				fail("Failed to query item: " + e.getMessage());
				signal.countDown();
			}
		});
		
		signal.await();
		
	}
	
	public void testQueryUpdate() throws Throwable{

		initClearBladeSDK();
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		//create random int between 23 and 39 so that other tests aren't affected
		final Integer randomNum = 23 + (int)(Math.random() * ((39 - 23) + 1));
		
		Query testQuery = new Query(queryCollectionID);
		testQuery.equalTo("firstname", "Alex");
		testQuery.addChange("age", randomNum);
		testQuery.update( new DataCallback() {
			@Override
			public void done(Item[] items){
				assertEquals(1, items.length);
				assertEquals("Alex", items[0].getString("firstname"));
				assertEquals((int)randomNum, items[0].getInt("age"));
				signal.countDown();
			}
			@Override
			public void error(ClearBladeException e){
				fail("Failed to query update: " + e.getMessage());
				signal.countDown();
			}
		});
		
		signal.await();
		
	}
	
	public void testQueryRemove() throws Throwable{
		
		initClearBladeSDK();
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		Item tempItem = new Item(queryCollectionID);
		
		tempItem.set("age", 123);
		tempItem.set("firstname", "should be deleted..");
		
		tempItem.save(new DataCallback(){
			@Override
			public void done(Item[] items){
				//item created, now delete it using a query
				Query testQuery = new Query(queryCollectionID);
				testQuery.equalTo("firstname", "should be deleted..");
				testQuery.remove(new DataCallback(){
					@Override
					public void done(Item[] items){
						assertEquals(0, items.length);
						signal.countDown();
					}
					@Override
					public void error(ClearBladeException e){
						fail("Query delete failed: " + e.getMessage());
						signal.countDown();
					}
				});
			}
			@Override
			public void error(ClearBladeException e){
				fail("Unable to create item for query delete test: " + e.getMessage());
				signal.countDown();
			}
		});
		
		signal.await();
	}

}
