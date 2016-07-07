package com.tanlong.exercise.ui.view.customviewgroup;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.tanlong.exercise.util.LogTool;

/**
 * Created by 龙 on 2016/6/29.
 */
public class VDHLayout extends LinearLayout {
    private final String TAG = "VDHLayout";
    private ViewDragHelper mDragger;

    public VDHLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        /**
         * 第二个参数sensitivity决定了mTouchSlop的大小，sensitivity值越大，mTouchSlop值越小，反应越灵敏
         */
        mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                /**
                 * 返回ture则表示可以捕获该view(即该View可以拖动)
                 */
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                /**
                 * 对child移动的边界进行控制，left为即将移动到的位置
                 */
                int leftBounds = getPaddingLeft();
                int rightBounds = getWidth() - getPaddingRight() - child.getWidth();
                // 令newLeft的取值范围为(leftBounds, rightBounds)
                int newLeft = Math.min(Math.max(left, leftBounds), rightBounds);
                return newLeft;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                int topBounds = getPaddingTop();
                int bottomBounds = getHeight() - getPaddingBottom() - child.getHeight();
                int newTop = Math.min(Math.max(top, topBounds), bottomBounds);
                return newTop;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        /**
         *  使用shouldInterceptTouchEvent()决定我们是否应该拦截当前的事件
         */
        return mDragger.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**
         *  使用processTouchEvent()处理事件
         */
        mDragger.processTouchEvent(event);
        return true;
    }
}
