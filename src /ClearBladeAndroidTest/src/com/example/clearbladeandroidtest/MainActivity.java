package com.example.clearbladeandroidtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.clearblade.platform.api.ClearBlade;

public class MainActivity extends Activity {
	public final static String APIKEY = "com.clearblade.platform.api.key";
	public final static String APISECRET = "com.clearblade.platform.api.secret";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void initializePlatform(View view){
		Intent intent = new Intent(this, MenuActivity.class);
		
		EditText uriText= (EditText) findViewById(R.id.uriEditText);
		String platformURI = uriText.getText().toString();
		
		CheckBox trustedCheckBox = (CheckBox) findViewById(R.id.untrustedCheckBox);
		
		EditText secretText= (EditText) findViewById (R.id.secretEditText);
		String secret = secretText.getText().toString();
		
		EditText keyText =(EditText) findViewById (R.id.keyEditText);
		String key = keyText.getText().toString();
		
		intent.putExtra(APIKEY, key);
		intent.putExtra(APISECRET, secret);
		ClearBlade.initialize(key, secret);
		ClearBlade.setUri(platformURI);
		ClearBlade.setAllowUntrusted(trustedCheckBox.isChecked());
		startActivity(intent);
		
	}

}
