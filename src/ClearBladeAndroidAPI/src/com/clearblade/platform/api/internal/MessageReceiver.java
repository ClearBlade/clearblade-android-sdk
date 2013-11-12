package com.clearblade.platform.api.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.clearblade.platform.api.Item;
import com.clearblade.platform.api.MessageCallback;


 public class MessageReceiver extends BroadcastReceiver{
	 MessageCallback publishCallback;
	 MessageCallback subscribeCallback;
	 MessageCallback messageReceivedCallback;
	 MessageCallback unsubscribeCallback;
	 MessageCallback disconnectCallback;
	 
	 public void addPublishCallback(MessageCallback back) {
		 publishCallback = back;
	 }
	 
	 public void addSubscribeCallback(MessageCallback back) {
		 subscribeCallback = back;
	 }
	 
	 public void addMessageReceivedCallback( MessageCallback back) {
		 messageReceivedCallback = back;
	 }
	 
	 public void addUnsubscribeCallback(MessageCallback back) {
		 messageReceivedCallback = back;
	 }
	 
	 public void addDisconnectCallback( MessageCallback back) {
		 messageReceivedCallback = back;
	 }
	 
	 @Override
	 public void onReceive(Context ctx, Intent intent) {
		 String action = intent.getAction();
		 if (action.equals(MessageService.MESSAGE_ACTION_MESSAGE_RECEIVED)){
			 String topic = intent.getStringExtra("topic");
			 byte[] message = intent.getByteArrayExtra("message");
			 messageReceivedCallback.done(topic, message);
			 String temp = new String(message);
			 messageReceivedCallback.done(topic, temp);
			 Item item = new Item(topic);
			 item.populateFromMessaging(temp);
			 messageReceivedCallback.done(topic, item);
		 }else if (action.equals(MessageService.MESSAGE_ACTION_PUBLISH)){
			 String topic = intent.getStringExtra("topic");
			 String message = intent.getStringExtra("message");
			 publishCallback.done(topic, message);
		 }else if (action.equals(MessageService.MESSAGE_ACTION_SUBSCRIBE)){
			 String topic = intent.getStringExtra("topic");
			 subscribeCallback.done(topic, "");
		 }else if (action.equals(MessageService.MESSAGE_ACTION_UNSUBSCRIBE)){
			 
		 }else if (action.equals(MessageService.MESSAGE_ACTION_DISCONNECT)){
			 
		 }
	  
	 }
	 
	}
