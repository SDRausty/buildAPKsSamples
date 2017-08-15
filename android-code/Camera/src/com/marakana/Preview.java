package com.marakana;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class Preview extends SurfaceView implements SurfaceHolder.Callback { // <1>
  private static final String TAG = "Preview";

  SurfaceHolder mHolder;  // <2>
  public Camera camera; // <3>

  Preview(Context context) {
    super(context);

    // Install a SurfaceHolder.Callback so we get notified when the
    // underlying surface is created and destroyed.
    mHolder = getHolder();  // <4>
    mHolder.addCallback(this);  // <5>
    mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); // <6>
  }

  // Called once the holder is ready
  public void surfaceCreated(SurfaceHolder holder) {  // <7>
    // The Surface has been created, acquire the camera and tell it where
    // to draw.
    camera = Camera.open(); // <8>
    try {
      camera.setPreviewDisplay(holder);  // <9>

      camera.setPreviewCallback(new PreviewCallback() { // <10>
        // Called for each frame previewed
        public void onPreviewFrame(byte[] data, Camera camera) {  // <11>
          Log.d(TAG, "onPreviewFrame called at: " + System.currentTimeMillis());
          Preview.this.invalidate();  // <12>
        }
      });
    } catch (IOException e) { // <13>
      e.printStackTrace();
    }
  }

  // Called when the holder is destroyed
  public void surfaceDestroyed(SurfaceHolder holder) {  // <14>
    camera.stopPreview();
    camera = null;
  }

  // Called when holder has changed
  public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) { // <15>
    camera.startPreview();
  }

}