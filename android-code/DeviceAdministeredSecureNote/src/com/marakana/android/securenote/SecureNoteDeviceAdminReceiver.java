package com.marakana.android.securenote;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class SecureNoteDeviceAdminReceiver extends DeviceAdminReceiver {
	private static final String TAG = "SecureNoteDeviceAdminReceiver";

	private void showToast(Context context, CharSequence msg) {
		Log.d(TAG, msg.toString());
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onEnabled(Context context, Intent intent) {
		showToast(context, "Enabled");
	}

	@Override
	public CharSequence onDisableRequested(Context context, Intent intent) {
		return context.getText(R.string.device_admin_disable_confirmation);
	}

	@Override
	public void onDisabled(Context context, Intent intent) {
		showToast(context, "Disabled");
	}

	@Override
	public void onPasswordChanged(Context context, Intent intent) {
		showToast(context, "Password changed");
	}

	@Override
	public void onPasswordFailed(Context context, Intent intent) {
		showToast(context, "Password failed");
	}

	@Override
	public void onPasswordSucceeded(Context context, Intent intent) {
		showToast(context, "Password succeeded");
	}
}
