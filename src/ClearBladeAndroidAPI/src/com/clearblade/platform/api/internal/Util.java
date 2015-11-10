package com.clearblade.platform.api.internal;

import android.util.Log;

import com.clearblade.platform.api.ClearBlade;

public class Util {
	
	private static String systemKey;							// system Id
	private static String systemSecret;						// system Password
	private static String certPassword;						// certificate password
	private static String certPath;							//certificate path
	

	public static void setSystemKey(String systemKey) {
		Util.systemKey = systemKey;
	}

	public static void setSystemSecret(String systemSecret) {
		Util.systemSecret = systemSecret;
	}

	public static void setCertPassword(String password) {Util.certPassword = password;}

	public static void setCertPath(String path) {Util.certPath = path;}

	
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
	
	public static String getSystemKey() {
		return systemKey;
	}

	public static String getCertPath() { return certPath; }

	protected static String getSystemSecret() {
		return systemSecret;
	}

	protected static String getCertPassword() { return certPassword; }
}
