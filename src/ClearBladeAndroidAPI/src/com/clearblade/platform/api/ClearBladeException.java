package com.clearblade.platform.api;

@SuppressWarnings("serial")
/**
 * This class consists of methods that will throw an API specific message.
 * 
 * <p>It extends the Exception class
 * <strong>*It is not serializable*</strong>
 * </p>
 * @author Clyde Byrd III
 * @see Exception
 */
public class ClearBladeException extends Exception {
	/**
	 *  Constructs a new ClearBladeException with null as its detail message.
	 *  
	 *  <p>Constructs a new exception with null as its detail message. 
	 *  The cause is not initialized, and may subsequently be initialized 
	 *  by a call to Throwable.initCause(java.lang.Throwable).</p>
	 */
	public ClearBladeException() {
		super(); 
	}

	/**
	 *   Constructs a new ClearBladeException with the specified detail message.
	 *   <p>
	 *   The cause is not initialized, and may subsequently be initialized
	 *   by a call to Throwable.initCause(java.lang.Throwable).
	 *   </p>
	 * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 */
	public ClearBladeException(String message) { 
		super(message); 
	}

	/**
	 *   Constructs a new ClearBladeException with the specified detail message and cause.
	 *   <p>
	 *   Note that the detail message associated with cause is not automatically incorporated in this exception's detail message.
	 *   </p>
	 * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 * @param cause  the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public ClearBladeException(String message, Throwable cause) { 
		super(message, cause);
	}

	/**
	 *  Constructs a new ClearBladeException with the specified cause and a detail message of (cause==null ? null : cause.toString()) (which typically contains the class and detail message of cause).
	 * @param cause  the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public ClearBladeException(Throwable cause) { 
		super(cause); 
	}

}
