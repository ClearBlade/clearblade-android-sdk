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
	
	//possible values are "prod", "rtp" (ie. develop), or "staging"
	private static String test_against = "staging";
			
	private static String systemKey, systemSecret, testCollectionID, testItemID;

	//prod system info
	private static String prodSK = "c2c895af0af087bea2e1f2a4fb0b";
	private static String prodSS = "C2C895AF0A98FAE0CEF2A4AF890B";
	
	private static String prodTestCollID = "d6ca95af0ad8c7cabfedfcc895b001";
	
	private static String prodTestItemID = "2b58c44f-9fdc-11e3-ad92-bc764e0487f9";
	
	//rtp system info
	private static String rtpSK = "e6cf96b40ab4868aeba0e48e83b601";
	private static String rtpSS = "E6CF96B40AB68BA7C39A91FAB95D";
	
	private static String rtpTestCollID = "b8c7b6b40a80a28ce6af8deeeeb101";
	
	private static String rtpTestItemID = "e549fdb2-c0d3-11e3-9b7f-f0def1a7ff39";
	
	//staging system info
	private static String stagingSK = "c0a8c2ba0aac91f3d0a79fb9c5ea01";
	private static String stagingSS = "C0A8C2BA0AA8A38ABEDABFB0E365";
	
	private static String stagingTestCollID = "f0aac2ba0aecb8dde0c1e9d2f3a701";
	
	private static String stagingTestItemID = "ea63f8ba-faec-11e3-ac9a-22000b298817";

	private void initClearBladeSDK() throws Throwable{
		
		//make sure auth token isn't lingering from other tests
		if(ClearBlade.getCurrentUser() != null){
			ClearBlade.getCurrentUser().setAuthToken(null);
		}		
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		HashMap<String,Object> initOptions = new HashMap<String,Object>();
		
		initOptions.put("email", "android@test.com");
		initOptions.put("password", "android_test");
		
		if(test_against == "prod"){
			systemKey = prodSK;
			systemSecret = prodSS;
			testCollectionID = prodTestCollID;
			testItemID = prodTestItemID;
		}else if(test_against == "rtp"){
			systemKey = rtpSK;
			systemSecret = rtpSS;
			testCollectionID = rtpTestCollID;
			testItemID = rtpTestItemID;
			initOptions.put("platformURL", "https://rtp.clearblade.com");
			initOptions.put("allowUntrusted", true);
		}else if(test_against == "staging"){
			systemKey = stagingSK;
			systemSecret = stagingSS;
			testCollectionID = stagingTestCollID;
			testItemID = stagingTestItemID;
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
