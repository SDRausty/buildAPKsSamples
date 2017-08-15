package com.marakana;

import android.app.Activity;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

// implement SensorListener
public class Compass extends Activity implements SensorEventListener { // <1>
  SensorManager sensorManager; // <2>
  Sensor sensor;
  Rose rose;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) { // <3>
    super.onCreate(savedInstanceState);

    // Set full screen view <4>
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    requestWindowFeature(Window.FEATURE_NO_TITLE);

    // Create new instance of custom Rose and set it on the screen
    rose = new Rose(this); // <5>
    setContentView(rose); // <6>

    // Get sensor and sensor manager
    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); // <7>
    sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION); // <8>

    Log.d("Compass", "onCreated");
  }

  // Register to listen to sensors
  @Override
  public void onResume() {
    super.onResume();
    sensorManager.registerListener(this, sensor,
        SensorManager.SENSOR_DELAY_NORMAL); // <9>
  }

  // Unregister the sensor listener
  @Override
  public void onPause() {
    super.onPause();
    sensorManager.unregisterListener(this); // <10>
  }

  // Ignore accuracy changes
  public void onAccuracyChanged(Sensor sensor, int accuracy) { // <11>
  }

  // Listen to sensor and provide output
  public void onSensorChanged(SensorEvent event) { // <12>
    int orientation = (int) event.values[0]; // <13>
    Log.d("Compass", "Got sensor event: " + event.values[0]);
    rose.setDirection(orientation); // <14>
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
  }

}
