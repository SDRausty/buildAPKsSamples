package com.marakana;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class BubbleView extends View {
	int left, top;
	Bitmap bmp;

	public BubbleView(Context context) {
		super(context);

		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		this.setLayoutParams(params);

		bmp = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ball);
		this.setBackgroundColor(Color.WHITE);
	}

	// Called when component is to be drawn
	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawBitmap(bmp, left, top, null);
		Log.d("Bubble", "left=" + left + " top=" + top);
		super.onDraw(canvas);
	}

	public void update(int x, int y) {
		left = (x + 90) * (getWidth() - 50) / 180;
		top = (-y + 90) * (getHeight() - 50) / 180;
		invalidate();
	}
}
