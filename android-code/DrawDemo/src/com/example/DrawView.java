package com.example;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class DrawView extends View implements OnTouchListener {
	Paint paint = new Paint();
	
	float x=-10; // so initial point is off the screen 
	float y=-10;
	float oldX=-1;
	float oldY=-1;

	public DrawView(Context context) {
		super(context);
		setFocusable(true);
		setFocusableInTouchMode(true);

		this.setOnTouchListener(this);
		this.setBackgroundResource(R.color.background);

		paint.setColor( Color.argb(255,255,255,255));
		paint.setAntiAlias(false);
	}

	/** Called by parent every time this view needs to be drawn */
	@Override
	public void onDraw(Canvas canvas) {
			canvas.drawLine(oldX, oldY, x, y, paint);
	}

	/** Called on touch */
	public boolean onTouch(View view, MotionEvent event) {
	  oldX = x;
	  oldY = y;
		x = event.getX();
		y = event.getY();
    int left = (int)Math.min(x, oldX); 
		int right = (int)Math.max(x, oldX);
		int top = (int)Math.min(y, oldY);
		int bottom = (int)Math.max(y, oldY);
    if(oldX<0) return false;

		invalidate(left,top,right,bottom); // redraw just the rectangle
		return true;
	}
}
