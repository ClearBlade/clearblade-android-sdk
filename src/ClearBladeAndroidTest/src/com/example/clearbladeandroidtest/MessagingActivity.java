package com.example.clearbladeandroidtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.clearblade.platform.api.ClearBladeException;
import com.clearblade.platform.api.Message;
import com.clearblade.platform.api.MessageCallback;

public class MessagingActivity extends Activity {

	Message message;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messaging);
		if (message== null){
			message = new Message(this);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.messaging, menu);
		return true;
	}
	public void toggleSubscription(View view) {
		
		ToggleButton toggleButton = (ToggleButton)findViewById(R.id.subToggleButton);
		EditText topicText = (EditText) findViewById(R.id.topicEditText);
		
		if (toggleButton.isChecked()) {
			
			
			message.subscribe(topicText.getText().toString(),
					new MessageCallback() {
	
						@Override
						public void done(String topic, String message) {
							TextView resultText = (TextView) findViewById(R.id.messageTextView);
							resultText.setText(message);
						}
	
						@Override
						public void error(ClearBladeException exception) {
							super.error(exception);
						}
	
					}
			);
		}else{
			// we should unsubscribe here
			message.unsubscribe(topicText.getText().toString());
		}

	}
	
	public void publishClick(View view){
		EditText publishText = (EditText) findViewById(R.id.publishEditText);
		EditText topicText = (EditText) findViewById(R.id.topicEditText);
		
		message.publish(topicText.getText().toString(), publishText.getText().toString());
	}

	protected void onStart() {


		super.onStart();
	}

	@Override
		protected void onStop() {
			super.onStop();
		}

}
