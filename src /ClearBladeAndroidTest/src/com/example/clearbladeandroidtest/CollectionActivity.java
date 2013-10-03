package com.example.clearbladeandroidtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.clearblade.platform.api.ClearBladeException;
import com.clearblade.platform.api.Collection;
import com.clearblade.platform.api.DataCallback;
import com.clearblade.platform.api.Item;
import com.clearblade.platform.api.Query;

public class CollectionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.collection, menu);
		return true;
	}
	
	public void fetchAll(View view){
		EditText collectionText= (EditText) findViewById (R.id.uriEditText);
		String collectionId = collectionText.getText().toString();
		Collection collection = new Collection(collectionId);
		collection.fetchAll(new DataCallback(){

			@Override
			public void done(Item[] response) {
				TextView resultText= (TextView) findViewById (R.id.resultTextView);
				String msg = "";
				for (int i=0;i<response.length;i++){
					msg = msg + response[i].toString()+",";
				}
				resultText.setText("rows:"+response.length+"    "+msg);
			}

			@Override
			public void error(ClearBladeException exception) {
				// TODO Auto-generated method stub
				TextView resultText= (TextView) findViewById (R.id.resultTextView);
				resultText.setText(exception.getMessage());
			}
			
			
		});
	
		
	}
	
	public void clearAll(View view){
		EditText collectionText= (EditText) findViewById (R.id.uriEditText);
		String collectionId = collectionText.getText().toString();
		Collection collection = new Collection(collectionId);
		collection.clear(new DataCallback(){

			@Override
			public void done(Item[] response) {
				TextView resultText= (TextView) findViewById (R.id.resultTextView);
				String msg = "";
				for (int i=0;i<response.length;i++){
					msg = msg + response[i].toString()+",";
				}
				resultText.setText(msg);
			}

			@Override
			public void error(ClearBladeException exception) {
				// TODO Auto-generated method stub
				TextView resultText= (TextView) findViewById (R.id.resultTextView);
				resultText.setText(exception.getMessage());
			}
			
			
		});

	}

}
