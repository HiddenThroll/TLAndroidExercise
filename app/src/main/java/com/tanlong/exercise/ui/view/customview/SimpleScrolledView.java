package com.tanlong.exercise.ui.view.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class SimpleScrolledView extends View {
    private final String TAG = "SimpleScrolledView";

    private Paint mPaint;
    private int[] colors;

    private GestureDetector gestureDetector;

    public SimpleScrolledView(Context context) {
        this(context, null);
    }

    public SimpleScrolledView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleScrolledView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        colors = new int[]{ContextCompat.getColor(getContext(), android.R.color.holo_red_light),
                ContextCompat.getColor(getContext(), android.R.color.holo_green_light),
                ContextCompat.getColor(getContext(), android.R.color.holo_blue_light)};
        gestureDetector = new GestureDetector(getContext(), gestureListener);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画一个3*3的九宫格, 颜色依次使用RGB
        float itemWidth = getWidth() / 3.0f;
        float itemHeight = getHeight() / 3.0f;
        for (int i = 0; i < colors.length; i++) {
            for (int j = 0; j < colors.length; j++) {
                int tempColor = colors[(i + j) % colors.length];
                mPaint.setColor(tempColor);
                float left = j * itemWidth;
                float top = i * itemHeight;
                float right = left + itemWidth;
                float bottom = top + itemHeight;
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            scrollBy((int)distanceX, (int)distanceY);
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    };

}
