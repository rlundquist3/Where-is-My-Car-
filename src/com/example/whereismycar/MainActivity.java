package com.example.whereismycar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity {
	
	GPSTracker gps;
	GoogleMap map;
	File file;
	String fileName = "storedLocFile";
	double carLatitude, carLongitude;
	LatLng currentLoc, carLoc;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		gps = new GPSTracker(this);
		currentLoc = new LatLng(gps.getLatitude(), gps.getLongitude());
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 15));
	}
	
	public void storeLocation(View view) {
		
		if (gps.canGetLocation) {
			file = new File(getFilesDir(), fileName);
			PrintWriter out = null;
			try {
				out = new PrintWriter(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			carLatitude = gps.getLatitude();
			carLongitude = gps.getLongitude();
			out.println(carLatitude);
			out.println(carLongitude);
			out.close();
			Toast.makeText(getApplicationContext(), "Got it:\n" + carLatitude + "\n" + carLongitude + "\nSaved", Toast.LENGTH_LONG).show();
			LatLng carLoc = new LatLng(carLatitude, carLongitude);
			map.addMarker(new MarkerOptions().position(carLoc).icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(carLoc, 20));
		}
		else
			gps.showSettingsAlert();
	}
	
	public void readLocation(View view) {
		file = new File(getFilesDir(), fileName);
		try {
			Scanner in = new Scanner(file);
			carLatitude = Double.parseDouble(in.nextLine());
			carLongitude = Double.parseDouble(in.nextLine());
			in.close();
			Toast.makeText(getApplicationContext(), "Found it:\n" + carLatitude + "\n" + carLongitude, Toast.LENGTH_LONG).show();
			
			carLoc = new LatLng(carLatitude, carLongitude);
			map.addMarker(new MarkerOptions().position(carLoc).icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
			
			currentLoc = new LatLng(gps.getLatitude(), gps.getLongitude());
			map.addMarker(new MarkerOptions().position(currentLoc).icon(BitmapDescriptorFactory.fromResource(R.drawable.man)));
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 20));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "No location saved: Car lost forever!", Toast.LENGTH_LONG).show();
		}
	}
}
