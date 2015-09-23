package com.clearblade.platform.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;

import android.content.Context;
import android.content.IntentFilter;

import com.clearblade.platform.api.internal.MessageReceiver;
import com.clearblade.platform.api.internal.MessageService;
import com.clearblade.platform.api.internal.PlatformCallback;
import com.clearblade.platform.api.internal.RequestEngine;
import com.clearblade.platform.api.internal.RequestProperties;
import com.clearblade.platform.api.internal.UserTask;
import com.clearblade.platform.api.internal.Util;


public class Message {
	
	MessageService messageService;
	public static HashSet<String> subscribed;
	int qualityOfService;
	Context context;

	public Message(Context ctx, InitCallback callback) {
		
		context = ctx;
	    qualityOfService = 0;
	    
	    messageService = new MessageService();
		messageService.initializeAndConnect(ctx, qualityOfService, callback);
		subscribed = new HashSet<String>();
	}
	
	//constructor to set custom QoS level
	public Message(Context ctx, int qos, InitCallback callback) {
		
		context = ctx;
		//check qos is valid, if not return error
		if(qos > 2 || qos < 0){
			throw new IllegalArgumentException("qualityOfService must be 0, 1, or 2");
		}
		
		//set to passed qos value
		qualityOfService = qos;

        messageService = new MessageService();
		messageService.initializeAndConnect(ctx, qualityOfService, callback);
		subscribed = new HashSet<String>();
	}
	
	public void subscribe(String topic, MessageCallback back) {
		
		if(subscribed.contains(topic)){
			return;
		} else {
			subscribed.add(topic);
		}

	   boolean isSubscribed = messageService.subscribe(topic);
	   
	   if (isSubscribed) {
		   MessageReceiver messageReceiver = new MessageReceiver();
		   IntentFilter intentFilter = new IntentFilter();
		   intentFilter.addAction(MessageService.MESSAGE_RECEIVED);
		   context.registerReceiver(messageReceiver, intentFilter);
		   messageReceiver.addMessageReceivedCallback(back);
	   }
	}
	
	public void publish(String topic, String message) {
		
		messageService.publish(topic, message);
	}
	
	public void unsubscribe(String topic) {
		
		messageService.unsubscribe(topic);
	}
	
	public void disconnect() {
		
		messageService.disconnect();
	}
	
	
	public void getHistory(String topic, int count, String lastTime, final MessageCallback callback) {
		
		RequestEngine request = new RequestEngine();
		
		String t = topic;
		try {
			t = URLEncoder.encode(topic, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String endpoint = "api/v/1/message/" +Util.getSystemKey()+"?topic="+t+"&count="+count+"&last="+lastTime;
		RequestProperties headers = new RequestProperties.Builder().method("GET").endPoint(endpoint).build();
		request.setHeaders(headers);
		final History history = new History();
		
		UserTask asyncFetch = new UserTask(new PlatformCallback(history, callback){
			@Override
			public void done(String response){
				try {
					history.loadHistoryJSON(response);
				} catch (ClearBladeException e) {
					e.printStackTrace();
				}
				callback.done(history);
			}
			@Override
			public void error(ClearBladeException exception){
				ClearBlade.setInitError(true);
				callback.error(exception);
			}
		});
		
		asyncFetch.execute(request);
	}

}
