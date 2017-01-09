package com.tanlong.exercise.ui.view.customviewgroup;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.tanlong.exercise.util.LogTool;

/**
 * 自定义拖动布局, 主要是学习ViewDragHelper的使用
 * Created by 龙 on 2016/6/29.
 */
public class VDHLayout extends LinearLayout {
    private final String TAG = "VDHLayout";
    private ViewDragHelper mDragger;
    /**
     * 可拖动View
     */
    private View mDragView;
    /**
     * 拖动后自动返回View
     */
    private View mAutoBackView;
    /**
     * 边缘操作View
     */
    private View mEdgeTrackerView;
    /**
     * mAutoBackView的原始位置
     */
    private int mOriginX, mOriginY;

    public VDHLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        /**
         * 第二个参数sensitivity决定了mTouchSlop的大小，sensitivity值越大，mTouchSlop值越小，反应越灵敏
         */
        mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
                    @Override
                    public boolean tryCaptureView(View child, int pointerId) {
                        LogTool.e(TAG, "tryCaptureView");
                        /**
                         * 1. 返回ture则表示可以捕获该view(即该View可以拖动)
                         * 2. 只允许mDragView 和 mAutoBackView 可拖动
                         */
                        return mDragView == child || mAutoBackView == child;
                    }

                    @Override
                    public int clampViewPositionHorizontal(View child, int left, int dx) {
                        /**
                         * 对child移动的边界进行控制，left为水平方向期望移动到的位置, 返回值为子View在最终位置时的left值
                         */
                        int leftBounds = getPaddingLeft();
                        int rightBounds = getWidth() - getPaddingRight() - child.getWidth();
                        // 令newLeft的取值范围为(leftBounds, rightBounds)
                        int newLeft = Math.min(Math.max(left, leftBounds), rightBounds);
                        return newLeft;
                    }

                    @Override
                    public int clampViewPositionVertical(View child, int top, int dy) {
                        /**
                         * 对child移动的边界进行控制，top为垂直方向即将移动到的位置
                         */
                        int topBounds = getPaddingTop();
                        int bottomBounds = getHeight() - getPaddingBottom() - child.getHeight();
                        int newTop = Math.min(Math.max(top, topBounds), bottomBounds);
                        return newTop;
                    }

                    //在边界拖动时回调
                    @Override
                    public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                        LogTool.e(TAG, "onEdgeDragStarted");
                        // 通过captureChildView对View进行捕获，可以绕过tryCaptureView
                        mDragger.captureChildView(mEdgeTrackerView, pointerId);
                    }

                    //手指释放的时候回调
                    @Override
                    public void onViewReleased(View releasedChild, float xvel, float yvel) {
                        LogTool.e(TAG, "onViewReleased");
                        //让mAutoBackView手指释放时可以自动回去
                        if (releasedChild == mAutoBackView) {
                            mDragger.settleCapturedViewAt(mOriginX, mOriginY);// 让捕获到的View到指定的位置
                            invalidate();
                        }
                    }

                    @Override
                    public int getViewHorizontalDragRange(View child) {
                        // 返回值大于0时, 水平方向可以拖动
                        return getMeasuredWidth() - child.getMeasuredWidth();
                    }

                    @Override
                    public int getViewVerticalDragRange(View child) {
                        // 返回值大于0时, 垂直方向可以拖动
                        return getMeasuredHeight() - child.getMeasuredHeight();
                    }
                }
        );
        // 让ViewDragHelper可以响应左边缘拖动
        mDragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        LogTool.e(TAG, "让ViewDragger可以响应左边缘拖动");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LogTool.e(TAG, "onFinishInflate");

        mDragView = getChildAt(0);
        mAutoBackView = getChildAt(1);
        mEdgeTrackerView = getChildAt(2);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        LogTool.e(TAG, "onLayout");

        mOriginX = mAutoBackView.getLeft();
        mOriginY = mAutoBackView.getTop();
    }

    @Override
    public void computeScroll() {
//        LogTool.e(TAG, "computeScroll");
        if (mDragger.continueSettling(true)) {// 是否还在Settling捕获View的过程中
            invalidate();
        }
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
