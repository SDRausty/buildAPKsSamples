package com.marakana.template;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class TemplateIntentService extends IntentService {
  static final String TAG = "TemplateIntentService";
  
  public TemplateIntentService() {
    super(TAG);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    Log.d(TAG, "onHandledIntent");
  }

}
