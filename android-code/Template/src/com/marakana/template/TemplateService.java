package com.marakana.template;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class TemplateService extends Service {
  static final String TAG = "TemplateService";
  
  @Override
  public void onCreate() {
    super.onCreate();
    Log.d(TAG, "onCreated");
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return super.onStartCommand(intent, flags, startId);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "onDestroyed");
  }

  @Override
  public IBinder onBind(Intent intent) { 
    return null;
  }

}
