package com.clearblade.platform.test;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.clearblade.platform.api.ClearBlade;
import com.clearblade.platform.api.ClearBladeException;
import com.clearblade.platform.api.Collection;
import com.clearblade.platform.api.DataCallback;
import com.clearblade.platform.api.InitCallback;
import com.clearblade.platform.api.QueryResponse;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;
import junit.framework.TestCase;

public class CollectionTestCase extends AndroidTestCase {
	
	//prod
	//private static String systemKey = "c2c895af0af087bea2e1f2a4fb0b";
	//private static String systemSecret = "C2C895AF0A98FAE0CEF2A4AF890B";
	
	//rtp
	private static String systemKey = "e6cf96b40ab4868aeba0e48e83b601";
	private static String systemSecret = "E6CF96B40AB68BA7C39A91FAB95D";
	
	private static String collectionID = "c8d796b40adcbefc9ecfb2ebaf05";

	private void initClearBladeSDK() throws Throwable{
		
		//make sure auth token isn't lingering from other tests
		if(ClearBlade.getCurrentUser() != null){
			ClearBlade.getCurrentUser().setAuthToken(null);
		}		
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		HashMap<String,Object> initOptions = new HashMap<String,Object>();
		
		initOptions.put("email", "android@test.com");
		initOptions.put("password", "android_test");
		//rtp
		initOptions.put("platformURL", "https://rtp.clearblade.com");
		initOptions.put("allowUntrusted", true);
		
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
	
	public void testFetchAll() throws Throwable{
		
		initClearBladeSDK();
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		Collection collection = new Collection(collectionID);
		collection.fetchAll(new DataCallback(){
			@Override
			public void done(QueryResponse response){
				assertEquals(6, response.getDataItems().length);
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

}
