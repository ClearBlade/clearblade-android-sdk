package com.clearblade.platform.api;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.clearblade.platform.api.internal.MessageReceiver;
import com.clearblade.platform.api.internal.MessageService;

import java.util.HashSet;


public class Message {
	MessageReceiver messageReceiver;
	Context context;
	HashSet<String> subscribed;
	public Message(Context ctx){
		context = ctx;
		//Start our own service
	    Intent intent = new Intent(context, MessageService.class);
	    intent.setAction(MessageService.MESSAGE_ACTION_START);
	    //intent.putExtra("topic", topic);
	    context.startService(intent);
	    subscribed = new HashSet<String>();
	    
	}
	
	//MessageCallback callback;
	
	public void subscribe(String topic, MessageCallback back){
		if (subscribed.contains(topic)){
			return;
		} else {
			subscribed.add(topic);
		}

	    //send the subscribe message
		//serviceReceiver = new ServiceReceiver();
	    Intent intent = new Intent();
		intent.setAction(MessageService.MESSAGE_ACTION_SUBSCRIBE);
		intent.putExtra("topic", topic);
		context.sendBroadcast(intent);	
		
		//Set the callback events for publish events
	    messageReceiver = new MessageReceiver();
	    IntentFilter intentFilter = new IntentFilter();
	    intentFilter.addAction(MessageService.MESSAGE_ACTION_MESSAGE_RECEIVED);
	    context.registerReceiver(messageReceiver, intentFilter);
	    messageReceiver.addMessageReceivedCallback(back);
	}
	
	public void publish(String topic, String message) {
		Intent intent = new Intent();
		intent.setAction(MessageService.MESSAGE_ACTION_PUBLISH);
		intent.putExtra("topic", topic);
		intent.putExtra("message", message);
		context.sendBroadcast(intent);
	}
	
	public void unsubscribe(String topic){
		subscribed.remove(topic);
		Intent intent = new Intent();
		intent.setAction(MessageService.MESSAGE_ACTION_SUBSCRIBE);
		intent.putExtra("topic", topic);
		context.sendBroadcast(intent);	
	}
	
	public void destroy(){
		context.unregisterReceiver(messageReceiver);
	}
}
