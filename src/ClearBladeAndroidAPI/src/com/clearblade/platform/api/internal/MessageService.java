package com.clearblade.platform.api.internal;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.util.Log;

import com.clearblade.platform.api.Item;
import com.clearblade.platform.api.MessageCallback;
import com.clearblade.platform.api.ClearBlade;


public class MessageService extends Service implements MqttCallback{
	public String url = ClearBlade.getMessageUrl();
	public final static String 		MESSAGE_ACTION_SUBSCRIBE = "MESSAGE_ACTION_SUBSCRIBE";
	public final static String 		MESSAGE_ACTION_PUBLISH = "MESSAGE_ACTION_PUBLISH";
	public final static String 		MESSAGE_ACTION_UNSUBSCRIBE = "MESSAGE_ACTION_UNSUBSCRIBE";
	public final static String 		MESSAGE_ACTION_DISCONNECT = "MESSAGE_ACTION_DISCONNECT";
	public final static String 		MESSAGE_ACTION_MESSAGE_RECEIVED = "MESSAGE_ACTION_MESSAGE_RECEIVED";
	public static final String 		MESSAGE_ACTION_START 	= "MESSAGE_START"; 
	public static final String 		MESSAGE_ACTION_STOP		= "MESSAGE_STOP"; 
	public static final String 		MESSAGE_ACTION_RECONNECT= "MESSAGE_RECONNECT";
	
	public static final String 		DEBUG_TAG = "MqttService"; // Debug TAG

 
	//handle the state of the mqtt connection
	private boolean isStarted = false; 
	
	
	private String deviceId;		 
	private Handler connectionHandler;	  

	//mqtt paho stuff
	private MemoryPersistence memoryPersistance; 		
	private MqttConnectOptions opts;			
	private MqttClient mqttClient;					

	// receiver listens for external events UI or Network connection
	private ConnectivityManager connectivityManager; 
	private MessageReceiver messageReceiver;
	
	
	@Override
	public void onCreate() {
		super.onCreate();

		deviceId = "ad"+Secure.getString(getContentResolver(), Secure.ANDROID_ID);
		HandlerThread thread = new HandlerThread("MessageServiceThread");
		thread.start();

		connectionHandler = new Handler(thread.getLooper());

		connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		
		//sets the clean session option, where is the cheapest place to set this?
		opts = new MqttConnectOptions();
		opts.setCleanSession(true);
		opts.setUserName(Util.getAppKey());
		opts.setPassword(Util.getAppSecret().toCharArray());
		String action = intent.getAction();

		Log.i(DEBUG_TAG,"Received action of " + action);

		if(action == null) {
			Log.i(DEBUG_TAG,"Starting service with no action\n Probably from a crash");
		} else {
			if(action.equals(MESSAGE_ACTION_START)) {
				Log.i(DEBUG_TAG,"Received ACTION_START");
				start();
			} else if(action.equals(MESSAGE_ACTION_STOP)) {
				stop();
			} else if(action.equals(MESSAGE_ACTION_RECONNECT)) {
				if(isNetworkAvailable()) {
					reconnectIfNecessary();
				}
			}
		}

		return START_REDELIVER_INTENT;
	}

	private synchronized void start() {
		/**starting the broker**/
		
		if(isStarted) {
			return;
		}

		connect();

		registerReceiver(mConnectivityReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
		
		messageReceiver = new MessageReceiver();
		messageReceiver.addSubscribeCallback(new MessageCallback(){
			@Override
			public void done(String topic, String message, int qos) {
				subscribe(topic, qos);
			}	
		});
		
		messageReceiver.addPublishCallback(new MessageCallback(){
			@Override
			public void done(String topic, String message, int qos) {
				publish(topic,message, qos);
			}	
		});
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(MessageService.MESSAGE_ACTION_SUBSCRIBE);
		intentFilter.addAction(MessageService.MESSAGE_ACTION_PUBLISH);
		registerReceiver(messageReceiver, intentFilter);
		
	}
	
	private synchronized void stop() {
		if(!isStarted) {
			return;
		}

		if(mqttClient != null) {
			connectionHandler.post(new Runnable() {
				@Override
				public void run() {
					try {
						mqttClient.disconnect();
					} catch(MqttException ex) {
						ex.printStackTrace();
					}
					mqttClient = null;
					isStarted = false;

				}
			});
		}

		unregisterReceiver(mConnectivityReceiver);
	}
	
	private synchronized void connect() {
		
		try {
			mqttClient = new MqttClient(url,deviceId, memoryPersistance);

		} catch(MqttException e) {
			e.printStackTrace();
		}

		connectionHandler.post(new Runnable() {
			@Override
			public void run() {
				try {
					mqttClient.connect(opts);

					mqttClient.setCallback(MessageService.this);

					isStarted = true; // Service is now connected

					
				} catch(MqttException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private synchronized void reconnectIfNecessary() {
		if(isStarted && mqttClient == null) {
			connect();
		}
	}
	
	private boolean isNetworkAvailable() {
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();

		return (info == null) ? false : info.isConnected();
	}
	
	/**
	 * Receiver that listens for connectivity changes
	 * via ConnectivityManager
	 */
	private final BroadcastReceiver mConnectivityReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(DEBUG_TAG,"Connectivity Changed...");
		}
	};


	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void connectionLost(Throwable arg0) {
		mqttClient = null;

		if(isNetworkAvailable()) {
			reconnectIfNecessary();
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		
	}
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		Log.i(DEBUG_TAG,"  Topic:\t" + topic +
				"  Message:\t" + new String(message.getPayload()) +
				"  QoS:\t" + message.getQos());
		Intent intent = new Intent();
		intent.setAction(MESSAGE_ACTION_MESSAGE_RECEIVED);
		intent.putExtra("topic", topic);
		//intent.putExtra("item", new Item(message.getPayload()));
		Object obj = message.getPayload();
		
		intent.putExtra("message", message.getPayload());
		
		sendBroadcast(intent);
	}

	public void publish(String topic, byte[] payload, int qos){
		try {
			mqttClient.publish(topic, payload, qos, false);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public void publish(String topic, String payload, int qos){
		publish(topic, payload.getBytes(), qos);
	}
	
	public void publish(String topic, Item payload, int qos){
		publish(topic, payload.toString(), qos);
	}

	public void subscribe(String topic, int qos){
		try {
			mqttClient.subscribe(topic, qos);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	


}