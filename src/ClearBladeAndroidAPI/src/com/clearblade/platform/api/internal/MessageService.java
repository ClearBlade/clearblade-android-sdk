package com.clearblade.platform.api.internal;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.android.service.*;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings.Secure;
import android.util.Log;

import com.clearblade.platform.api.ClearBladeException;
import com.clearblade.platform.api.InitCallback;
import com.clearblade.platform.api.Item;
import com.clearblade.platform.api.ClearBlade;
import com.clearblade.platform.api.User;


public class MessageService implements MqttCallback {
	
	public String url = ClearBlade.getMessageUrl();
	
	public static final String 	DEBUG_TAG = "MqttService"; // Debug TAG
 	public static final String	MESSAGE_RECEIVED = "MESSAGE_RECEIVED";
	
	private String deviceId;
	private int qualityOfService;

	//mqtt paho stuff
	private MqttClientPersistence clientPersistance = null;
	private MqttConnectOptions opts;			
	private MqttAndroidClient androidClient;
    
	private Context context;
	
	private InitCallback connectCallback;
	
	public void initializeAndConnect(Context ctx, int qos, InitCallback callback) {
		
		connectCallback = callback;
		context = ctx;
		qualityOfService = qos;
		deviceId = "ad" + Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID);
		
		opts = new MqttConnectOptions();
		opts.setCleanSession(true);
		
		User curUser = ClearBlade.getCurrentUser();
		
		if(curUser.getAuthToken() == null) {
			Log.i(DEBUG_TAG,"Auth token is null, cannot start messaging service");
			return;
		} else {
			Log.i(DEBUG_TAG,"Received Authenticated user details");
			opts.setUserName(curUser.getAuthToken());
			opts.setPassword(Util.getSystemKey().toCharArray());
		}
		
		connect(ctx, callback);
	}
	
	private void connect(Context ctx, final InitCallback callback) {
		
		try {
            androidClient = new MqttAndroidClient(ctx, url, deviceId, clientPersistance);
            androidClient.connect(opts, ctx, new IMqttActionListener() {

				@Override
				public void onFailure(IMqttToken arg0, Throwable arg1) {
					Log.i(DEBUG_TAG, "Could not connect to MQTT broker: " + arg1.getMessage());
					callback.error(new ClearBladeException(arg1.getMessage()));
					
				}

				@Override
				public void onSuccess(IMqttToken arg0) {
					Log.i(DEBUG_TAG, "Client Connected");
					callback.done(true);
				}
            	
            });
            androidClient.setCallback(this);

		} catch (MqttException err) {
			Log.i(DEBUG_TAG, err.getLocalizedMessage());
		}
	}
	
	public void disconnect() {
		
		if (androidClient.isConnected()) {
			try {
				androidClient.disconnect();
				Log.i(DEBUG_TAG, "Client Disconnected");
			} catch (MqttException err) {
				Log.i(DEBUG_TAG, err.getLocalizedMessage());
			}
		} else {
			Log.i(DEBUG_TAG, "Client is already disconnected");
		}
	}

	public void publish(String topic, byte[] payload) {
		
		if (androidClient.isConnected()) {
			try {
				androidClient.publish(topic, payload, qualityOfService, false);
			} catch (MqttPersistenceException e) {
				Log.i(DEBUG_TAG, e.getLocalizedMessage());
			} catch (MqttException e) {
				Log.i(DEBUG_TAG, e.getLocalizedMessage());
			}
		} else {
			Log.i(DEBUG_TAG, "Could not publish. Client is not connected. Please connect first.");
		}
	}
	
	public void publish(String topic, String payload) {
		
		publish(topic, payload.getBytes());
	}
	
	public void publish(String topic, Item payload) {
		
		publish(topic, payload.toString());
	}

	public boolean subscribe(final String topic) {
		
		if (androidClient.isConnected()) {
			try {
				androidClient.subscribe(topic, qualityOfService);
				return true;
			} catch (MqttException err) {
				Log.i(DEBUG_TAG, err.getLocalizedMessage());
				return false;
			}
		} else {
			Log.i(DEBUG_TAG, "Could not subscribe. Client is not connected. Please connect first.");
			return false;
		}

    }

	
	public void unsubscribe(String topic) {
		
		if (androidClient.isConnected()) {
			try {
				androidClient.unsubscribe(topic);
			} catch (MqttException err) {
				Log.i(DEBUG_TAG, err.getLocalizedMessage());
			}
		} else {
			Log.i(DEBUG_TAG, "Could not unsubscribe. Client is not connected. Please connect first.");
		}
	}

    @Override
    public void connectionLost(Throwable throwable) {
    	Log.i(DEBUG_TAG, throwable.getMessage() + ". Attempting to reconnect");
		androidClient = null;
		connect(context, connectCallback);
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        Log.i(DEBUG_TAG, "Topic: " + topic + " Message: " + new String(mqttMessage.getPayload()));
        Intent intent = new Intent();
        intent.setAction(MESSAGE_RECEIVED);
        intent.putExtra("topic", topic);
        intent.putExtra("message", mqttMessage.getPayload());
        context.sendBroadcast(intent);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }


}
