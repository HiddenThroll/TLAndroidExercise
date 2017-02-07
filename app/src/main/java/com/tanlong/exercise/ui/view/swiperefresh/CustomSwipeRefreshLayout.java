package com.tanlong.exercise.ui.view.swiperefresh;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ListView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.util.LogTool;

/**
 * 自定义SwipeRefreshLayout，具备以下功能：
 * 1. 解决横向滑动引起的SwipeRefreshLayout下拉事件冲突
 * 2. 实现上滑加载更多功能
 * Created by 龙 on 2016/6/21.
 */
public class CustomSwipeRefreshLayout extends SwipeRefreshLayout implements AbsListView.OnScrollListener {
    private final String TAG = "CustomSwipeRefreshLayout";
    /**
     * 滑动响应距离，当手指移动距离大于该值时，系统才认为发生了滑动事件
     */
    private int mTouchSlop;
    private float mPrevX;

    private ListView mListView;
    private View mBottomView;

    /**
     * 是否正在加载更多
     */
    private boolean isLoading;
    /**
     * 是否可以加载更多
     */
    private boolean mCanLoadMore;

    private OnLoadMoreListener mLoadMoreListener;

    /**
     * 加载更多的监听器
     */
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public CustomSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public CustomSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mBottomView = LayoutInflater.from(context).inflate(R.layout.layout_load_more, null,
                false);
        isLoading = false;
        mCanLoadMore = true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 初始化ListView对象
        if (mListView == null) {
            getListView();
        }
    }

    /**
     * 获取ListView对象
     */
    private void getListView() {
        LogTool.e(TAG, "getListView");
        int childCount = getChildCount();
        LogTool.e(TAG, "childCount is " + childCount);
        if (childCount > 0) {
            View childView = getChildAt(0);
            if (childView instanceof ListView) {
                mListView = (ListView) childView;
                // 设置滚动监听器给ListView, 使得滚动的情况下也可以自动加载
                mListView.setOnScrollListener(this);
                LogTool.e(TAG, "找到listview");
            } else {
                LogTool.e(TAG, childView + " is not ListView");
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = MotionEvent.obtain(event).getX();
                break;

            case MotionEvent.ACTION_MOVE:
                final float eventX = event.getX();
                float xDiff = Math.abs(eventX - mPrevX);

                if (xDiff > mTouchSlop) {// 水平移动距离大于触发距离，将事件交由子View处理
                    return false;
                }
        }

        return super.onInterceptTouchEvent(event);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (!isLoading && mCanLoadMore) {// 没有加载 且 可以加载
            int selectItemHeight = getSelectItemHeight(mListView, firstVisibleItem, visibleItemCount, totalItemCount);
            if (firstVisibleItem + visibleItemCount == totalItemCount &&
                    selectItemHeight > view.getHeight()) {// 已经到达底部
                loadData();
            }
        }
    }

    /**
     * 加载数据
     */
    private void loadData() {
        if (mLoadMoreListener != null) {
            setLoading(true);
            mLoadMoreListener.onLoadMore();
        }
    }

    /**
     * 设置加载状态
     *
     * @param loading -- 是否正在加载
     */
    public void setLoading(boolean loading) {
        isLoading = loading;
        if (isLoading) {
            mListView.addFooterView(mBottomView);
        } else {
            mListView.removeFooterView(mBottomView);
        }
    }

    public boolean ismCanLoadMore() {
        return mCanLoadMore;
    }

    public void setmCanLoadMore(boolean mCanLoadMore) {
        this.mCanLoadMore = mCanLoadMore;
    }

    public void setmLoadMoreListener(OnLoadMoreListener mLoadMoreListener) {
        this.mLoadMoreListener = mLoadMoreListener;
    }

    /**
     * 设置刷新显示颜色
     */
    public void initColor() {
        setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_green_light, android.R.color.holo_blue_light,
                android.R.color.holo_orange_light);
    }

    /**
     * 获取ListV指定Item之间的高度
     * @param listView -- 获取的ListView
     * @param firstVisibleItem -- 当前ListView的第一个可见Item的index(包含未完全显示的Item)
     * @param visibleItemCount -- 当前ListView可见Item的个数(包含未完全显示的Item)
     * @param totalItemCount -- 当前ListView的Item总个数
     * @return
     */
    private int getSelectItemHeight(ListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Adapter adapter = listView.getAdapter();
        if (adapter == null || adapter.getCount() == 0) {
            return 0;
        }
        int allItemHeight = 0;
        for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
            if (i >= totalItemCount) {
                break;
            }
            View itemView = adapter.getView(i, null, listView);
            itemView.measure(0, 0);
            allItemHeight += itemView.getMeasuredHeight();
        }

        int allDividerHeight = listView.getDividerHeight() * (visibleItemCount - 1);
        if (allDividerHeight < 0) {
            allDividerHeight = 0;
        }
        return allItemHeight + allDividerHeight;
    }
}
