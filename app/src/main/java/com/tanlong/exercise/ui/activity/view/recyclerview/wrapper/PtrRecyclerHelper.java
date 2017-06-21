package com.tanlong.exercise.ui.activity.view.recyclerview.wrapper;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tanlong.exercise.ui.activity.view.recyclerview.layoutmanager.ILayoutManager;


/**
 * Created by 龙 on 2017/5/17.
 */

public class PtrRecyclerHelper implements SwipeRefreshLayout.OnRefreshListener{

    private final String TAG = getClass().getSimpleName();

    public static final int ACTION_PULL_TO_REFRESH = 1;
    public static final int ACTION_LOAD_MORE_REFRESH = 2;
    public static final int ACTION_IDLE = 0;
    /**
     * 当最后一个可见Item距总Item个数小于该值时，触发加载更多
     */
    private int TRIGGER_LOAD_MORE_ITEM_COUNT = 1;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private OnRefreshListener mRefreshListener;
    private OnLoadMoreListener mLoadMoreListener;

    private int mCurrentState = ACTION_IDLE;
    private boolean isLoadMoreEnabled = false;
    private boolean isPullToRefreshEnabled = true;

    private ILayoutManager mLayoutManager;
    private HeaderAndFooterWrapper mAdapter;
    private View mFooterView;

    private int scrollState = -1;//记录RecyclerView的滑动状态, 避免首次刷新时加载数据无法铺满屏幕触发上拉加载

    public interface OnRefreshListener {
        void onRefresh();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public PtrRecyclerHelper(SwipeRefreshLayout mSwipeRefreshLayout, RecyclerView mRecyclerView,
                             View footerView) {
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
        this.mRecyclerView = mRecyclerView;
        this.mFooterView = footerView;
    }

    public SwipeRefreshLayout getmSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    public RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }

    public void init() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                scrollState = newState;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mCurrentState == ACTION_IDLE && isLoadMoreEnabled
                        && scrollState != -1 && checkIfNeedLoadMore()) {
                    mCurrentState = ACTION_LOAD_MORE_REFRESH;
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.addFootView(mFooterView);
                            mLoadMoreListener.onLoadMore();
                        }
                    });
                }
            }
        });
}

    public void setLayoutManager(ILayoutManager manager) {
        mLayoutManager = manager;
        mRecyclerView.setLayoutManager(mLayoutManager.getLayoutManager());
    }

    public void setAdapter(HeaderAndFooterWrapper adapter) {
        mAdapter = adapter;
        mRecyclerView.setAdapter(mAdapter);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decoration) {
        if (decoration != null) {
            mRecyclerView.addItemDecoration(decoration);
        }
    }

    @Override
    public void onRefresh() {
        mCurrentState = ACTION_PULL_TO_REFRESH;
        if (mRefreshListener != null) {
            mRefreshListener.onRefresh();
        }
    }

    /**
     * 停止刷新
     */
    public void onRefreshCompleted() {
        switch (mCurrentState) {
            case ACTION_PULL_TO_REFRESH:
                mSwipeRefreshLayout.setRefreshing(false);//停止下拉刷新
                break;
            case ACTION_LOAD_MORE_REFRESH:
                // 停止加载更多
                mAdapter.deleteFooterView(mFooterView);
                if (isPullToRefreshEnabled) {
                    mSwipeRefreshLayout.setEnabled(true);
                }
                break;
        }
        mCurrentState = ACTION_IDLE;
    }

    public void doRefresh() {
        if (mRefreshListener != null) {
            mSwipeRefreshLayout.setRefreshing(true);
            mCurrentState = ACTION_PULL_TO_REFRESH;
            mRefreshListener.onRefresh();
        }
    }

    public void enablePullToRefresh(boolean enable) {
        isPullToRefreshEnabled = enable;
        mSwipeRefreshLayout.setEnabled(enable);
    }

    public void enableLoadMore(boolean enable) {
        isLoadMoreEnabled = enable;
    }

    /**
     * 检测是否有需要加载更多, 当最后一个可见Item距总Item个数小于5个时，返回true
     * @return
     */
    private boolean checkIfNeedLoadMore() {
        int lastVisibleItemPosition = mLayoutManager.findLastVisiblePosition();
        int totalCount = mLayoutManager.getLayoutManager().getItemCount();
        if (totalCount == 0) {//空列表时，不允许加载更多
            return false;
        }
        return totalCount - lastVisibleItemPosition <= TRIGGER_LOAD_MORE_ITEM_COUNT;
    }

    public void setRefreshListener(OnRefreshListener mRefreshListener) {
        this.mRefreshListener = mRefreshListener;
    }

    public void setLoadMoreListener(OnLoadMoreListener mLoadMoreListener) {
        this.mLoadMoreListener = mLoadMoreListener;
    }
}
