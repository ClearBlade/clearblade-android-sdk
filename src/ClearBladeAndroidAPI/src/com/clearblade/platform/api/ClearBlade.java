package com.clearblade.platform.api;

import java.util.HashMap;

import android.util.Log;


/**
 * This class consists exclusively of static methods that manage API configurations, initialization, and API information
 * <p>This class consists of static methods that:
 * <ul>
 *  <li>Manage configurations for the ClearBlade API</li>
 *  <li>Provide SDK and API information</li>
 *  <li>Initialize the ClearBlade Library for use</li>
 * </ul>
 * <strong>*You must call initialize(String systemKey, String systemSecret) or its other variants to initialize the API*</strong>
 * </p>
 *
 * @author  Clyde Byrd III, Aaron Allsbrook, Michael Sprague
 * @since   1.0
 */
public class ClearBlade {
	private static final String TAG = "ClearBlade"; 		
	private final static String apiVersion = "0.0.1";
	private final static String sdkVersion = "0.0.1";
	private static int callTimeOut;							// http Requests will be aborted after this amount of milliseconds
	private static boolean logging;						    // if the user wants internal logs to show. 
	private static String masterSecret;					    // App's Admin Password; has access to Everything
	private static String uri;								// Default URL to send Api requests to
	private static String messageUrl;						// Default URL to connect to the message broker with
	private static User user;								// Current User of the application. Not implemented Yet
	private static boolean initError = false;
	private static boolean allowUntrusted=false;			// if the platform has https enabled but no publically signed certificate
	
	
	
	/**
	 * Returns the version of the API that is currently in use.
	 * 
	 * @return The API version
	 */
	public static String getApiVersion() {
		return apiVersion;
	}

	/**
	 * Returns the Application Authorization String. It is used to verify the Application with the Backend
	 * *This method will be need to be changed; It will only use OAuth, while the appKey and appSecret are now headers.*
	 * @protected 
	 * @return The authentication for the app
	 * @see Base64
	 */
//	protected static String getAuthentication() {
//		String auth = null;
//		try{
//
//			if (masterSecret != null) { 
//				//If master Secret is not null; use it; 0 means defaults (check android.util.Base64 for more details) 
//				auth = Base64.encodeToString(("Basic " + appKey + ":" + masterSecret).getBytes("UTF-8"), 0);
//			} else {
//				auth = Base64.encodeToString(("Basic " + appKey + ":" + appSecret).getBytes("UTF-8"), 0);
//			}
//
//		} catch(UnsupportedEncodingException e) {
//			logger(TAG, "Error encoding Authentication: " + e.getMessage(), true);
//		}
//
//		return auth;
//	}
	
	/** 
	 * Returns the milliseconds that the API will wait for a connection to the backend
	 * until API requests are aborted.
	 * @return milliseconds 
	 */
	public static int getCallTimeOut() {
		return callTimeOut;
	}

	/**
	 * Returns the current user of the Application
	 * @return Current user object
	 */
	public static User getCurrentUser() {
		return user;
	}
	
	/**
	 * Returns the version of the SDK  in use
	 * @return SDK version
	 */
	public static String getSdkVersion() {
		return sdkVersion;
	}

	/**
	 * Returns the uri of the backend that will be used for API calls
	 * @return uri of the Backend
	 */
	public static String getUri() {
		return uri;
	}
	
	/**
	 * Sets uri of the backend platform that will be used for API calls
	 * Typically scenarios are https://platform.clearblade.com
	 */
	public static void setUri(String platformURI){
		uri = platformURI;
	}
	
	/**
	 * Sets the url of the message broker that will be used in messaging applications
	 * Defaults to 'tcp://platform.clearblade.com:1883'
	 * @param messageURL the string that will be set as the url
	 */
	public static void setMessageUrl(String messageURL) {
		messageUrl = messageURL;
	}
	
	/**
	 * Gets the url of the message broker that was set upon initialization
	 * @return URL of message broker
	 */
	public static String getMessageUrl() {
		return messageUrl;
	}

	/**
	 * Allows for passing requests to an untrusted server.  This method
	 * is not recommended for any scenario other than development
	 */
	public static void setAllowUntrusted(boolean allowUntrustedCertificates){
		allowUntrusted = allowUntrustedCertificates;
	}
	
	/**
	 * Allows for passing requests to an untrusted server. 
	 * @return boolean value for using untrusted backend servers
	 */
	public static boolean getAllowUntrusted(){
		return allowUntrusted;
	}
	
	/**
	 * Initializes the API for the given system as an anonymous user. (If the system
	 * has user authentication required set to true, this will fail - See the initialize
	 * method mentioned below)
	 * Must be called prior to any API calls
	 * Throws IllegalArgumentException if myAppKey or myAppSecret is null 
	 * @param myAppKey The key used to identify the Application in use
	 * @param myAppSecret The secret used to verify the Application in use
	 * throws IllegalArgumentException
	 */
	public static void initialize(String systemKey, String systemSecret, InitCallback callback) {

		if(user != null){
			user = null;
		}
		
		if (systemKey == null) {
			throw new IllegalArgumentException("systemKey must be a non-empty Strings");
		}
		if(systemSecret == null) {
			throw new IllegalArgumentException("systemSecret can not be null");
		}

		Util.setSystemKey(systemKey);
		Util.setSystemSecret(systemSecret);
		masterSecret = null;
		uri =  "https://platform.clearblade.com";
		messageUrl = "tcp://messaging.clearblade.com:1883";
		logging = false;
		callTimeOut = 30000;
		
		user = new User(null);
		
		user.authWithAnonUser(callback);
	}
	
	/**
	 * Initializes API with given system credentials and options.
	 * Upon Success/Failure, appropriate callback methods are triggered
	 * Must be called prior to any API calls.
	 * Available init options:
	 * 	email - String to register or log-in as specific user (required if password is given) Default - null<br>
	 * 	password - password String for given user (required if email is given) Default - null<br>
	 * 	platformURL - Custom URL for the platform Default - https://platform.clearblade.com<br>
	 * 	messagingURL - Custom Messaging URL Default - tcp://messaging.clearblade.com:1883<br>
	 * 	registerUser - Boolean to tell if you'd like to attempt registering the given user Default - false<br>
	 * 	logging - Boolean to enable ClearBlade Internal API logging Default - false<br>
	 * 	callTimeout - Int number of milliseconds for call timeouts Default - 30000 (30 seconds)<br>
	 *  allowUntrusted - Boolean to connect to a platform server without a signed SSL certificate Default - false
	 * Throws IllegalArgumentException if systemKey or systemSecret is null 
	 * @param systemKey The key used to identify the System in use
	 * @param systemSecret The secret used to verify the System in use
	 * @param initOptions HashMap of initialization options
	 * @param callback InitCallback for when initialization is done (success of failure)
	 * @throws IllegalArgumentException
	 */
	public static void initialize(String systemKey, String systemSecret, HashMap<String,Object> initOptions, InitCallback callback){
				
		if (systemKey == null) {
			throw new IllegalArgumentException("systemKey must be a non-empty Strings");
		}
		
		if(systemSecret == null) {
			throw new IllegalArgumentException("systemSecret can not be null");
		}
		
		//validate options
		validateOptions(initOptions, callback);
		
		Util.setSystemKey(systemKey);
		Util.setSystemSecret(systemSecret);
		
		//init platform url
		String platURL = (String) initOptions.get("platformURL");
		if(platURL != null){
			uri = platURL;
		}else{
			uri = "https://platform.clearblade.com";
		}
		
		//init messaging url
		String messURL = (String) initOptions.get("messagingURL");
		if(messURL != null){
			messageUrl = messURL;
		}else{
			messageUrl = "tcp://messaging.clearblade.com:1883";
		}
		
		//init logging
		Boolean log = (Boolean) initOptions.get("logging");
		if(log != null){
			setLogging(log);
		}
		
		//init call timeout
		Integer timeout = (Integer) initOptions.get("callTimeout");
		if(timeout != null && timeout > 0){
			setCallTimeOut(timeout);
		}else{
			setCallTimeOut(30000);
		}
		
		//init registerUser
		Boolean registerUser = (Boolean) initOptions.get("registerUser");
		if(registerUser == null){
			registerUser = false;
		}
	
		//init untrusted
		Boolean allowUntrusted = (Boolean) initOptions.get("allowUntrusted");
		if(allowUntrusted != null){
			setAllowUntrusted(allowUntrusted.booleanValue());
		}
		
		String email = (String) initOptions.get("email");
		final String password = (String) initOptions.get("password");
		
		user = new User(email);
		
		if(!initError && email != null && !registerUser.booleanValue()){
			//no init error, an email was given, and don't register user
			//just auth with given user info
			user.authWithCurrentUser(password, callback);
		}else if(!initError && registerUser.booleanValue()){
			//no errors, and register new user
			user.registerUser(password, callback);
		}else if(!initError && email == null){
			//email is null, so try to auth as anon user
			user.authWithAnonUser(callback);
		}
		
	}

	/**
	 * Returns a boolean that specifies if the API will show internal Logs
	 * @return logging boolean
	 */
	public static boolean isLogging() {
		return logging;
	}


	/**
	 * Sets the time in milliseconds that an http Request will wait for a 
	 * connection with the backend until it is aborted.
	 * @param timeOut milliseconds until http request is aborted
	 */
	public static void setCallTimeOut(int timeOut) {
		callTimeOut = timeOut;
	}

	/**
	 * If value is true, internal API logs will be displayed throughout the use of the 
	 * API else no internal logs displayed
	 * @param value determines API logging
	 */
	public static void setLogging(boolean value) {
		logging = value;
	}

	/**
	 * <p>Sets the masterSecret of the Application.
	 * If masterSecret is set it will be used instead
	 * of the appSecret. It gives complete access to APP resources
	 * </p>
	 * <strong>Never use the masterSecret in production code.</strong>
	 * @param myMasterSecret - The Applications Admin secret
	 */
	public static void setMasterSecret(String myMasterSecret) {
		masterSecret = myMasterSecret;
	}
	
	public static void setInitError(boolean value){
		initError = value;
	}
	
	private static void validateOptions(HashMap<String, Object> options,
			InitCallback callback) {
		initError = false;
		
		String email = (String) options.get("email");
		String password = (String) options.get("password");
		Boolean shouldRegister = (Boolean) options.get("registerUser");
		if(email == null && password != null){
			initError = true;
			callback.error(new ClearBladeException("Must provide both an email and password to authenticate. You only provided a password"));
		}else if(email != null && password == null){
			initError = true;
			callback.error(new ClearBladeException("Must provide both an email and password to authenticate. You only provided an email"));
		}else if(shouldRegister != null && shouldRegister.booleanValue() && email == null){
			initError = true;
			callback.error(new ClearBladeException("Cannot register anonymous user"));
		}
		
	}
}

