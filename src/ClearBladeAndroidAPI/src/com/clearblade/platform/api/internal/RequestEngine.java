package com.clearblade.platform.api.internal;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

import com.clearblade.platform.api.ClearBlade;

import android.util.Log;

/**
 * This class consists of methods that configure and execute API Calls
 * <p>
 * This class is currently not thread safe.
 * A typical example of this classes usage:
 * <pre>
 *
 *RequestProperties headers = new RequestProperties.Builder().method("DELETE").endPoint("apidev/" + collection).qs(queryString).build();
 *	request.setHeaders(headers);
 *	
 *	ApiResponse<String> result = request.execute();
 *	// If there was an error log it,
 *	if(result.getError()) {	
 *		throw new Exception("Call to delete failed:"+result.getData());
 *	} else {
 *		this.json = convertJsonStringInToJsonObject(result.getData());
 *	}
 * </pre>
 * </p>
 * @author Clyde Byrd III
 * @author Aaron Allsbrook
 * @since 1.0
 * @see HttpURLConnection
 * @see PlatformResponse
 *
 */
public class RequestEngine {
	private final String TAG = "RequestEngine";

	private KeyStore keyStore;
	private FileInputStream fis;
	//internal flag for understanding the use of SSL on the platform server
	private int isSSL = -1;
	
	private RequestProperties headers;
	
	/**
	 * Constructs a RequestEngine Object with null RequestProperties object
	 */
	public RequestEngine() {
		this.headers = null;
	}

	/**
	 * Constructs a RequestEngine Object with given RequestProperties object
	 * @param headers
	 */
	public RequestEngine(RequestProperties headers) {
		this.headers = headers;
	}

	/**
	 * Returns an ApiResponse<String> object that contains the 
	 * results of the API call. The String in ApiResponse<String>.getData()
	 * is usually converted to a more useful object.
	 * @return result stores the condition of the ApiRequest
	 */
	public PlatformResponse<String> execute() {
		return request();
	}
	
	/**
	 * Returns an ApiResponse<String> object that contains the 
	 * results of the API call. The String in ApiResponse<String>.getData()
	 * is usually converted to a more useful object.
	 * @return result stores the condition of the ApiRequest
	 */
	public PlatformResponse<String> executeOnActivity() {
		return request();
	}
	
	/**
	 * Returns the output from an InputStream
	 * @param in inputStream to get data from
	 * @return output A string gathered from the inputStream
	 * @throws IOException
	 */
	private String readStream(InputStream in) throws IOException {
		StringBuilder sb = new StringBuilder();  
		BufferedReader r = new BufferedReader(new InputStreamReader(in), 4096);  
		for (String line = r.readLine(); line != null; line =r.readLine()){  
			sb.append(line);  
		}  
		in.close();  
		return sb.toString();
	}

		
	/**
	 * Sets the RequestEngine Object's headers to the given RequestProperties
	 * object.
	 * @param headers RequestProperties to make API Call from
	 */
	public void setHeaders(RequestProperties headers) {
		this.headers = headers;
	}
	
	
	/**
	 * Returns an ApiResponse<String> object that contains the 
	 * results of the API call. The String in ApiResponse<String>.getData()
	 * is usually converted to a more useful object.
	 * @private
	 * @return result stores the condition of the ApiRequest
	 * //@throws IllegalArguementExecption will be thrown if headers is null
	 */
	private PlatformResponse<String> request(){
		String reqURL = this.headers.getUri().toLowerCase();
		if(this.headers == null){
			throw new IllegalArgumentException("The headers must not be null!");
		}
		
		HttpURLConnection urlConnection = null;
		// used to determine if error happened during call
		boolean err = false;

		// The two variables hold the server Status codes.
		int responseCode = 0;
		String responseMessage = null;

		PlatformResponse<String> result = null;
		SSLContext ctx=null;
		try {
			if (isSSL() && ClearBlade.getAllowUntrusted()) {
				ctx = createUntrustedManager(ctx);
				HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());
			}
			if (reqURL.contains("/user/authwithssl")) {	// Use SSL certificate to make the HTTP request
				String clientCertPassword = Util.getCertPassword();
				try {
					keyStore = KeyStore.getInstance("PKCS12");
				} catch (KeyStoreException e) {
					e.printStackTrace();
				}

				try {
					fis = new FileInputStream(Util.getCertPath());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				try {
					keyStore.load(fis, clientCertPassword.toCharArray());
				} catch (IOException e) {
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (CertificateException e) {
					e.printStackTrace();
				}

				try {
					KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
					kmf.init(keyStore, clientCertPassword.toCharArray());
					KeyManager[] keyManagers = kmf.getKeyManagers();
					SSLContext sslContext = SSLContext.getInstance("TLS");
					sslContext.init(keyManagers, null, null);
					HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (UnrecoverableKeyException e) {
					e.printStackTrace();
				} catch (KeyStoreException e) {
					e.printStackTrace();
				}
			}
			String method = this.headers.getMethod();
			String charset = "UTF-8";
			URL url = new URL(this.headers.getUri());
			
			if (isSSL()){
				urlConnection = (HttpsURLConnection) url.openConnection();
				
			}else {
				urlConnection = (HttpURLConnection) url.openConnection();
				
			}
			
			urlConnection.setRequestMethod(method);
			urlConnection.setConnectTimeout(this.headers.getTimeout());

			//things get ugly here. most requests should just need usertoken, but you need key/secret
			// to get token on the auth request and to register new user. also both token and key/secret 
			// are needed for logout and auth check requests. so if the url cotains the logout or check 
			// endpoints, we add all 3, otherwise we add token
			boolean isLogoutOrAuthCheck = (reqURL.contains("/user/logout") ||
											reqURL.contains("/user/checkauth"));
			boolean isAuthOrReg = (reqURL.contains("/user/auth") ||
									reqURL.contains("/user/anon") ||
									reqURL.contains("/user/reg") ||
									reqURL.contains("/user/authWithSSL"));
			String userToken = ClearBlade.getCurrentUser().getAuthToken();
			if(isLogoutOrAuthCheck){
				urlConnection.setRequestProperty("CLEARBLADE-SYSTEMKEY", Util.getSystemKey());
				urlConnection.setRequestProperty("CLEARBLADE-SYSTEMSECRET", Util.getSystemSecret());
				urlConnection.setRequestProperty("ClearBlade-UserToken", userToken);
			}else if(isAuthOrReg){
				urlConnection.setRequestProperty("CLEARBLADE-SYSTEMKEY", Util.getSystemKey());
				urlConnection.setRequestProperty("CLEARBLADE-SYSTEMSECRET", Util.getSystemSecret());
			}else if(userToken != null){
				urlConnection.setRequestProperty("ClearBlade-UserToken", userToken);
			}
			urlConnection.setRequestProperty("Accept", "application/json");
			urlConnection.setRequestProperty("Accept-Charset", charset);
			
			OutputStream output = null;

			if (method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("PUT")) {
				urlConnection.setDoOutput(true);
				urlConnection.setFixedLengthStreamingMode(this.headers.getBody().length());
				//setDoOutput implicitly sets method to POST so re-set method for PUTs
				urlConnection.setRequestMethod(method);
				// Content-Type is necessary for POST and PUTS
				urlConnection.setRequestProperty("Content-Type", "application/json");
				
				output = urlConnection.getOutputStream();
				output.write(this.headers.getBody().getBytes(charset));
			}
			
			//TODO : what is the expense of getResponseCode(), getResponseMessage(), getInputStream()
			responseCode = urlConnection.getResponseCode();
			responseMessage = urlConnection.getResponseMessage();

			
			if(responseCode / 100 == 2) {  // If the response code is within 200 range success
				InputStream in = new BufferedInputStream(urlConnection.getInputStream());
				String json = readStream(in);
				result = new PlatformResponse<String>(err, json);
				Util.logger(TAG,method + " "+ responseCode + ":" + responseMessage, false);

			} else {	// else an Error Occurred 
				String errMessage;
				InputStream in = new BufferedInputStream(urlConnection.getErrorStream());
				errMessage = readStream(in);
				String errResp = responseCode + ":" + responseMessage + ":" + errMessage;
				Util.logger(TAG,errResp, true);
				err = true;
				result = new PlatformResponse<String>(err,errResp);
			}
		}catch(Exception e) {

			String caught = null;
			if(e instanceof MalformedURLException) {
				caught = "MalformedURLException: ";
			} else if (e instanceof UnsupportedEncodingException) {
				caught = "UnsupportedEncodingException: ";
			} else if (e instanceof ProtocolException) {
				caught = "ProtocolException: ";
			} else if (e instanceof IOException) {
				caught = "IOException: ";
			} else {
				caught = "Exception: ";
			}
			err = true;
			e.printStackTrace();
			result = new PlatformResponse<String>(err,"RequestEngine Caught " + caught + e.getMessage());
		}
		finally {
			// Make sure to close connection
			if (urlConnection!= null){
				urlConnection.disconnect();
			}
		}

		return result;


	}
	
	/**
	 * Internal function to identify if the hosted platform is running under SSL
	 * @return true if the platform uri contains https at the start
	 */
	private boolean isSSL() {
		if (isSSL == -1) {
			if (this.headers.getUri().startsWith("https")){
				// if starts with https, then SSL is in use 
				isSSL = 1;	
			}else {
				isSSL=0;
			}
		}
		if (isSSL==1){
			return true;
		}
		return false;
	}
	
	/**
	 * Internal function to hack around unsigned certificates on platform servers.  
	 * @param ctx
	 * @return SSLContext object that has a TrustManager that will accept all certificates
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	private SSLContext createUntrustedManager(SSLContext ctx) throws KeyManagementException, NoSuchAlgorithmException{
		ctx = SSLContext.getInstance("SSL");
		ctx.init(null, new TrustManager[] {
			
	       new X509TrustManager() {
	          public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	            return null;
	          }

			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
					throws CertificateException {
				System.out.println("AARON : checking for client trusted");
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
					throws CertificateException {
				System.out.println("AARON : checking for server trusted");
				
			}

	       }
		}, new java.security.SecureRandom());
		return ctx;
	}

}

