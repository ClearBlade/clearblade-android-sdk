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

import junit.framework.TestCase;
import android.test.AndroidTestCase;


public class UsersTestCase extends AndroidTestCase {
	
	//prod
	//private static String systemKeyWithAuth = "c2c895af0af087bea2e1f2a4fb0b";
	//private static String systemSecretWithAuth = "C2C895AF0A98FAE0CEF2A4AF890B";
	
	private static String systemKeyWithoutAuth = "e4eab0b10ac0ff85adec80e67c";
	private static String systemSecretWithoutAuth = "E4EAB0B10AFCA4E6F4BCCEDFF0CB01";
	
	//rtp
	private static String systemKeyWithAuth = "e6cf96b40ab4868aeba0e48e83b601";
	private static String systemSecretWithAuth = "E6CF96B40AB68BA7C39A91FAB95D";
	
	
	public void testExistingUserAuthWithAuthRequiredTrue() throws Throwable{
		
		if(ClearBlade.getCurrentUser() != null){
			ClearBlade.getCurrentUser().setAuthToken(null);
		}
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		HashMap<String,Object> initOptions = new HashMap<String,Object>();
		
		initOptions.put("email", "android@test.com");
		initOptions.put("password", "android_test");
		
		initOptions.put("platformURL", "https://rtp.clearblade.com");
		initOptions.put("allowUntrusted", true);
		
		ClearBlade.initialize(systemKeyWithAuth, systemSecretWithAuth, initOptions, new InitCallback(){

			@Override
			public void done(boolean results) {
				assertNotNull(ClearBlade.getCurrentUser().getAuthToken());
				//now check that the token is correct
				ClearBlade.getCurrentUser().checkUserAuth(new InitCallback(){
					@Override
					public void done(boolean results){
						assertEquals(true, results);
						signal.countDown();
					}
					@Override
					public void error(ClearBladeException e){
						fail("Invalid auth check request: " + e.getMessage());
						signal.countDown();
					}
				});
			}
			@Override
			public void error(ClearBladeException e){
				fail(e.getMessage());
				signal.countDown();
			}
			
		});
		
		signal.await();

	}
	
	public void testNewUserRegWithAuthRequiredTrue() throws Throwable{
		
		//make sure auth token isn't lingering from other init tests
		if(ClearBlade.getCurrentUser() != null){
			ClearBlade.getCurrentUser().setAuthToken(null);
		}		
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		HashMap<String,Object> initOptions = new HashMap<String,Object>();
		
		Random rand = new Random();
		Integer randomNum = rand.nextInt(1000000);
		
		String email = "test_" + randomNum.toString() + "@test.com";
		
		initOptions.put("email", email);
		initOptions.put("password", "android_test");
		initOptions.put("registerUser", true);
		
		initOptions.put("platformURL", "https://rtp.clearblade.com");
		initOptions.put("allowUntrusted", true);
		
		ClearBlade.initialize(systemKeyWithAuth, systemSecretWithAuth, initOptions, new InitCallback(){

			@Override
			public void done(boolean results) {
				assertNotNull(ClearBlade.getCurrentUser().getAuthToken());
				//now check that the token is correct
				ClearBlade.getCurrentUser().checkUserAuth(new InitCallback(){
					@Override
					public void done(boolean results){
						assertEquals(true, results);
						signal.countDown();
					}
					@Override
					public void error(ClearBladeException e){
						fail("Invalid auth check request: " + e.getMessage());
						signal.countDown();
					}
				});
			}
			@Override
			public void error(ClearBladeException e){
				fail(e.getMessage());
				signal.countDown();
			}
			
		});
		
		signal.await();
	}
	
	public void testUnregUserAuthWithAuthRequiredTrue() throws Throwable{
		
		//make sure auth token isn't lingering from other init tests
		if(ClearBlade.getCurrentUser() != null){
			ClearBlade.getCurrentUser().setAuthToken(null);
		}
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		HashMap<String,Object> initOptions = new HashMap<String,Object>();
		
		Random rand = new Random();
		Integer randomNum = rand.nextInt(100000);
		
		String email = "unreg_user_" + randomNum.toString() + "@test.com";
		
		System.out.println();
		
		initOptions.put("email", email);
		initOptions.put("password", "android_test");
		initOptions.put("registerUser", false);
		
		initOptions.put("platformURL", "https://rtp.clearblade.com");
		initOptions.put("allowUntrusted", true);
		
		ClearBlade.initialize(systemKeyWithAuth, systemSecretWithAuth, initOptions, new InitCallback(){

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
	
	public void testRegAlreadyExistingUserWithAuthRequiredTrue() throws Throwable{
		
		if(ClearBlade.getCurrentUser() != null){
			ClearBlade.getCurrentUser().setAuthToken(null);
		}
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		HashMap<String,Object> initOptions = new HashMap<String,Object>();
		
		initOptions.put("email", "android@test.com");
		initOptions.put("password", "android_test");
		initOptions.put("registerUser", true);
		
		ClearBlade.initialize(systemKeyWithAuth, systemSecretWithAuth, initOptions, new InitCallback(){

			@Override
			public void done(boolean results) {
				fail("Able to register an already existing user");
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
	
	public void testUserAuthBadPasswordWithAuthRequiredTrue() throws Throwable{
		
		if(ClearBlade.getCurrentUser() != null){
			ClearBlade.getCurrentUser().setAuthToken(null);
		}
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		HashMap<String,Object> initOptions = new HashMap<String,Object>();
		
		initOptions.put("email", "android@test.com");
		initOptions.put("password", "bad_password");
		
		ClearBlade.initialize(systemKeyWithAuth, systemSecretWithAuth, initOptions, new InitCallback(){

			@Override
			public void done(boolean results) {
				fail("Able to authenticate a user with invalid password");
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
	
	//all functions below are temp removed until backend denies user auth requests 
	// when auth required is false
	/*	
	public void testExistingUserAuthWithAuthRequiredFalse() throws Throwable{
		
		//make sure auth token isn't lingering from other init tests
		if(ClearBlade.getCurrentUser() != null){
			ClearBlade.getCurrentUser().setAuthToken(null);
		}
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		HashMap<String,Object> initOptions = new HashMap<String,Object>();
		
		initOptions.put("email", "android@test.com");
		initOptions.put("password", "android_test");
		
		ClearBlade.initialize(systemKeyWithoutAuth, systemSecretWithoutAuth, initOptions, new InitCallback(){

			@Override
			public void done(boolean results) {
				fail("Able to authenticate with a registered user with auth required set to false");
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
	 
	public void testUnregUserAuthWithAuthRequiredFalse() throws Throwable{
		
		//make sure auth token isn't lingering from other init tests
		if(ClearBlade.getCurrentUser() != null){
			ClearBlade.getCurrentUser().setAuthToken(null);
		}
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		HashMap<String,Object> initOptions = new HashMap<String,Object>();
		
		initOptions.put("email", "fake_user@test.com");
		initOptions.put("password", "android_test");
		
		ClearBlade.initialize(systemKeyWithoutAuth, systemSecretWithoutAuth, initOptions, new InitCallback(){

			@Override
			public void done(boolean results) {
				fail("Able to authenticate with an unregistered user with auth required set to false");
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
	
	public void testUserAuthBadPasswordWithAuthRequiredFalse() throws Throwable{
		
		//make sure auth token isn't lingering from other init tests
		if(ClearBlade.getCurrentUser() != null){
			ClearBlade.getCurrentUser().setAuthToken(null);
		}
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		HashMap<String,Object> initOptions = new HashMap<String,Object>();
		
		initOptions.put("email", "android@test.com");
		initOptions.put("password", "bad_password");
		
		ClearBlade.initialize(systemKeyWithoutAuth, systemSecretWithoutAuth, initOptions, new InitCallback(){

			@Override
			public void done(boolean results) {
				fail("Able to authenticate with a user with invalid password and auth required set to false");
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
	
	public void testNewUserRegWithAuthRequiredFalse() throws Throwable{
		
		//make sure auth token isn't lingering from other init tests
		if(ClearBlade.getCurrentUser() != null){
			ClearBlade.getCurrentUser().setAuthToken(null);
		}
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		Random rand = new Random();
		Integer randomNum = rand.nextInt(1000000);
		
		String email = "test_" + randomNum.toString() + "@test.com";
		
		HashMap<String,Object> initOptions = new HashMap<String,Object>();
		
		initOptions.put("email", email);
		initOptions.put("password", "android_test");
		initOptions.put("registerUser", true);
		
		ClearBlade.initialize(systemKeyWithoutAuth, systemSecretWithoutAuth, initOptions, new InitCallback(){

			@Override
			public void done(boolean results) {
				fail("Able to register and authenticate with a user with auth required set to false");
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
	*/
}