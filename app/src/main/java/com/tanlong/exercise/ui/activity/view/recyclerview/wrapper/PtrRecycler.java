package com.tanlong.exercise.ui.activity.view.recyclerview.wrapper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.tanlong.exercise.ui.activity.view.recyclerview.layoutmanager.MyLinearLayoutManager;

/**
 * Created by 龙 on 2017/5/22.
 */

public abstract class PtrRecycler<T> {

    private PtrRecyclerHelper mHelper;
    private HeaderAndFooterWrapper<T> mAdapter;

    private int mPageSize = 10;
    private Context context;

    public PtrRecycler(Context context) {
        this.context = context;
    }

    /**
     * 在布局完成后，调用该方法
     */
    public void init() {
        if (mHelper == null) {
            Logger.e("init Helper");

            mHelper = getPtrRecyclerHelper();
            mHelper.init();

            mAdapter = new HeaderAndFooterWrapper<>(getAdapter());
            mHelper.setAdapter(mAdapter);

            mHelper.setLayoutManager(new MyLinearLayoutManager(context));

            mHelper.setRefreshListener(new PtrRecyclerHelper.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getRefreshData();
                }
            });

            mHelper.setLoadMoreListener(new PtrRecyclerHelper.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    getLoadMoreData();
                }
            });

            mHelper.doRefresh();
        }
    }

    /**
     * 下拉刷新获取数据
     */
    protected abstract void getRefreshData();

    /**
     * 上拉加载获取数据
     */
    protected abstract void getLoadMoreData();

    /**
     * 完成PtrRecyclerHelper的初始化
     * @return
     */
    protected abstract PtrRecyclerHelper getPtrRecyclerHelper();

    /**
     * 完成RecyclerView使用的Adapter的初始化
     * @return
     */
    protected abstract RecyclerView.Adapter getAdapter();

    /**
     * 子类下拉刷新获取数据后调用
     */
    protected void finishPullRefresh() {
        mAdapter.notifyDataSetChanged();
        mHelper.onRefreshCompleted();
        // 刷新后，打开加载更多
        mHelper.enableLoadMore(true);
    }

    /**
     * 子类上拉加载获取数据后调用
     * @param count -- 本次上拉加载调用接口获取的数据条数，不是数据源的总条数！！！
     */
    protected void finishLoadMore(int count) {
        if (count >= mPageSize) {//还有更多数据
            mHelper.enableLoadMore(true);
        } else {//没有更多数据
            mHelper.enableLoadMore(false);
            Toast.makeText(context, "没有更多了~", Toast.LENGTH_SHORT).show();
        }

        mAdapter.notifyDataSetChanged();
        mHelper.onRefreshCompleted();
    }

    public int getmPageSize() {
        return mPageSize;
    }

    public void setmPageSize(int mPageSize) {
        this.mPageSize = mPageSize;
    }

    public PtrRecyclerHelper getmHelper() {
        return mHelper;
    }

    public void setmHelper(PtrRecyclerHelper mHelper) {
        this.mHelper = mHelper;
    }

    public HeaderAndFooterWrapper<T> getmAdapter() {
        return mAdapter;
    }

    public void setmAdapter(HeaderAndFooterWrapper<T> mAdapter) {
        this.mAdapter = mAdapter;
    }

    public Context getContext() {
        return context;
    }
}
