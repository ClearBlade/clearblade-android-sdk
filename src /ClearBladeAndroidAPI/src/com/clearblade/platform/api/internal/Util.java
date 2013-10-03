package com.clearblade.platform.api.internal;

import android.util.Log;

import com.clearblade.platform.api.ClearBlade;

public class Util {
	
	private static String appKey;							// app Id
	private static String appSecret;						// app Password
	

	public static void setAppKey(String appKey) {
		Util.appKey = appKey;
	}

	public static void setAppSecret(String appSecret) {
		Util.appSecret = appSecret;
	}

	
	/**
	 * Displays internal log messages when using the API
	 * @protected
	 * @param tag The Class calling
	 * @param log The Message to display
	 * @param error is the message an Error?
	 */
	protected static void logger(String tag, String log, boolean error) {
		if(ClearBlade.isLogging()) {
			if(error) {
				Log.e(tag, log);
			} else {
				Log.v(tag, log);
			}
		}
	}
	
	protected static String getAppKey() {
		return appKey;
	}

	protected static String getAppSecret() {
		return appSecret;
	}
}
