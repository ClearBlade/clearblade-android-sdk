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
	
	public void testQueryEqualTo() throws Throwable{
		
		initClearBladeSDK();
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		Query testQuery = new Query(queryCollectionID);
		testQuery.equalTo("firstname", "Michael");
		// TODO: uncomment below line once defect 20216 is resolved
		//testQuery.equalTo("age", "22");
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
	
	public void testQueryNotEqualTo() throws Throwable{
		
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
	
	public void testQueryGreaterThan() throws Throwable{
		
		// TODO: Change greater than field check to int field once defect 20216 is resolved
		
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
	
	public void testQueryGreaterThanEqualTo() throws Throwable{
		
		// TODO: Change greater than equal to field check to int field once defect 20216 is resolved
		
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
	
	public void testQueryLessThan() throws Throwable{
		
		// TODO: Change less than field check to int field once defect 20216 is resolved
		
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
	
	public void testQueryLessThanEqualTo() throws Throwable{
		
		// TODO: Change less than equal to field check to int field once defect 20216 is resolved
		
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
	
	public void testQueryUpdate() throws Throwable{
		// TODO: uncomment full test once defect 20216 is resolved
		/*
		initClearBladeSDK();
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		Random rand = new Random();
		final Integer randomNum = rand.nextInt(100);
		
		Query testQuery = new Query(queryCollectionID);
		testQuery.equalTo("firstname", "Eric");
		testQuery.addChange("age", randomNum.toString());
		testQuery.update( new DataCallback() {
			@Override
			public void done(Item[] items){
				assertEquals(1, items.length);
				assertEquals("Eric", items[0].getString("firstname"));
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
		 */
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
