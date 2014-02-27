package com.clearblade.platform.test;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import com.clearblade.platform.api.ClearBlade;
import com.clearblade.platform.api.ClearBladeException;
import com.clearblade.platform.api.DataCallback;
import com.clearblade.platform.api.InitCallback;
import com.clearblade.platform.api.Item;

import android.test.AndroidTestCase;
import junit.framework.TestCase;

public class ItemTestCase extends AndroidTestCase {

	private static String systemKey = "c2c895af0af087bea2e1f2a4fb0b";
	private static String systemSecret = "C2C895AF0A98FAE0CEF2A4AF890B";
	
	private static String testCollectionID = "d6ca95af0ad8c7cabfedfcc895b001";
	
	private static String testItemID = "2b58c44f-9fdc-11e3-ad92-bc764e0487f9";

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
	
	public void testAddAndDeleteItem() throws Throwable{
		
		initClearBladeSDK();
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		Random rand = new Random();
		int randomNum = rand.nextInt(100000);
		
		Item tempItem = new Item(testCollectionID);
		
		tempItem.set("intcolumn", randomNum);
		tempItem.set("stringcolumn", "should be deleted..");
		
		tempItem.save(new DataCallback(){
			@Override
			public void done(Item[] items){
				//item created, now delete it
				items[0].destroy(new DataCallback(){

					@Override
					public void done(Item[] response) {
						assertEquals(0, response.length);
						signal.countDown();
					}
					@Override
					public void error(ClearBladeException e){
						fail("Failed to delete item: " + e.getMessage());
						signal.countDown();
					}
					
				});
			}
			@Override
			public void error(ClearBladeException e){
				fail("Unable to load item from collection: " + e.getMessage());
				signal.countDown();
			}
		});
		
		signal.await();
		
	}
	
	public void testGetItem() throws Throwable{
		
		initClearBladeSDK();
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		Item tempItem = new Item(testCollectionID);
		
		tempItem.load(testItemID, new DataCallback(){
			@Override
			public void done(Item[] item){
				assertEquals(1, item.length);
				assertEquals("test_data", item[0].getString("stringcolumn"));
				assertEquals(123, item[0].getInt("intcolumn"));
				signal.countDown();
			}
			@Override
			public void error(ClearBladeException e){
				fail("Unable to get item from collection: " + e.getMessage());
				signal.countDown();
			}
		});
		
		signal.await();
	}
	
	
}
