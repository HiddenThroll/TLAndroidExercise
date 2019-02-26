package com.tanlong.exercise.ui.view.customviewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class TestViewGroup extends ViewGroup {

    public final String TAG = "TestViewGroup";

    public TestViewGroup(Context context) {
        super(context);
    }

    public TestViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        Log.e(TAG, "onLayout");
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {
                LayoutParams params = childView.getLayoutParams();
                int right = params.width;
                int bottom = params.height;
                childView.layout(0, 0, right, bottom);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.e(TAG, "dispatchTouchEvent begin ");
        boolean result = super.dispatchTouchEvent(ev);
//        Log.e(TAG, "dispatchTouchEvent return " + result);
        return result;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = super.onInterceptTouchEvent(ev);
//        Log.e(TAG, "onInterceptTouchEvent return " + result);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);
//        Log.e(TAG, "onTouchEvent return " + result);
        return result;
    }
}
