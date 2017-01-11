package com.tanlong.exercise.ui.view.customviewgroup;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.tanlong.exercise.util.LogTool;

/**
 * Created by 龙 on 2017/1/11.
 */

public class LeftDrawerLayout2 extends ViewGroup {
    private final String TAG = getClass().getSimpleName();

    private ViewDragHelper mDragerHelper;
    private View mMenuView;
    private View mContentView;

    public LeftDrawerLayout2(Context context) {
        super(context);
        initView();
    }

    public LeftDrawerLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LeftDrawerLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            LogTool.e(TAG, "onMeasure");
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            setMeasuredDimension(widthSize, heightSize);

            // 测量菜单View
            View leftMenuView = getChildAt(1);
            MarginLayoutParams lp = (MarginLayoutParams) leftMenuView.getLayoutParams();
            int menuWidthSpec = getChildMeasureSpec(widthMeasureSpec, lp.leftMargin + lp.rightMargin,
                    lp.width);
            int menuHeightSpec = getChildMeasureSpec(heightMeasureSpec, lp.topMargin + lp.bottomMargin,
                    lp.height);
            leftMenuView.measure(menuWidthSpec, menuHeightSpec);

            // 测量内容View
            View contentView = getChildAt(0);
            lp = (MarginLayoutParams) contentView.getLayoutParams();
            int contentWidthSpec = getChildMeasureSpec(widthMeasureSpec, lp.leftMargin + lp.rightMargin,
                    lp.width);
            int contentHeightSpec = getChildMeasureSpec(heightMeasureSpec, lp.topMargin + lp.bottomMargin,
                    lp.height);
            contentView.measure(contentWidthSpec, contentHeightSpec);

            mMenuView = leftMenuView;
            mContentView = contentView;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        LogTool.e(TAG, "onLayout");
        try {
            //布局内容
            MarginLayoutParams lp = (MarginLayoutParams) mMenuView.getLayoutParams();
            mMenuView.layout(lp.leftMargin, lp.topMargin, lp.leftMargin + mMenuView.getMeasuredWidth(),
                    lp.topMargin + mMenuView.getMeasuredHeight());

            //布局菜单
            lp = (MarginLayoutParams) mMenuView.getLayoutParams();
            int menuWidth = mMenuView.getMeasuredWidth();
            int menuLeft = -menuWidth;
            mMenuView.layout(menuLeft, lp.topMargin, menuLeft + menuWidth,
                    lp.topMargin + mMenuView.getMeasuredHeight());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LogTool.e(TAG, "onFinishInflate");
    }

    private void initView() {
        mDragerHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return mMenuView == child;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                // 水平移动距离 [-child.getWidth()，0]
                return Math.max(child.getWidth(), Math.min(left, 0));
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return child == mMenuView ? mMenuView.getWidth() : 0;
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                mDragerHelper.captureChildView(mMenuView, pointerId);
            }
        });
        mDragerHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }



    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragerHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragerHelper.processTouchEvent(event);
        return true;
    }
}
