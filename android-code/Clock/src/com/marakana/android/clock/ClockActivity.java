package com.marakana.android.clock;

import java.util.Date;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class ClockActivity extends Activity {
    static final String TAG = "ClockActivity";
    TickReceiver receiver;
    IntentFilter filter;
    ImageView h1View, h2View, m1View, m2View;
    
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    
        h1View = (ImageView)findViewById(R.id.h1);
        h2View = (ImageView)findViewById(R.id.h2);
        m1View = (ImageView)findViewById(R.id.m1);
        m2View = (ImageView)findViewById(R.id.m2);
        
        setTime();
        
        receiver = new TickReceiver();
        filter = new IntentFilter(Intent.ACTION_TIME_TICK);        
    }

    @Override
    protected void onStart() {
      super.onStart();
      registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop() {
      super.onStop();
      unregisterReceiver(receiver);
    }
    
    
    public class TickReceiver extends BroadcastReceiver {
      static final String TAG = "TickReceiver";
      
      @Override
      public void onReceive(Context arg0, Intent arg1) {
     
        setTime();
        Log.d(TAG, "tick...");
      }
    }
    
    private void setTime() {
      Date now = new Date();
      int hours = now.getHours();
      hours = (hours>12)?hours-12:hours;
      int minutes  = now.getMinutes();
      int h1 = hours/10;
      int h2 = hours - h1*10;
      int m1 = minutes/10;
      int m2 = minutes -  m1*10;
      
      Log.d(TAG, String.format("%d:%d -> %d %d: %d %d",hours, minutes, h1, h2, m1, m2));
      
      h1View.setImageResource( numToRes(h1) );
      h2View.setImageResource( numToRes(h2) );
      m1View.setImageResource( numToRes(m1) );
      m2View.setImageResource( numToRes(m2) );
    }
    
    private int numToRes( int num ) {
      switch (num) {
      case 1: return R.drawable.number1;
      case 2: return R.drawable.number2;
      case 3: return R.drawable.number3;
      case 4: return R.drawable.number4;
      case 5: return R.drawable.number5;
      case 6: return R.drawable.number6;
      case 7: return R.drawable.number7;
      case 8: return R.drawable.number8;
      case 9: return R.drawable.number9;
      case 0: return R.drawable.number0;
      default: return R.drawable.icon;
      }
    }
    
}