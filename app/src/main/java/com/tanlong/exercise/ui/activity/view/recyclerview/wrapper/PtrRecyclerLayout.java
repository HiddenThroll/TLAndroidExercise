package com.tanlong.exercise.ui.activity.view.recyclerview.wrapper;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.view.recyclerview.layoutmanager.ILayoutManager;


/**
 * 包含SwipeRefreshLayout和RecyclerView，实现下拉刷新，上拉加载
 * Created by 龙 on 2017/3/14.
 */

public class PtrRecyclerLayout extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener{

    public static final int ACTION_PULL_TO_REFRESH = 1;
    public static final int ACTION_LOAD_MORE_REFRESH = 2;
    public static final int ACTION_IDLE = 0;

    private int TRIGGER_LOAD_MORE_ITEM_COUNT = 5;

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

    public PtrRecyclerLayout(Context context) {
        super(context);
        initView();
    }

    public PtrRecyclerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PtrRecyclerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_ptr_recycler, this, true);
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.swipeRefreshLayout);
        mFooterView = LayoutInflater.from(getContext()).inflate(R.layout.layout_refresh_item, null);
        TextView tvLoadMoreTips = (TextView) mFooterView.findViewById(R.id.tv_refresh_tips);
        tvLoadMoreTips.setText("正在加载...");

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mCurrentState == ACTION_IDLE && isLoadMoreEnabled && checkIfNeedLoadMore()) {
                    mCurrentState = ACTION_LOAD_MORE_REFRESH;
                    mAdapter.addFootView(mFooterView);
                    mLoadMoreListener.onLoadMore();
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
        return totalCount - lastVisibleItemPosition < TRIGGER_LOAD_MORE_ITEM_COUNT;
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public View getmFooterView() {
        return mFooterView;
    }

    public void setmFooterView(View mFooterView) {
        this.mFooterView = mFooterView;
    }

    public int getTRIGGER_LOAD_MORE_ITEM_COUNT() {
        return TRIGGER_LOAD_MORE_ITEM_COUNT;
    }

    public void setTRIGGER_LOAD_MORE_ITEM_COUNT(int TRIGGER_LOAD_MORE_ITEM_COUNT) {
        this.TRIGGER_LOAD_MORE_ITEM_COUNT = TRIGGER_LOAD_MORE_ITEM_COUNT;
    }

    public void setmRefreshListener(OnRefreshListener mRefreshListener) {
        this.mRefreshListener = mRefreshListener;
    }

    public void setmLoadMoreListener(OnLoadMoreListener mLoadMoreListener) {
        this.mLoadMoreListener = mLoadMoreListener;
    }
}
