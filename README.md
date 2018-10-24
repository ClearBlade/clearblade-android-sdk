API Reference
===========

Setup
------------
The ClearBlade Android API requires three JAR files that should be copied to your project’s /libs folder:
-ClearBlade_Android_API.jar
-org.eclipse.paho.client.mqttv3-1.0.2.jar
-org.eclipse.paho.android.service-1.0.3.jar
-gson-2.2.4.jar

Download these jars from https://github.com/ClearBlade/Android-API. They are located in the /jars folder.

_AndroidManifest.xml Configuration_

The AndroidManifest.xml files needs to be modified in order to start the Messaging service in the background. Add the following within the <manifest>..</manifest> tags:

<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

Add the following within the <application>..</application> tags:

<service android:name="org.eclipse.paho.android.service.MqttService" ></service>


Authenticating
------------------------------------
Authentication is the very first and crucial step in using the ClearBlade Android API for your application. You will not be able to access any features of the ClearBlade platform without Authentication.

You will need to import the following packages in your java file:
import com.clearblade.platform.api.ClearBlade;
import com.clearblade.platform.api.InitCallback;

There are two ways to authenticate to the ClearBlade platform:

 __Without  Options (Anonymous Authentication)__
 
String SYSTEM_KEY = "your_systemkey";
String SYSTEM_SECRET = "your_systemsecret";

InitCallback initCallback = new InitCallback(){
    @Override
    public void done(boolean results){
	    //initialization successful
	}
	@Override
	public void error(ClearBladeException exception){ 
	   //initialization failed, given a ClearBladeException with the cause
        Log.i("Failed init", "holy cow!!" + exception.getLocalizedMessage());
    }
};

ClearBlade clearBlade = new ClearBlade();
clearBlade.initialize(SYSTEM_KEY, SYSTEM_SECRET, initCallback);

__With Options__

String SYSTEM_KEY = "your_systemkey";
String SYSTEM_SECRET = "your_systemsecret";
HashMap<String, Object> initOptions = new HashMap<String, Object>();

/**Available init options:
	 * 	email - String to register or log-in as specific user (required if password is given) Default - null
	 * 	password - password String for given user (required if email is given) Default - null
	 * 	platformURL - Custom URL for the platform Default - https://platform.clearblade.com
	 * 	messagingURL - Custom Messaging URL Default - tcp://messaging.clearblade.com:1883
	 * 	registerUser - Boolean to tell if you'd like to attempt registering the given user Default - false
	 * 	logging - Boolean to enable ClearBlade Internal API logging Default - false
	 * 	callTimeout - Int number of milliseconds for call timeouts Default - 30000 (30 seconds)
	 *  allowUntrusted - Boolean to connect to a platform server without a signed SSL certificate Default - false
*/

InitCallback initCallback = new InitCallback(){
    @Override
    public void done(boolean results){
	    //initialization successful
	}
	@Override
	public void error(ClearBladeException exception){ 
	   //initialization failed, given a ClearBladeException with the cause
        Log.i("Failed init", "holy cow!!" + exception.getLocalizedMessage());
    }
};

initOptions.put("platformURL", "https://yourURL.com");
initOptions.put("messagingURL", "tcp://yourURL:1883");

ClearBlade clearBlade = new ClearBlade();
clearBlade.initialize(SYSTEM_KEY, SYSTEM_SECRET, initOptions, initCallback);


Code 
----------
The ClearBlade Android API allows executing a Code Service on the platform from your Android device.

__Please make sure that you have initialized and authenticated with the ClearBlade platform prior to using the Code API.__

You need to import the following packages to use the Code API:
import com.clearblade.platform.api.Code;
import com.clearblade.platform.api.CodeCallback;

__Code Service Without Parameters__

A code service which does not take any parameters can be executed as follows:

String serviceName = "yourServiceName";

CodeCallback codeCallback = new CodeCallback() {
	@Override
	public void done(JsonObject response) {
	    // Code Service executed successfully
		Log.i("codeResponse", response.toString());
	}

	@Override
	public void error(ClearBladeException exception) {
	    // Code Service execution failed
		Log.i("codeResponse", exception.getMessage());
	}
};

Code codeService = new Code(serviceName);
codeService.executeWithoutParams(codeCallback);

__Code Service With Parameters__
A Json Object of parameters needs to be passed to the Code class constructor along with the service name:

String serviceName = "yourServiceName";
String parameters = "{\"param1\":\"value1\"}";
JsonObject parameterJsonObject = new JsonParser().parse(parameters).getAsJsonObject();

CodeCallback codeCallback = new CodeCallback() {
	@Override
	public void done(JsonObject response) {
	    // Code Service executed successfully
		Log.i("codeResponse", response.toString());
	}

	@Override
	public void error(ClearBladeException exception) {
	    // Code Service execution failed
		Log.i("codeResponse", exception.getMessage());
	}
};

Code codeService = new Code(serviceName, parameterJsonObject);
codeService.executeWithParams(codeCallback);

Data
---------
With the ClearBlade Android API, a developer can use the Query, Item and Collection objects to manipulate data on the ClearBlade platform.
Import the following packages:
-import com.clearblade.platform.api.Collection;
-import com.clearblade.platform.api.Query;
-import com.clearblade.platform.api.Item;
-import com.clearblade.platform.api.DataCallback;

__Query__

Create a new Query object:

String collectionID = "yourCollectionID";
Query query = new Query(collectionID);

_query.EqualTo(String field, Object value)_


/**
	 * Creates an equality clause in the query object 
*/
	 query.equalTo('name', 'John');
	 query.fetch(new DataCallback{
	    public void done(QueryResponse resp){
	       //your logic here
	    }
	 });
/* Will only match if an item has an attribute 'name' that is equal to 'John' */

_query.notEqual(String field, Object value)_

/**
	 * Creates a non-equality clause in the query object 
*/
	 query.notEqual('name', 'John');
	 query.fetch(new DataCallback{
	    public void done(QueryResponse resp){
	       //your logic here
	    }
	 });
/* Will only match if an item has an attribute 'name' that is not equal to 'John' */

_query.greaterThan(String field, Object value)_

/**
	 * Creates a greater than clause in the query object 
*/
	 query.greaterThan('age', '18');
	 query.fetch(new DataCallback{
	    public void done(QueryResponse resp){
	       //your logic here
	    }
	 });
	 /* Will return all the items that are greater than age 18 if present*/
   
_query.greaterThanEqualTo(String field, Object value)_

/**
	 * Creates a greater than or equal to clause in the query object 
*/
	 query.greaterThanEqualTo('age', '18');
	 query.fetch(new DataCallback{
	    public void done(QueryResponse resp){
	       //your logic here
	    }
	 });
	 /* Will return all the items that are greater than equal to age 18 if present*/
   
_query.lessThan(String field, Object value)_

/**
	 * Creates a less than clause in the query object 
*/
	 query.lessThan('age', '18');
	 query.fetch(new DataCallback{
	    public void done(QueryResponse resp){
	       //your logic here
	    }
	 });
	 /* Will return all the items that are less than age 18 if present*/
   
_query.lessThanEqualTo(String field, Object value)_

/**
	 * Creates a less than equal to clause in the query object 
*/
	 query.lessThanEqualTo('age', '18');
	 query.fetch(new DataCallback{
	    public void done(QueryResponse resp){
	       //your logic here
	    }
	 });
	 /* Will return all the items that are less than equal to age 18 if present*/
   
_query.update(final DataCallback callback)_

	 /* Call an update on all items matching the query criteria to conform to the changes that have been added via the addChange method */
	 	query.equalTo("name", "John");
	 	query.addChange("name", "Johan");
	 	query.update( new DataCallback() {
	 		@Override
	 		public void done(Item[] response) {
	 		    // Query successful
	 		}
	 		@Override
	 		public void error(ClearBladeException exception) {
	 			// Query unsuccessful
	 		}
	 	});
    
_query.remove(final DataCallback callback)_

	 /* Removes on all items matching the query criteria within a Collection */

	 	query.equalTo("name", "John");
	 	query.remove( new DataCallback() {
	 		@Override
	 		public void done(Item[] response) {
	 		     // Query successful
	 		}
	 		@Override
	 		public void error(ClearBladeException exception) {
	 			// Query unsuccessful
	 		}
	 	});
The page size and page number of the results to be returned can be set by using query.setPageSize(int pageSize) and query.setPageNum(int pageNum).

Collections
-------------
The Collection class contains functions to __fetch (GET), update (PUT), create (POST)__ and __remove (DELETE)__ a collection using the REST API.

A collection object needs to be created first:

String collectionID = "yourCollectionID";
Collection collection = new Collection(collectionID);

_collection.fetch(Query query, final DataCallback callback)_

/** 
	 * Gets all Items that match Query criteria from the platform in the Cloud.
	 * Retrieved Items will be stored locally in the Collection.</p>
	 * Overrides previously stored Items*</strong>
	 * Runs in its own asynchronous task*</strong>
	 * @throws ClearBladeException will be returned in callback.error() if the collection was empty
	 */
Query query = new Query();
query.equalTo("height", 105);
collection.fetch(query, new DataCallback() {
    @Override
    public void done(QueryResponse response) {
        //Success
    }
    @Override
    public void error(ClearBladeException exception) {
        //Failure
    }
});

_collection.fetchAll(final DataCallback callback)_

/** 
	 * Gets all Items that are saved in the collection in the Cloud.
	 * Retrieved Items will be stored locally in the Collection.</p>
	 * Overrides previously stored Items*</strong>
	 * Runs in its own asynchronous task*</strong>
	 * @throws ClearBladeException will be returned in callback.error() if the collection was empty
	 */
collection.fetchAll(new DataCallback() {
    @Override
    public void done(QueryResponse response) {
        //Success
    }
    @Override
    public void error(ClearBladeException exception) {
        //Failure
    }
});

_collection.update(final DataCallback callback)_

Query query = new Query(collectionID);
query.equalTo("name", "John");
query.addChange("name", "Johan");
collection.update(new DataCallback() {
    @Override
    public void done(Item[] response) {
        // Query successful
    }
    @Override
    public void error(ClearBladeException exception) {
        // Query unsuccessful
    }
});

_collection.create(String columns, final DataCallback callback)_

String column = "{\"columnName\":\"newColumn\"}";
collection.create(column, new DataCallback() {
    @Override
    public void done(Item[] response) {
        // Query successful
    }
    @Override
    public void error(ClearBladeException exception) {
        // Query unsuccessful
    }
});

_collection.remove(DataCallback callback)_

/** 
	 * Deletes all Items that are saved in the collection in the Cloud synchronously.
	 * Deleted Items will be stored locally in the Collection.</p>
	 * Overrides previously stored Items*</strong>
	 * Runs in its own asynchronous task*</strong>
	 * @throws ClearBladeException will be returned in the callback error function
	 */
collection.remove(new DataCallback() {
    @Override
    public void done(QueryResponse response) {
        //Success
    }
    @Override
    public void error(ClearBladeException exception) {
        //Failure
    }
});


Messaging
---------
The Messaging API is used initialize, connect and communicate with the ClearBlade MQTT Broker for publishing messages, subscribing, unsubscribing to and from topics and disconnect. The API uses the Paho MQTT Asynchronous Client.

__Please make sure that you have initialized and authenticated with the ClearBlade platform prior to using the Messaging API. This is important because the ClearBlade MQTT Broker requires the authentication token to establish a successful connection. This authentication token can only be obtained by initializing and authenticaing with the ClearBlade platform__

You will need to import the following packages for using the Messaging API:
import com.clearblade.platform.api.Message;
import com.clearblade.platform.api.MessageCallback;

__Initialize and Connect__

The first step is to create a new Message object by passing the application context and messaging QoS (optional). The Message constructor will then initialize and connect with the MQTT Broker.

Context context = this; 
/* context = this; iff your main class extends Activity
* If your main class extends Application, context = getApplicationContext();
*/
Message message = new Message(context); // QoS = 0 Default

OR

int qos = 1; // QoS can be 0,1 or 2
Context context = this; 
/* context = this; iff your main class extends Activity
* If your main class extends Application, context = getApplicationContext();
*/
Message message = new Message(context, qos);

After the connection is successful, you can publish, subscribe, unsubscribe or disconnect using the Message object.

__Publish__

The publish function takes a topic and message of type String and publishes to the MQTT Broker.

String topic = "yourTopic";
String message = "yourMessage";
message.publish(topic, message);

__Subscribe__

The subscribe function takes a topic of type String and a callback to handle the arrived messages.

String topic = "topicToSubscribe";
MessageCallback messageCallback = new MessageCallback() {
	@Override
	public void done(String topic, String messageString) {
		//Message arrived on subscribed topic
	}
};
message.subscribe(topic, messageCallback);

__Unsubscribe__

The unsubscribe function takes a topic of type String.

String topic = "topicToUnsubscribe";
message.unsubscribe(topic);

__Disconnect__

The disconnect function is used to disconnect from the MQTT Broker. Note that this does not disconnect the user from the ClearBlade platform. User logout needs to be called separately.

message.disconnect();

Javadoc
=======

The Javadoc for the Android API can be found at: https://docs.clearblade.com/v/3/static/androidapi/index.html

QuickStart
==========

_Installing Android Studio_

Download and install Android Studio from https://developer.android.com/sdk/index.html and configure the SDKs from the instructions given at https://developer.android.com/sdk/installing/index.html.

_Configuration_

After you open your project in Android Studio, download the required jars and edit the AndroidManifest.xml file specified in the API Reference. After that you can initialize with the platform and use its features.



















