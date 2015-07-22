package com.clearblade.platform.api;

import android.os.AsyncTask;

public class UserTask extends AsyncTask<RequestEngine, Void, PlatformResponse>{
	PlatformCallback _callback;
	
	public UserTask(PlatformCallback callback){
		_callback = callback;
	}
	
	@Override
	protected void onPostExecute(PlatformResponse result){
		super.onPostExecute(result);
		if(result.getError()){
			Util.logger("CBUserTask", "User call failed: " + result.getData(), true);
			_callback.error(new ClearBladeException("Call to user failed: " + result.getData()));
		}else{
			_callback.done((String)result.getData());
		}
	}
	
   @Override
   protected void onPreExecute() {
       super.onPreExecute();
   }
   
	@Override
	protected PlatformResponse doInBackground(RequestEngine... params) {
		return params[0].execute();
	} 
	
}
