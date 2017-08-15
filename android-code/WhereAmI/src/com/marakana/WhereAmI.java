package com.marakana;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class WhereAmI extends Activity implements LocationListener { // <1>
  LocationManager locationManager; // <2>
  Geocoder geocoder; // <3>
  TextView textOut; // <4>

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    textOut = (TextView) findViewById(R.id.textOut);

    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE); // <5>
    geocoder = new Geocoder(this); // <6>

    // Initialize with the last known location
    Location lastLocation = locationManager
        .getLastKnownLocation(LocationManager.GPS_PROVIDER); // <7>
    if (lastLocation != null)
      onLocationChanged(lastLocation);
  }

  @Override
  protected void onResume() { // <8>
    super.onResume();
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
        10, this);
  }

  @Override
  protected void onPause() { // <9>
    super.onPause();
    locationManager.removeUpdates(this);
  }

  // Called when location has changed
  public void onLocationChanged(Location location) { // <10>
    String text = String.format(
        "Lat:\t %f\nLong:\t %f\nAlt:\t %f\nBearing:\t %f",
        location.getLatitude(), location.getLongitude(),
        location.getAltitude(), location.getBearing()); // <11>
    textOut.setText(text);

    // Perform geocoding for this location
    try {
      List<Address> addresses = geocoder.getFromLocation(
          location.getLatitude(), location.getLongitude(), 10); // <12>
      for (Address address : addresses) {
        textOut.append("\n" + address.getAddressLine(0)); // <13>
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Methods required by LocationListener <14>
  public void onProviderDisabled(String provider) {
  }

  public void onProviderEnabled(String provider) {
  }

  public void onStatusChanged(String provider, int status, Bundle extras) {
  }

}