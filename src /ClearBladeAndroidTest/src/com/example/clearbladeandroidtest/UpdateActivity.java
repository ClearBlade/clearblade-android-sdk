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
import com.clearblade.platform.api.Query;

public class UpdateActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update, menu);
		return true;
	}
	
	public void updateColumn(View view){
		//TextView resultText= (TextView) findViewById (R.id.resultTextView);
		
		EditText collectionText= (EditText) findViewById (R.id.updateIdEditText);
		EditText itemText= (EditText) findViewById (R.id.updateItemIdEditText);
		EditText columnText= (EditText) findViewById (R.id.updateColEditText);
		EditText valueText= (EditText) findViewById (R.id.updateValEditText);
		
		Query query = new Query(collectionText.getText().toString());
		query.equalTo("itemId", itemText.getText().toString());
		query.addChange(columnText.getText().toString(), valueText.getText().toString());
		query.update( new DataCallback() {

			@Override
			public void done(Item[] response) {
				TextView resultText= (TextView) findViewById (R.id.resultUpdateTextView);
				String msg = "";
				for (int i=0;i<response.length;i++){
					msg = msg + response[i].toString()+",";
				}
				resultText.setText(msg);
			}

			@Override
			public void error(ClearBladeException exception) {
				// TODO Auto-generated method stub
				super.error(exception);
				TextView resultText= (TextView) findViewById (R.id.resultUpdateTextView);
				resultText.setText(exception.getMessage());
			}
			
		});
	}

}
