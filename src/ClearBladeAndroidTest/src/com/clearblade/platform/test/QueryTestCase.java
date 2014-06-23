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
import com.clearblade.platform.api.QueryResponse;
import com.google.gson.Gson;

import android.test.AndroidTestCase;
import junit.framework.TestCase;

public class QueryTestCase extends AndroidTestCase {
	
	//possible values are "prod", "rtp" (ie. develop), or "staging"
	private static String test_against = "staging";
	
	private static String systemKey, systemSecret, queryCollectionID;
	
	//prod system info
	private static String prodSK = "c2c895af0af087bea2e1f2a4fb0b";
	private static String prodSS = "C2C895AF0A98FAE0CEF2A4AF890B";
	
	private static String prodQueryCollID = "f8a0f1b00a90c9969eadd695d21d";

	//rtp system info
	private static String rtpSK = "e6cf96b40ab4868aeba0e48e83b601";
	private static String rtpSS = "E6CF96B40AB68BA7C39A91FAB95D";
	
	private static String rtpQueryCollID = "c8d796b40adcbefc9ecfb2ebaf05";
	
	//staging system info
	private static String stagingSK = "c0a8c2ba0aac91f3d0a79fb9c5ea01";
	private static String stagingSS = "C0A8C2BA0AA8A38ABEDABFB0E365";
	
	private static String stagingQueryCollID = "f4a9c2ba0af889bde6cfaf868a10";
	
	
	private void initClearBladeSDK() throws Throwable{
		
		//make sure auth token isn't lingering from other tests
		if(ClearBlade.getCurrentUser() != null){
			ClearBlade.getCurrentUser().setAuthToken(null);
		}		
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		HashMap<String,Object> initOptions = new HashMap<String,Object>();
		
		initOptions.put("email", "android@test.com");
		initOptions.put("password", "android_test");
		initOptions.put("setLogging",true);
		
		//set needed variables based on system testing against
		if(test_against == "prod"){
			systemKey = prodSK;
			systemSecret = prodSS;
			queryCollectionID = prodQueryCollID;
		}else if(test_against == "rtp"){
			systemKey = rtpSK;
			systemSecret = rtpSS;
			queryCollectionID = rtpQueryCollID;
			initOptions.put("platformURL", "https://rtp.clearblade.com");
			initOptions.put("allowUntrusted", true);
		}else if(test_against == "staging"){
			systemKey = stagingSK;
			systemSecret = stagingSS;
			queryCollectionID = stagingQueryCollID;
			initOptions.put("platformURL", "https://staging.clearblade.com");
			initOptions.put("allowUntrusted", true);
		}else{
			fail("An invalid test_against value was provided. The values accepted are prod, rtp, or staging");
		}
		
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
	
	public void testQueryPagination() throws Throwable{
		initClearBladeSDK();
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		Query testQuery = new Query(queryCollectionID);
		testQuery.equalTo("firstname", "Michael");
		Query temp = new Query(queryCollectionID);
		temp.equalTo("firstname", "Alex");
		testQuery.or(temp);
		testQuery.setPageSize(1);
		testQuery.fetch(new DataCallback(){
			@Override
			public void done(QueryResponse resp) {
				assertEquals(1, resp.getDataItems().length);
				assertNotNull(resp.getNextPageURL());
				assertNull(resp.getPrevPageURL());
				assertEquals(1, resp.getCurrentPage());
				assertEquals(2, resp.getTotalCount());
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
	
	public void testQueryReturnsNothing() throws Throwable{
		
		initClearBladeSDK();
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		Query testQuery = new Query(queryCollectionID);
		testQuery.equalTo("firstname", "NOTHING AT ALL");
		testQuery.fetch(new DataCallback(){
			@Override
			public void done(QueryResponse resp) {
				assertEquals(0, resp.getDataItems().length);
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
	
	public void testQueryEqualToString() throws Throwable{
		
		initClearBladeSDK();
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		Query testQuery = new Query(queryCollectionID);
		testQuery.equalTo("firstname", "Michael");
		testQuery.fetch(new DataCallback(){
			@Override
			public void done(QueryResponse resp) {
				assertEquals(1, resp.getDataItems().length);
				assertEquals("Michael", resp.getDataItems()[0].getString("firstname"));
				assertEquals(22, resp.getDataItems()[0].getInt("age"));
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
		testQuery.equalTo("age", 22);
		testQuery.fetch(new DataCallback(){
			public void done(QueryResponse resp) {
				assertEquals(1, resp.getDataItems().length);
				assertEquals("Michael", resp.getDataItems()[0].getString("firstname"));
				assertEquals(22, resp.getDataItems()[0].getInt("age"));
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
	
	//NEQ not to be trusted currently, temp removed until we get off cassandra
	/*
	public void testQueryNotEqualToString() throws Throwable{
		
		initClearBladeSDK();
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		Query testQuery = new Query(queryCollectionID);
		testQuery.notEqual("firstname", "Michael");
		testQuery.fetch(new DataCallback(){
			@Override
			public void done(QueryResponse resp){
				assertEquals(5, resp.getDataItems().length);
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
			public void done(QueryResponse resp){
				assertEquals(5, resp.getDataItems().length);
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
	*/
	public void testQueryGreaterThanString() throws Throwable{
		
		initClearBladeSDK();
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		Query testQuery = new Query(queryCollectionID);
		testQuery.greaterThan("firstname", "Michael");
		testQuery.fetch(new DataCallback(){
			@Override
			public void done(QueryResponse resp){
				assertEquals(1, resp.getDataItems().length);
				assertEquals("Trevor", resp.getDataItems()[0].getString("firstname"));
				assertEquals(32, resp.getDataItems()[0].getInt("age"));
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
			public void done(QueryResponse resp){
				assertEquals(1, resp.getDataItems().length);
				assertEquals("Eric", resp.getDataItems()[0].getString("firstname"));
				assertEquals(45, resp.getDataItems()[0].getInt("age"));
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
			public void done(QueryResponse resp){
				assertEquals(1, resp.getDataItems().length);
				assertEquals("Trevor", resp.getDataItems()[0].getString("firstname"));
				assertEquals(32, resp.getDataItems()[0].getInt("age"));
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
			public void done(QueryResponse resp){
				assertEquals(1, resp.getDataItems().length);
				assertEquals("Eric", resp.getDataItems()[0].getString("firstname"));
				assertEquals(45, resp.getDataItems()[0].getInt("age"));
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
			public void done(QueryResponse resp){
				assertEquals(1, resp.getDataItems().length);
				assertEquals("Aaron", resp.getDataItems()[0].getString("firstname"));
				assertEquals(35, resp.getDataItems()[0].getInt("age"));
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
			public void done(QueryResponse resp){
				assertEquals(1, resp.getDataItems().length);
				assertEquals("Brian", resp.getDataItems()[0].getString("firstname"));
				assertEquals(18, resp.getDataItems()[0].getInt("age"));
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
			public void done(QueryResponse resp){
				assertEquals(1, resp.getDataItems().length);
				assertEquals("Aaron", resp.getDataItems()[0].getString("firstname"));
				assertEquals(35, resp.getDataItems()[0].getInt("age"));
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
			public void done(QueryResponse resp){
				assertEquals(1, resp.getDataItems().length);
				assertEquals("Brian", resp.getDataItems()[0].getString("firstname"));
				assertEquals(18, resp.getDataItems()[0].getInt("age"));
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
