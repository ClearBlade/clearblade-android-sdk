package com.clearblade.platform.api;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MessageMqttCallback implements MqttCallback {

	@Override
	public void connectionLost(Throwable arg0) {
		System.out.println("received a connectionLost");
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		System.out.println("received a deliveryComplete");
	}

	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		System.out.println("received a messageArrived");
	}

}
