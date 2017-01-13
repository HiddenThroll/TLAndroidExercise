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
        LogTool.e(TAG, "onMeasure");
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);

        /**
         * getChildMeasureSpec() 在measureChildren中最难的部分：找出传递给child的MeasureSpec。
         * 目的是结合父view的MeasureSpec与子view的LayoutParams信息去找到最好的结果
         * （也就是说子view的确切大小由两方面共同决定：1.父view的MeasureSpec 2.子view的LayoutParams属性）
         *
         * @param spec 父view的详细测量值(MeasureSpec)
         * @param padding view当前尺寸的的内边距和外边距(padding,margin)
         * @param childDimension child在当前尺寸下的布局参数宽高值(LayoutParam.width,height)
         */

        // 测量菜单View
        mMenuView = getChildAt(1);
        MarginLayoutParams lp = (MarginLayoutParams) mMenuView.getLayoutParams();
        int menuWidthSpec = getChildMeasureSpec(widthMeasureSpec, lp.leftMargin + lp.rightMargin,
                lp.width);
        int menuHeightSpec = getChildMeasureSpec(heightMeasureSpec, lp.topMargin + lp.bottomMargin,
                lp.height);
        mMenuView.measure(menuWidthSpec, menuHeightSpec);

        // 测量内容View
        mContentView = getChildAt(0);
        lp = (MarginLayoutParams) mContentView.getLayoutParams();
        int contentWidthSpec = getChildMeasureSpec(widthMeasureSpec, lp.leftMargin + lp.rightMargin,
                lp.width);
        int contentHeightSpec = getChildMeasureSpec(heightMeasureSpec, lp.topMargin + lp.bottomMargin,
                lp.height);
        mContentView.measure(contentWidthSpec, contentHeightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        LogTool.e(TAG, "onLayout");
        //布局内容
        MarginLayoutParams lp = (MarginLayoutParams) mContentView.getLayoutParams();
        mContentView.layout(lp.leftMargin, lp.topMargin, lp.leftMargin + mContentView.getMeasuredWidth(),
                lp.topMargin + mContentView.getMeasuredHeight());

        //布局菜单
        lp = (MarginLayoutParams) mMenuView.getLayoutParams();
        int menuWidth = mMenuView.getMeasuredWidth();
        int menuLeft = -menuWidth;
        LogTool.e(TAG, "menuLeft is " + menuLeft);
        mMenuView.layout(menuLeft, lp.topMargin, menuLeft + menuWidth,
                lp.topMargin + mMenuView.getMeasuredHeight());
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
                int result = Math.max(-child.getWidth(), Math.min(left, 0));
                return result;
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return child == mMenuView ? mMenuView.getWidth() : 0;
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                // 因为开始的时候mMenuView不可见，通过captureChildView对mMenuView进行捕获
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

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new MarginLayoutParams(p);
    }
}
