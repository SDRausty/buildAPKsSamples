package com.marakana;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.ImageView;

public class Rose extends ImageView { // <1>
  int direction = 0;

  public Rose(Context context) {  
    super(context);

    this.setImageResource(R.drawable.compassrose); // <2>
  }

  // Called when component is to be drawn
  @Override
  public void onDraw(Canvas canvas) { // <3>
    int height = this.getHeight();  // <4>
    int width = this.getWidth();

    canvas.rotate(direction, width / 2, height / 2); // <5>
    super.onDraw(canvas); // <6>
  }

  // Called by Compass to update the orientation
  public void setDirection(int direction) { // <7>
    this.direction = direction;
    this.invalidate(); // request to be redrawn <8>
  }

}
