package com.clearblade.platform.api.internal;

import java.util.Map.Entry;

import android.net.Uri;

import com.clearblade.platform.api.ClearBlade;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
/**
 * This class consists of instance methods that create and modify
 * http headers that will be used in calls to the API.
 * 
 * <p>Headers are typically used in conjunction with the Request Engine Class
 * like so:
 * <pre>
 * RequestProperties headers = new RequestProperties.Builder(ClearBlade.getUri(), ClearBlade.getCallTimeOut()).build();
 * request.setHeaders(headers);
 * </pre>
 * </p>
 * @author CBIII
 * @since 1.0
 * @see RequestEngine
 */
public class RequestProperties {
	private String authentication;		// Holds authentication for API Call
	private String body;				// Holds the content of the API Call
	private String endPoint;			// The section of the backend to send this information to, 
	private String method;				// The http method to use
	private String qs;					// the query string to be used
	private int timeout;				// the time the API CALL will wait for a connection until it aborts.
	private String uri;					// the backend uri

	/**
	 * Class consists of methods that configure the RequestProperties object 
	 * @author CBIII
	 * @since 1.0
	 *
	 */
	public static class Builder {

		//optional 
		private String uri = ClearBlade.getUri();
		private String body = "";
		private String endPoint = "";
		private String method = "GET";
		private String qs = "";
		private int timeout = ClearBlade.getCallTimeOut();

		/**
		 * Returns an html encoded string
		 * @param params JsonObject that is Uri encoded
		 * @return encodedString html safe string
		 */
		private String encodeParams (JsonObject params) {
			StringBuilder encodedString = new StringBuilder(""); 
			if (!params.isJsonNull()) {
				for (Entry<String, JsonElement> entry : params.entrySet()) {
					encodedString.append(Uri.encode(entry.getKey()) + "=" + Uri.encode(entry.getValue().getAsString())+"&");
				}
				// delete the '&' on at the end
				encodedString.setLength(encodedString.length()-1);
			}
			return encodedString.toString();
		}
		/**
		 * Constructs a new Builder object.
		 * 
		 */
		public Builder () {

		}

		/** 
		 * returns the current Builder Object with method
		 * set as the given http Verb.
		 * @param httpVerb method to use for API Call
		 * @return this the builder Object being modified
		 */
		public Builder method (String httpVerb) {
			method = httpVerb;
			return this;
		}

		/** 
		 * returns the current Builder Object with timeout
		 * set as the given time in milliseconds.
		 * @param time milliseconds until API call is aborted unless a connection is made
		 * @return this the builder Object being modified
		 */
		public Builder setTimeOut (int time) {
			timeout = time;
			return this;
		}
		/** 
		 * returns the current Builder Object with query string
		 * set as the given Jsonobject.
		 * @param toEncode object to convert in to a query string
		 * @return this the builder Object being modified
		 */
		public Builder qs (JsonObject toEncode) {
			qs = encodeParams(toEncode);
			return this;
		}

		/** 
		 * returns the current Builder Object with body
		 * set as the given data.
		 * @param data the payload for the request
		 * @return this the builder Object being modified
		 */
		public Builder body (JsonObject data) {
			body = data.toString();
			// if a JsonObject is inserted as a key-value pair in to another JsonObject
			// the JsonObject to be converted is inserted as an escaped string.
			// we need to unescape it for the API call; The backend currently will not accept it as is.
			if(body.contains("\\"))		
				body = body.replace("\"{", "{").replace("}\"", "}").replace("\\", "");
			return this;
		}

		/** 
		 * returns the current Builder Object with url end Point
		 * set as the given endPoint.
		 * @param urlEnd the final part to be joined with the uri to create a url
		 * @return this the builder Object being modified
		 */
		public Builder endPoint (String urlEnd) {
			endPoint = urlEnd;
			return this;
		}

		/** 
		 * returns a RequestProperties object
		 * made from the Builder object.
		 * 
		 * @return headers The RequestProperties object constructed
		 */
		public RequestProperties build() {
			return new RequestProperties(this);
		}
	}

	/**
	 * Returns the Authentication of the RequestProperties
	 * @return authentication string used for App authentication with the Cloud backend
	 */
	public String getAuthentication() {
		return authentication;
	}

	/**
	 * Returns the payload for the RequestProperties
	 * @return payload The content of the API call to be made
	 */
	public String getBody() {
		return body;
	}

	/**
	 * Returns the endPoint of the RequestProperties
	 * @return endPoint The section of the backend to send the request to
	 */
	public String getEndPoint() {
		return endPoint;
	}

	/**
	 * Returns the http method the API Call will use
	 * @return httpVerb http method to use
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * Returns the Query String to attach to the url
	 * @return queryString
	 */
	public String getQs() {
		return qs;
	}

	/** 
	 * Returns the time in milliseconds until the API call will wait for a backend connection until it is
	 * aborted
	 * @return time milliseconds to wait for a connection with the backend
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * Returns the uri the API Call will send information too.
	 * @return uri base uniform resource identifier
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * Constructs a RequestProperties object that is made from given
	 * builder object.
	 * @param builder
	 */
	public RequestProperties (Builder builder) {
		//authentication = ClearBlade.getAuthentication();
		body = builder.body;
		endPoint = builder.endPoint;
		method = builder.method;
		qs = builder.qs;
		uri = builder.uri;
		if(builder.timeout != 0) {
			timeout = builder.timeout;
		} else {
			timeout = ClearBlade.getCallTimeOut();
		}
		if(!endPoint.equals(""))
			if (endPoint.equals("api/v/3/user/authWithSSL")) {
				uri += ":8950/" + endPoint;
			} else {
				uri += "/" + endPoint;
			}
		if(!qs.equals(""))
			uri += "?" + qs;
	}

}
