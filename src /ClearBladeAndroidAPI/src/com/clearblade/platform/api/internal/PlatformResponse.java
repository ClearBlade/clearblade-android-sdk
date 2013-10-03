package com.clearblade.platform.api.internal;
/**
 * This class consists of methods that configure and create ApiResponse objects.
 * 
 * @author CBIII
 * @since 1.0
 * @param <T> The Data Type to Store
 */
public class PlatformResponse<T> {
	
	private boolean error;
	private T data;
	/**
	 * Returns the Data stored in the ApiResponse Object
	 * @return T Any Data type stored
	 */
	public T getData() {
		return this.data;
	}
	
	/**
	 * Returns a boolean that determines if an error
	 * occurred during the API call
	 * @return error is True is an error occurred during the API call, false otherwise
	 */
	public boolean getError() {
		return this.error;
	}
	/**
	 * Constructs new ApiResponse of Type T
	 * @param error stored the condition of the API Call
	 * @param data stores data of type T
	 */
	public PlatformResponse(boolean error, T data) {
		this.error = error;
		this.data = data;
	}
	
	
}
