package com.example.audio;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class AudioDemo extends Activity {
  MediaPlayer player;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    player =  MediaPlayer.create(this, R.raw.braincandy);
  }
  
  public void startPlaying(View v) {
    player.start();
  }
  
  public void stopPlaying(View v) {
    player.pause();
  }
}