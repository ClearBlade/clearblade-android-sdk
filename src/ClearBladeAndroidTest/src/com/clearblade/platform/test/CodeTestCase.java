package com.clearblade.platform.test;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import com.clearblade.platform.api.ClearBlade;
import com.clearblade.platform.api.ClearBladeException;
import com.clearblade.platform.api.Code;
import com.clearblade.platform.api.CodeCallback;
import com.clearblade.platform.api.InitCallback;
import com.google.gson.JsonObject;

import android.test.AndroidTestCase;

public class CodeTestCase extends AndroidTestCase {
	
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

	private void initClearBladeSDK() throws Throwable{
		
		//make sure auth token isn't lingering from other tests
		if(ClearBlade.getCurrentUser() != null){
			ClearBlade.getCurrentUser().setAuthToken(null);
		}		
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		HashMap<String,Object> initOptions = new HashMap<String,Object>();
		
		initOptions.put("email", "android@test.com");
		initOptions.put("password", "android_test");
		
		//set needed variables based on system testing against
		if(test_against == "prod"){
			systemKey = prodSK;
			systemSecret = prodSS;
		}else if(test_against == "rtp"){
			systemKey = rtpSK;
			systemSecret = rtpSS;
			initOptions.put("platformURL", "https://rtp.clearblade.com");
			initOptions.put("messagingURL", "tcp://rtp.clearblade.com:1883");
			initOptions.put("allowUntrusted", true);
		}else if(test_against == "staging"){
			systemKey = stagingSK;
			systemSecret = stagingSS;
			initOptions.put("platformURL", "https://staging.clearblade.com");
			initOptions.put("messagingURL", "tcp://ec2-54-82-138-91.compute-1.amazonaws.com:1883");
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
	
	public void testCodeExecute() throws Throwable{
		
		initClearBladeSDK();
		
		final CountDownLatch signal = new CountDownLatch(1);
		final String name = "michael";
		
		JsonObject params = new JsonObject();
		params.addProperty("name",name);
		
		Code code = new Code("test", params);
		
		code.execute(new CodeCallback(){
			
			@Override
			public void done(JsonObject response) {
				assertEquals(name, response.get("results").getAsString());
				signal.countDown();
			}
			
			@Override
			public void error(ClearBladeException e) {
				fail("executing code failed: " + e.getMessage());
				signal.countDown();
			}
		});
		
		signal.await();
		
		/*
		final String testTopic = "android_test";
		final String testMessage = "testing fun!";
		final int qos = 2;
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		Message message = new Message(this.getContext(), qos);
		
		//add a delay to give the message service time to start
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		message.subscribe(testTopic, new MessageCallback(){

			@Override
			public void done(String topic, String messageString) {
				assertEquals(testTopic, topic);
				assertEquals(testMessage, messageString);
				signal.countDown();
			}
			@Override
			public void error(ClearBladeException e){
				fail("subscribe failed: " + e.getMessage());
				signal.countDown();
			}

		});

		message.publish(testTopic, testMessage);
		
		signal.await();
		*/
	}

}
