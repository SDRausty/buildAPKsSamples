package com.marakana.template;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TemplateReceiver extends BroadcastReceiver {
  static final String TAG = "TemplateReceiver";
  
  @Override
  public void onReceive(Context context, Intent intent) {
    // Do something here...
    Log.d(TAG, "onReceived");
  }

}
