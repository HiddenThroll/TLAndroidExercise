package com.tanlong.exercise.ui.view.customviewgroup;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.tanlong.exercise.util.LogTool;

/**
 * 左抽屉菜单，主要是练习ViewDragHelper的使用，项目开发用到侧滑菜单还是使用DrawerLayout
 * Created by 龙 on 2016/8/4.
 */
public class LeftDrawerLayout extends ViewGroup {
    private final String TAG = "LeftDrawerLayout";
    /**
     * drawer离父容器右边的最小外边距,单位为dp
     */
    private static final int MIN_DRAWER_MARGIN = 64; // dp
    /**
     * 触发滑动的最小距离，单位为dp/s
     */
    private static final int MIN_FLING_VELOCITY = 400; // dips per second

    /**
     * drawer离父容器右边的最小外边距
     */
    private int mMinDrawerMargin;
    /**
     * 左边的菜单View
     */
    private View mLeftMenuView;
    private View mContentView;

    private ViewDragHelper mHelper;
    /**
     * drawer显示出来的占自身的百分比
     */
    private float mLeftMenuOnScreen;


    public LeftDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setup drawer's minMargin
        final float density = getResources().getDisplayMetrics().density;
        final float minVel = MIN_FLING_VELOCITY * density;
        mMinDrawerMargin = (int) (MIN_DRAWER_MARGIN * density + 0.5f);

        mHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                LogTool.e(TAG, "onEdgeDragStarted");
                // 因为开始的时候mLeftMenuView不可见，通过captureChildView对mLeftMenuView进行捕获
                mHelper.captureChildView(mLeftMenuView, pointerId);
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                // 水平移动距离 [-child.getWidth()，0]
                LogTool.e(TAG, "left is " + left);
                return Math.max(-child.getWidth(), Math.min(left, 0));
            }

            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                LogTool.e(TAG, "tryCaptureView");
                // 只有mLeftMenuView可以拖动
                return child == mLeftMenuView;
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return mLeftMenuView == child ? child.getWidth() : 0;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                // 点xvel的值只有大于我们设置的minVelocity才会出现大于0，如果小于我们设置的值则一直是0。
                final int childWidth = releasedChild.getWidth();
                float offset = (childWidth + releasedChild.getLeft()) * 1.0f / childWidth;
                mHelper.settleCapturedViewAt(xvel > 0 || xvel == 0 && offset > 0.5f ? 0 : -childWidth, releasedChild.getTop());
                LogTool.e(TAG, "onViewReleased offset is " + offset);
                invalidate();
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                final int childWidth = changedView.getWidth();
                float offset = (float) (childWidth + left) / childWidth;
                mLeftMenuOnScreen = offset;
                LogTool.e(TAG, "mLeftMenuOnScreen is " + mLeftMenuOnScreen);
                //offset can callback here
                changedView.setVisibility(offset == 0 ? View.INVISIBLE : View.VISIBLE);
                invalidate();
            }

        });
        //设置edge_left track
        mHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        //设置minVelocity
        mHelper.setMinVelocity(minVel);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LogTool.e(TAG, "onMeasure");
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);

        // 测量菜单View
        View leftMenuView = getChildAt(1);
        MarginLayoutParams lp = (MarginLayoutParams)
                leftMenuView.getLayoutParams();
        /**
         * getChildMeasureSpec() 在measureChildren中最难的部分：找出传递给child的MeasureSpec。
         * 目的是结合父view的MeasureSpec与子view的LayoutParams信息去找到最好的结果
         * （也就是说子view的确切大小由两方面共同决定：1.父view的MeasureSpec 2.子view的LayoutParams属性）
         *
         * @param spec 父view的详细测量值(MeasureSpec)
         * @param padding view当前尺寸的的内边距和外边距(padding,margin)
         * @param childDimension child在当前尺寸下的布局参数宽高值(LayoutParam.width,height)
         */

        final int drawerWidthSpec = getChildMeasureSpec(widthMeasureSpec,
                mMinDrawerMargin + lp.leftMargin + lp.rightMargin,
                lp.width);
        final int drawerHeightSpec = getChildMeasureSpec(heightMeasureSpec,
                lp.topMargin + lp.bottomMargin,
                lp.height);
        leftMenuView.measure(drawerWidthSpec, drawerHeightSpec);

        // 测量contentView
        View contentView = getChildAt(0);
        lp = (MarginLayoutParams) contentView.getLayoutParams();
        final int contentWidthSpec = MeasureSpec.makeMeasureSpec(
                widthSize - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY);
        final int contentHeightSpec = MeasureSpec.makeMeasureSpec(
                heightSize - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY);
        contentView.measure(contentWidthSpec, contentHeightSpec);

        mLeftMenuView = leftMenuView;
        mContentView = contentView;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        LogTool.e(TAG, "onLayout");
        View menuView = mLeftMenuView;
        View contentView = mContentView;

        MarginLayoutParams lp = (MarginLayoutParams) contentView.getLayoutParams();
        contentView.layout(lp.leftMargin, lp.topMargin,
                lp.leftMargin + contentView.getMeasuredWidth(),
                lp.topMargin + contentView.getMeasuredHeight());

        lp = (MarginLayoutParams) menuView.getLayoutParams();

        final int menuWidth = menuView.getMeasuredWidth();
        int childLeft = -menuWidth + (int) (menuWidth * mLeftMenuOnScreen);
        menuView.layout(childLeft, lp.topMargin, childLeft + menuWidth,
                lp.topMargin + menuView.getMeasuredHeight());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mHelper.shouldInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mHelper.processTouchEvent(event);
        return true;
    }


    @Override
    public void computeScroll() {
        if (mHelper.continueSettling(true)) {
            invalidate();
        }
    }

    public void closeDrawer() {
        View menuView = mLeftMenuView;
        mLeftMenuOnScreen = 0.f;
        mHelper.smoothSlideViewTo(menuView, -menuView.getWidth(), menuView.getTop());
    }

    public void openDrawer() {
        View menuView = mLeftMenuView;
        mLeftMenuOnScreen = 1.0f;
        mHelper.smoothSlideViewTo(menuView, 0, menuView.getTop());
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
