package com.tanlong.exercise.ui.activity.view.customviewgroup;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;

import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityTestViewgroupBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;

/**
 * @author 龙
 */
public class TestViewGroupActivity extends BaseActivity {
    private final String TAG = "TestViewGroupActivity";
    private ActivityTestViewgroupBinding binding;
    private GestureDetectorCompat gestureDetectorCompat;

    private VelocityTracker velocityTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_test_viewgroup);
        binding.setActivity(this);

        gestureDetectorCompat = new GestureDetectorCompat(this, simpleOnGestureListener);
//        gestureDetectorCompat.setOnDoubleTapListener(doubleTapListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        if (gestureDetectorCompat.onTouchEvent(event)) {
//            return true;
//        }
        int index = event.getActionIndex();
        int pointerId = event.getPointerId(index);
        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (velocityTracker == null) {
                    velocityTracker = VelocityTracker.obtain();
                } else {
                    velocityTracker.clear();
                }
                velocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_MOVE:
                velocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_UP:
                velocityTracker.addMovement(event);
                velocityTracker.computeCurrentVelocity(1000);
                Log.e(TAG, "X Velocity with id " + velocityTracker.getXVelocity(pointerId));
                Log.e(TAG, "Y Velocity with id " + velocityTracker.getYVelocity(pointerId));
                break;
            case MotionEvent.ACTION_CANCEL:
                velocityTracker.recycle();
                break;
            default:
                break;
        }

        return gestureDetectorCompat.onTouchEvent(event);

    }

    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            //手指按下
            Log.e(TAG, "onDown");
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            //手指按下后没有移动或抬起
            Log.e(TAG, "onShowPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.e(TAG, "onSingleTapUp");
            //点击抬起事件,无法区分是单击还是双击
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.e(TAG, "onScroll");
            //滑动事件
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.e(TAG, "onLongPress");
            //长按事件
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.e(TAG, "onFling");
            //抛事件, 即down -> move -> up, 按下后快速滑动并抬起
            return true;
        }
    };

    private GestureDetector.OnDoubleTapListener doubleTapListener = new GestureDetector.OnDoubleTapListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.e(TAG, "onSingleTapConfirmed");
            //单击事件
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.e(TAG, "onDoubleTap");
            //双击事件
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.e(TAG, "onDoubleTapEvent " + e.toString());
            //发生双击事件时触发,包括down, move, and up 事件
            return true;
        }
    };

    private GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            Log.e(TAG, "onDown");
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.e(TAG, "onFling");
            return true;
        }
    };
}
