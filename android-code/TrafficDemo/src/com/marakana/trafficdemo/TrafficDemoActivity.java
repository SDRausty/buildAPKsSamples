package com.marakana.trafficdemo;

import android.app.Activity;
import android.net.TrafficStats;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TrafficDemoActivity extends Activity {
	TrafficStats stats;
	TextView out;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		out = (TextView) findViewById(R.id.out);

		stats = new TrafficStats();
	}

	public void onClick(View v) {
		String currentStats = String.format("\nRx: %d\nTx: %d",
				stats.getTotalRxBytes(), stats.getTotalTxBytes());
		out.append(currentStats);
	}
}