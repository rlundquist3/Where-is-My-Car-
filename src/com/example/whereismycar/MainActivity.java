package com.example.whereismycar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	GPSTracker gps;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void showLocation(View view) {
		gps = new GPSTracker(this);
		
		if (gps.canGetLocation) {
			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			
			Toast.makeText(getApplicationContext(), "Current Location:\n" + latitude + "\n" + longitude, Toast.LENGTH_LONG).show();
		}
		else
			gps.showSettingsAlert();
	}
}
