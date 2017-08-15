package com.marakana.android.uiapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import dalvik.system.DexClassLoader;

/**
 * This activity is a proof of concept that one activity can load fragments from
 * another APK. This code depends on UIComponent project!
 * 
 * It loads a fragment from another APK via DexClassLaoder. It assumes that: 1.
 * The other APK is installed and has class name of
 * com.marakana.android.uicomponents.ComponentFragment, and 2. The other APK is
 * also located at /sdcard/UIComponents.apk.
 * 
 * The reason for 1. is to be able to load resources via PackageManager.
 * The reason for 2. is to be able to load classes via DexClassLoader.
 * 
 * @author marko
 * 
 */
public class UIAppActivity extends Activity {
	static final String TAG = UIAppActivity.class.getSimpleName();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		DexClassLoader cl = new DexClassLoader("/sdcard/UIComponents.apk",
				"/sdcard/", null, getClass().getClassLoader());
		Log.d(TAG, "resource: " + cl.getResource("res/string/app_name"));

		try {
			Class clazz = cl
					.loadClass("com.marakana.android.uicomponents.ComponentFragment");
			Log.d(TAG, "GOT CLASS: " + clazz.toString());

			FragmentTransaction t = getFragmentManager().beginTransaction();
			t.setCustomAnimations(R.anim.slide, R.anim.slide);
			t.add(R.id.container, (Fragment) clazz.newInstance());
			t.commit();
			Log.d(TAG, "DONE");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}
}