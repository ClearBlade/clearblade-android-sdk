package com.example.clearbladeandroidtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.clearblade.platform.api.ClearBladeException;
import com.clearblade.platform.api.DataCallback;
import com.clearblade.platform.api.Item;

public class CreateItemActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_item, menu);
		return true;
	}
	
	public void createItem(View view){
		EditText firstNameText= (EditText) findViewById (R.id.firstEditText);
		EditText lastNameText= (EditText) findViewById (R.id.lastEditText);
		EditText ageText= (EditText) findViewById (R.id.ageEditText);
		
		
		Item item = new Item("523876618ab3a371add29eef");
		item.set("firstName", firstNameText.getText().toString());
		item.set("lastName", lastNameText.getText().toString());
		item.set("age", ageText.getText().toString());
			item.save(new DataCallback(){

				@Override
				public void done(Item[] response) {
					TextView resultText= (TextView) findViewById (R.id.resultCreateTextView);
					String msg = "";
					for (int i=0;i<response.length;i++){
						msg = msg + response[i].toString()+",";
					}
					resultText.setText(msg);
				}
				public void error(ClearBladeException exception){
					TextView resultText= (TextView) findViewById (R.id.resultCreateTextView);
					resultText.setText("failure: "+exception.getMessage());
				}
				
			});
		
	}

}
