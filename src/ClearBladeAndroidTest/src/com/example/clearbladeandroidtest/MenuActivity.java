package com.example.clearbladeandroidtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	public void testCollections(View view){
		Intent intent = new Intent(this, CollectionActivity.class);
		startActivity(intent);	
	}
	
	public void testQuery(View view){
		Intent intent = new Intent(this, QueryActivity.class);
		startActivity(intent);
	}
	
	public void createItemClick(View view){
		Intent intent = new Intent(this, CreateItemActivity.class);
		startActivity(intent);
	}
	
	public void updateItemClick(View view){
		Intent intent = new Intent(this, UpdateActivity.class);
		startActivity(intent);
	}

	public void deleteItemClick(View view){
		Intent intent = new Intent(this, DeleteActivity.class);
		startActivity(intent);
	}
	
	public void testMessaging(View view){
		Intent intent = new Intent(this, MessagingActivity.class);
		startActivity(intent);
	}

}
