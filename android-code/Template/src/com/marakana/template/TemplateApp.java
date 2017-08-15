package com.marakana.template;

import android.app.Application;
import android.util.Log;

public class TemplateApp extends Application {
  static final String TAG = "TemplateApp";

  @Override
  public void onCreate() {
    super.onCreate();
    
    Log.d(TAG, "onCreated");
  }
  
}
