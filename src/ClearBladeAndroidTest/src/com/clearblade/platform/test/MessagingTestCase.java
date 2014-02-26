package com.clearblade.platform.test;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.clearblade.platform.api.ClearBlade;
import com.clearblade.platform.api.ClearBladeException;
import com.clearblade.platform.api.InitCallback;
import com.clearblade.platform.api.Message;
import com.clearblade.platform.api.MessageCallback;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;
import junit.framework.TestCase;

public class MessagingTestCase extends AndroidTestCase {
	
	private static String systemKey = "c2c895af0af087bea2e1f2a4fb0b";
	private static String systemSecret = "C2C895AF0A98FAE0CEF2A4AF890B";

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
	
	public void testSubscribeAndPublish() throws Throwable{
		
		initClearBladeSDK();
		
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
	}

}
