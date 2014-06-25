package com.clearblade.platform.test;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import com.clearblade.platform.api.ClearBlade;
import com.clearblade.platform.api.ClearBladeException;
import com.clearblade.platform.api.InitCallback;

import junit.framework.TestCase;
import android.test.AndroidTestCase;


public class InitTestCase extends AndroidTestCase {
	
	//possible values are "prod", "rtp" (ie. develop), or "staging"
	private static String test_against = "prod";
		
	private static String systemKey, systemSecret;
	
	//prod system info
	private static String prodSK = "c2c895af0af087bea2e1f2a4fb0b";
	private static String prodSS = "C2C895AF0A98FAE0CEF2A4AF890B";
	
	//rtp system info
	private static String rtpSK = "e6cf96b40ab4868aeba0e48e83b601";
	private static String rtpSS = "E6CF96B40AB68BA7C39A91FAB95D";
	
	//staging system info
	private static String stagingSK = "c0a8c2ba0aac91f3d0a79fb9c5ea01";
	private static String stagingSS = "C0A8C2BA0AA8A38ABEDABFB0E365";
	
	
	public void testAnonymousInitWithAuthRequired() throws Throwable{
	
		//make sure auth token isn't lingering from other init tests
		if(ClearBlade.getCurrentUser() != null){
			ClearBlade.getCurrentUser().setAuthToken(null);
		}
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		HashMap<String,Object> initOptions = new HashMap<String,Object>();
		
		//set needed variables based on system testing against
		if(test_against == "prod"){
			systemKey = prodSK;
			systemSecret = prodSS;
		}else if(test_against == "rtp"){
			systemKey = rtpSK;
			systemSecret = rtpSS;
			initOptions.put("platformURL", "https://rtp.clearblade.com");
			initOptions.put("allowUntrusted", true);
		}else if(test_against == "staging"){
			systemKey = stagingSK;
			systemSecret = stagingSS;
			initOptions.put("platformURL", "https://staging.clearblade.com");
			initOptions.put("allowUntrusted", true);
		}else{
			fail("An invalid test_against value was provided. The values accepted are prod, rtp, or staging");
		}
		
		ClearBlade.initialize(systemKey, systemSecret, initOptions, new InitCallback(){

			@Override
			public void done(boolean results) {
				assertNotNull(ClearBlade.getCurrentUser().getAuthToken());
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
	
	
	public void testUserInitWithAuthRequired() throws Throwable{
		
		if(ClearBlade.getCurrentUser() != null){
			ClearBlade.getCurrentUser().setAuthToken(null);
		}
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		HashMap<String,Object> initOptions = new HashMap<String,Object>();
		
		//set needed variables based on system testing against
		if(test_against == "prod"){
			systemKey = prodSK;
			systemSecret = prodSS;
		}else if(test_against == "rtp"){
			systemKey = rtpSK;
			systemSecret = rtpSS;
			initOptions.put("platformURL", "https://rtp.clearblade.com");
			initOptions.put("allowUntrusted", true);
		}else if(test_against == "staging"){
			systemKey = stagingSK;
			systemSecret = stagingSS;
			initOptions.put("platformURL", "https://staging.clearblade.com");
			initOptions.put("allowUntrusted", true);
		}else{
			fail("An invalid test_against value was provided. The values accepted are prod, rtp, or staging");
		}
		
		initOptions.put("email", "android@test.com");
		initOptions.put("password", "android_test");
		
		ClearBlade.initialize(systemKey, systemSecret, initOptions, new InitCallback(){

			@Override
			public void done(boolean results) {
				assertNotNull(ClearBlade.getCurrentUser().getAuthToken());
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
	
	public void testRegisterInitWithRegUserTrue() throws Throwable{
		
		//make sure auth token isn't lingering from other init tests
		if(ClearBlade.getCurrentUser() != null){
			ClearBlade.getCurrentUser().setAuthToken(null);
		}		
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		HashMap<String,Object> initOptions = new HashMap<String,Object>();
		
		Random rand = new Random();
		Integer randomNum = rand.nextInt(100000);
		
		String email = "test_" + randomNum.toString() + "@test.com";
		
		//set needed variables based on system testing against
		if(test_against == "prod"){
			systemKey = prodSK;
			systemSecret = prodSS;
		}else if(test_against == "rtp"){
			systemKey = rtpSK;
			systemSecret = rtpSS;
			initOptions.put("platformURL", "https://rtp.clearblade.com");
			initOptions.put("allowUntrusted", true);
		}else if(test_against == "staging"){
			systemKey = stagingSK;
			systemSecret = stagingSS;
			initOptions.put("platformURL", "https://staging.clearblade.com");
			initOptions.put("allowUntrusted", true);
		}else{
			fail("An invalid test_against value was provided. The values accepted are prod, rtp, or staging");
		}		
		
		initOptions.put("email", email);
		initOptions.put("password", "android_test");
		initOptions.put("registerUser", true);
		
		ClearBlade.initialize(systemKey, systemSecret, initOptions, new InitCallback(){

			@Override
			public void done(boolean results) {
				assertNotNull(ClearBlade.getCurrentUser().getAuthToken());
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
	
	public void testInitWithUnknownUser() throws Throwable{
		
		//make sure auth token isn't lingering from other init tests
		if(ClearBlade.getCurrentUser() != null){
			ClearBlade.getCurrentUser().setAuthToken(null);
		}
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		HashMap<String,Object> initOptions = new HashMap<String,Object>();
		
		Random rand = new Random();
		Integer randomNum = rand.nextInt(100000);
		
		String email = "unreg_user_" + randomNum.toString() + "@test.com";
		
		//set needed variables based on system testing against
		if(test_against == "prod"){
			systemKey = prodSK;
			systemSecret = prodSS;
		}else if(test_against == "rtp"){
			systemKey = rtpSK;
			systemSecret = rtpSS;
			initOptions.put("platformURL", "https://rtp.clearblade.com");
			initOptions.put("allowUntrusted", true);
		}else if(test_against == "staging"){
			systemKey = stagingSK;
			systemSecret = stagingSS;
			initOptions.put("platformURL", "https://staging.clearblade.com");
			initOptions.put("allowUntrusted", true);
		}else{
			fail("An invalid test_against value was provided. The values accepted are prod, rtp, or staging");
		}
		
		initOptions.put("email", email);
		initOptions.put("password", "android_test");
		initOptions.put("registerUser", false);
		
		ClearBlade.initialize(systemKey, systemSecret, initOptions, new InitCallback(){

			@Override
			public void done(boolean results) {
				fail("Able to authenticate as an unregistered user");
				signal.countDown();
			}
			@Override
			public void error(ClearBladeException e){
				assertNull(ClearBlade.getCurrentUser().getAuthToken());
				signal.countDown();
			}
			
		});
		
		signal.await();
	}
	
}
