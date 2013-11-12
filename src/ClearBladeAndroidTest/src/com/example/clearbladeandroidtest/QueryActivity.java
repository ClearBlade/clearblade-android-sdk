package com.example.clearbladeandroidtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.clearblade.platform.api.ClearBladeException;
import com.clearblade.platform.api.DataCallback;
import com.clearblade.platform.api.Item;
import com.clearblade.platform.api.Query;

public class QueryActivity extends Activity {

	Query parentQuery = new Query();
	Query currentQuery = new Query();
	
	boolean hasOred = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.query, menu);
		return true;
	}
	
	public void runAsQuery(View view){
		TextView resultText= (TextView) findViewById (R.id.resultTextView);
		
		EditText collectionText= (EditText) findViewById (R.id.uriEditText);
		String collectionId = collectionText.getText().toString();
		//Collection collection = new Collection(collectionId);
		
		if(hasOred){
			parentQuery.or(currentQuery);
		}else{
			parentQuery = currentQuery;
		}
		parentQuery.setCollectionId(collectionId);
		String result="Loading";
		parentQuery.fetch(new DataCallback(){

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
					//super.error(exception);
					TextView resultText= (TextView) findViewById (R.id.resultTextView);
					resultText.setText("failure: "+exception.getMessage());
				}
				
				
				
			});//  collection.fetch(parentQuery);
		
		resultText.setText(result);
	}
	
	public void runRemove(View view){
		TextView resultText= (TextView) findViewById (R.id.resultTextView);
		
		EditText collectionText= (EditText) findViewById (R.id.uriEditText);
		String collectionId = collectionText.getText().toString();
		//Collection collection = new Collection(collectionId);
		
		if(hasOred){
			parentQuery.or(currentQuery);
		}else{
			parentQuery = currentQuery;
		}
		parentQuery.setCollectionId(collectionId);
		String result="Loading";
		resultText.setText(result);
		parentQuery.remove(new DataCallback(){

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
					//super.error(exception);
					TextView resultText= (TextView) findViewById (R.id.resultTextView);
					resultText.setText("failure: "+exception.getMessage());
				}
				
				
				
			});//  collection.fetch(parentQuery);
		
		
	}
	

	
	public void orClick(View view){
		if (!hasOred){
			parentQuery = currentQuery;
		}else{
			parentQuery.or(currentQuery);
		}
		currentQuery = new Query();
		hasOred = true;
		addCondition();
		
		updateQueryText();
	}
	
	public void andClick(View view){
		addCondition();
		
		updateQueryText();
	}
	
	public void updateQueryText() {
		TextView queryText = (TextView) findViewById(R.id.queryStringTextView);
		//if (hasOred)
		//	queryText.setText(parentQuery.queryAsJsonString()+"or"+currentQuery.queryAsJsonString());
		//else{
		//	queryText.setText(currentQuery.queryAsJsonString());
		//}
	}
	
	public void clearClick(View view){
		parentQuery=new Query();
		currentQuery = new Query();
		hasOred=false;
		TextView queryText = (TextView) findViewById(R.id.queryStringTextView);
		queryText.setText("");
	}
	
	private Query addCondition() {
		EditText fieldText = (EditText) findViewById(R.id.fieldEditText);
		EditText valueText = (EditText) findViewById(R.id.valueEditText);
		Spinner conditionSpinner = (Spinner) findViewById(R.id.conditionSpinner);
		
		String selectedItem = (String)conditionSpinner.getSelectedItem();
		if (selectedItem.equals("equalTo")){
			currentQuery.equalTo(fieldText.getText().toString(), valueText.getText().toString());
		}else if (selectedItem.equals("lessThan")){
			currentQuery.lessThan(fieldText.getText().toString(), valueText.getText().toString());
		}else if (selectedItem.equals("lessThanEqualTo")){
			currentQuery.lessThanEqualTo(fieldText.getText().toString(), valueText.getText().toString());
		}else if (selectedItem.equals("greaterThan")){
			currentQuery.greaterThan(fieldText.getText().toString(), valueText.getText().toString());
		}else if (selectedItem.equals("greaterThanEqualTo")){
			currentQuery.greaterThanEqualTo(fieldText.getText().toString(), valueText.getText().toString());
		}else if (selectedItem.equals("notEqual")){
			currentQuery.notEqual(fieldText.getText().toString(), valueText.getText().toString());
		}
		return currentQuery;
	}
	
	
}
