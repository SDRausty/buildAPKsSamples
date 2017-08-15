package com.marakana;

import android.app.Activity;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Bubble extends Activity implements SensorListener {
	SensorManager sensorManager;
	static final int sensor = SensorManager.SENSOR_ORIENTATION;
	BubbleView bubbleView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set full screen view
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		bubbleView = new BubbleView(this);

		setContentView(bubbleView);

		// get sensor manager
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	}

	// register to listen to sensors
	@Override
	public void onResume() {
		super.onResume();
		sensorManager.registerListener(this, sensor);
	}

	// unregister
	@Override
	public void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
	}

	// Ignore for now
	public void onAccuracyChanged(int sensor, int accuracy) {
	}

	// Listen to sensor and provide output
	public void onSensorChanged(int sensor, float[] values) {

		int x = (int) values[2];
		int y = (int) values[1];

		bubbleView.update(x, y);
	}
}