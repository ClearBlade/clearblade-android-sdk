package com.clearblade.platform.api.internal;

import android.util.Log;

import com.clearblade.platform.api.ClearBlade;

public class Util {
	
	private static String systemKey;							// system Id
	private static String systemSecret;						// system Password
	

	public static void setSystemKey(String systemKey) {
		Util.systemKey = systemKey;
	}

	public static void setSystemSecret(String systemSecret) {
		Util.systemSecret = systemSecret;
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
	
	protected static String getSystemKey() {
		return systemKey;
	}

	protected static String getSystemSecret() {
		return systemSecret;
	}
}
