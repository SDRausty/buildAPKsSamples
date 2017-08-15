package com.marakana.android.toddlerlockdemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class ToddlerLockDemoActivity extends Activity {
	static final String TAG = "ToddlerLockDemoActivity";
	Toast toastLocked, toastUnlocked;
	int state = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pm = getPackageManager();

		// Go full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Load up UI and initialize widgets
		setContentView(R.layout.main);
		toastLocked = Toast.makeText(this, R.string.locked, Toast.LENGTH_LONG);
		toastUnlocked = Toast.makeText(this, R.string.unlocked,
				Toast.LENGTH_SHORT);
	}

	public void onClick(View v) {

		// Unlock state machine
		switch (v.getId()) {
		case R.id.button1:
			state = (state == 0) ? 1 : 0;
			break;
		case R.id.button2:
			state = (state == 1) ? 2 : 0;
			break;
		case R.id.button3:
			state = (state == 2) ? 3 : 0;
			break;
		}

		// Report results
		if (state == 3) {
			this.unlock();
		} else if (state == 0) {
			this.lock();
		}

		Log.d(TAG, "onClick with state: " + state);
	}

	/** Locks the device, preventing use of home and back buttons. */
	private void lock() {

		toastLocked.show();
		Log.d(TAG, "Device locked");
	}

	static final ComponentName COMPONENT = new ComponentName(
			"com.marakana.android.toddlerlockdemo", "ToddlerLockDemoActivity");
	PackageManager pm;

	/** Unlocks the device. */
	private void unlock() {
		pm.setComponentEnabledSetting(COMPONENT, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
				PackageManager.DONT_KILL_APP);

		startActivity(ActivateToddlerLockActivity.homeIntent);
		
		toastUnlocked.show();
		Log.d(TAG, "Device unlocked");
	}
}