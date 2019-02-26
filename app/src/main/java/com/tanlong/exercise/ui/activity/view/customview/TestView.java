package com.tanlong.exercise.ui.activity.view.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.tanlong.exercise.R;

public class TestView extends View {

    private final String TAG = "TestView";

    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.e(TAG, "onDraw");
        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.color_86d0ab));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.e(TAG, "dispatchTouchEvent begin ");
        boolean result = super.dispatchTouchEvent(ev);
//        Log.e(TAG, "dispatchTouchEvent return " + result);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);
//        Log.e(TAG, "onTouchEvent " + result);
        return result;
    }
}
