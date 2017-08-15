package com.marakana.android.toddlerlockdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActivateToddlerLockActivity extends Activity {
	static final Intent homeIntent = new Intent(Intent.ACTION_MAIN)
			.addCategory(Intent.CATEGORY_HOME);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activate);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	public void onClick(View v) {
		startActivity( homeIntent );
	}

}
