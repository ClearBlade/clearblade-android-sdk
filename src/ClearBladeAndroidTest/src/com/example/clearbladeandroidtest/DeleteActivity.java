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

public class DeleteActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delete);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.delete, menu);
		return true;
	}
	
	public void deleteItem(View view){
		EditText deleteIdText= (EditText) findViewById (R.id.deleteIeEditText);
		Item item = new Item("523876618ab3a371add29eef");
		
		item.set("itemId", deleteIdText.getText().toString());
		
		item.destroy(new DataCallback(){

			@Override
			public void done(Item[] response) {
				TextView resultText= (TextView) findViewById (R.id.deleteResultTextView);
				String msg = "";
				for (int i=0;i<response.length;i++){
					msg = msg + response[i].toString()+",";
				}
				resultText.setText(msg);
			}

			@Override
			public void error(ClearBladeException exception) {
				//super.error(exception);
				TextView resultText= (TextView) findViewById (R.id.deleteResultTextView);
				resultText.setText("failure: "+exception.getMessage());
			}
			
			
			
		});
	}

}
