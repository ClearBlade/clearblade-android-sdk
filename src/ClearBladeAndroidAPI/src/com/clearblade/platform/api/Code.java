package com.clearblade.platform.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class Code {
	
	private String serviceName;
	private JsonObject parameters; 
	
	private RequestEngine request;

	public Code(String serName, JsonObject params){
		serviceName = serName;
		parameters = params;
		request = new RequestEngine();
	}
	
	public void execute(final CodeCallback callback){
		RequestProperties headers = new RequestProperties.Builder().method("POST").endPoint("api/v/1/code/" +Util.getSystemKey() + "/" + serviceName).body(parameters).build();
		request.setHeaders(headers);
		DataTask asyncFetch = new DataTask(new PlatformCallback(this, callback) {
			
			@Override
			public void done(String response) {
				JsonObject codeResponse = convertJsonToJsonObject(response);
				if(codeResponse != null){
					callback.done(codeResponse);
				}else{
					callback.error(new ClearBladeException("Failed to parse code response"));
				}
			}
			
			@Override
			public void error(ClearBladeException exception) {
				callback.error(exception);
			}
		});
		asyncFetch.execute(request);
	}
	
	private JsonObject convertJsonToJsonObject(String json) {
		// parse json string in to JsonElement
		try {
			JsonElement toObject = new JsonParser().parse(json);
			return toObject.getAsJsonObject();
		}catch(JsonSyntaxException mfe){
			return null;
		}catch(IllegalStateException ise){
			return null;
		}
	}
	
	
}
