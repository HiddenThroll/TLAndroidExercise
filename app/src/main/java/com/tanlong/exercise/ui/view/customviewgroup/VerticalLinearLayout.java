package com.tanlong.exercise.ui.view.customviewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.tanlong.exercise.util.DisplayUtil;
import com.tanlong.exercise.util.LogTool;


/**
 * 竖向引导界面, 有较大实用价值
 * Created by Administrator on 2016/4/26.
 */
public class VerticalLinearLayout extends ViewGroup {

    private final String TAG = "VerticalLinearLayout";
    /**
     * 屏幕高度
     */
    private int mScreenHeight;
    private Scroller mScroller;
    /**
     * 是否正在滚动
     */
    private boolean isScrolling;
    /**
     * 加速度检测
     */
    private VelocityTracker mVelocityTracker;
    /**
     * 手指按下时的getScrollY
     */
    private int mScrollStart;
    /**
     * 手指抬起时的getScrollY
     */
    private int mScrollEnd;
    /**
     * 记录移动时的Y
     */
    private int mLastY;
    private int currentPage = 0;

    private OnPageChangeListener mOnPageChangeListener;

    /**
     * 回调接口
     */
    public interface OnPageChangeListener {
        void onPageChange(int currentPage);
    }

    public VerticalLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mScreenHeight = DisplayUtil.getDisplay(context).y;
        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            // 测量每一个子View
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int childCount = getChildCount();

            // 主布局的高度 = 屏幕高度 * 子View个数
            MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
            params.height = childCount * mScreenHeight;

            // 设置子View布局位置
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                if (childView.getVisibility() != View.GONE) {
                    // 调用每个子View的layout，让每个子View纵向依次排列
                    childView.layout(l, i * mScreenHeight, r, (i + 1) * mScreenHeight);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 如果当前正在滚动，调用父类的onTouchEvent
        if (isScrolling) {
            return super.onTouchEvent(event);
        }

        int action = event.getAction();
        int y = (int) event.getY();
        // 初始化加速度检测器
        obtainVelocity(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mScrollStart = getScrollY();// 猜：当前可见View顶部到最开始View顶部的距离
                LogTool.e(TAG, "mScrollStart is " + mScrollStart);
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScroller.isFinished()) {// 如果还在滑动
                    mScroller.abortAnimation();// 结束动画
                }
                // 边界值检查
                int dy = mLastY - y;// Y轴滑动距离
                int scrollY = getScrollY();
                LogTool.e(TAG, "move scrollY is " + scrollY);
                // 已经到达顶端，下拉多少，就往上滚动多少
                if (dy < 0 && scrollY + dy < 0) {// dy<0代表往上滑
                    dy = -scrollY;
                }
                // 已经到达底部，上拉多少，就往下滚动多少
                // dy>0代表往下滑
                // getHeight()和mScreenHeight均为固定值，两者相减等于倒数第二页高度
                if (dy > 0 && scrollY + dy > getHeight() - mScreenHeight) {
                    dy = getHeight() - mScreenHeight - scrollY;
                }
                scrollBy(0, dy);// 移动时滑动
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                mScrollEnd = getScrollY();
                LogTool.e(TAG, "mScrollEnd is " + mScrollEnd);
                int dScrollY = mScrollEnd - mScrollStart;

                if (wantScrollToNext()) {// 手指往上滑动
                    if (shouldScrollToNext()) {//是否滑动到下一页
                        mScroller.startScroll(0, getScrollY(), 0, mScreenHeight - dScrollY);//滑到下一页

                    } else {
                        mScroller.startScroll(0, getScrollY(), 0, -dScrollY);//还原
                    }

                }

                if (wantScrollToPre()) {// 手指往下滑动
                    if (shouldScrollToPre()) {//是否滑动到上一页
                        mScroller.startScroll(0, getScrollY(), 0, -mScreenHeight - dScrollY);//滑到上一页
                    } else {
                        mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
                    }
                }
                isScrolling = true;
                postInvalidate();
                recycleVelocity();
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {// 为true代表动画还没有结束
            scrollTo(0, mScroller.getCurrY());// 在ACTION_MOVE时，已经滑动了一段距离，这里继续滑动
            postInvalidate();
        } else {

            int position = getScrollY() / mScreenHeight;

            if (position != currentPage) {
                if (mOnPageChangeListener != null) {
                    currentPage = position;
                    mOnPageChangeListener.onPageChange(currentPage);
                }
            }

            isScrolling = false;
        }

    }

    /**
     * 初始化加速度检测器
     *
     * @param event -- 检测的事件
     */
    private void obtainVelocity(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 获取y方向的加速度
     *
     * @return
     */
    private int getVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        return (int) mVelocityTracker.getYVelocity();
    }

    /**
     * 释放加速度检测器
     */
    private void recycleVelocity() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * 根据用户滑动，判断用户的意图是否是滚动到下一页
     *
     * @return
     */
    private boolean wantScrollToNext() {
        return mScrollEnd > mScrollStart;
    }

    /**
     * 根据用户滑动，判断用户的意图是否是滚动到上一页
     *
     * @return
     */
    private boolean wantScrollToPre() {
        return mScrollEnd < mScrollStart;
    }

    /**
     * 根据滚动距离判断是否能够滚动到上一页
     *
     * @return
     */
    private boolean shouldScrollToPre() {
        // 滚动距离大于半个屏幕或滚动速度大于600
        return mScrollStart - mScrollEnd > mScreenHeight / 2 || Math.abs(getVelocity()) > 600;
    }

    /**
     * 根据滚动距离判断是否能够滚动到下一页
     *
     * @return
     */
    private boolean shouldScrollToNext() {
        // 滚动距离大于半个屏幕或滚动速度大于600
        return mScrollEnd - mScrollStart > mScreenHeight / 2 || Math.abs(getVelocity()) > 600;
    }

    /**
     * 设置回调接口
     *
     * @param onPageChangeListener
     */
    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }
}
