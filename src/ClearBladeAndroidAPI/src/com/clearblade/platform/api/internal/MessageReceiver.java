package com.clearblade.platform.api.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.clearblade.platform.api.Item;
import com.clearblade.platform.api.MessageCallback;

public class MessageReceiver extends BroadcastReceiver {
	
	 MessageCallback messageReceivedCallback;
	 
	 public void addMessageReceivedCallback( MessageCallback back) {
		 messageReceivedCallback = back;
	 }
	 
	 @Override
	 public void onReceive(Context ctx, Intent intent) {
		 String action = intent.getAction();
		 if (action.equals(MessageService.MESSAGE_RECEIVED)){
			 String topic = intent.getStringExtra("topic");
			 byte[] message = intent.getByteArrayExtra("message");
			 messageReceivedCallback.done(topic, message);
			 String temp = new String(message);
			 messageReceivedCallback.done(topic, temp);
			 Item item = new Item(topic);
			 item.populateFromMessaging(temp);
			 messageReceivedCallback.done(topic, item);
		 }
	 }
}
