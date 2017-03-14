package com.tanlong.exercise.ui.activity.view.recyclerview;

import android.os.Bundle;
import android.os.Handler;

import com.tanlong.exercise.R;
import com.tanlong.exercise.model.entity.NewsItem;
import com.tanlong.exercise.ui.activity.view.recyclerview.adapter.NewsSingleAdapter;
import com.tanlong.exercise.ui.activity.view.recyclerview.base.BaseRecyclerActivity;
import com.tanlong.exercise.ui.activity.view.recyclerview.wrapper.PtrRecyclerLayout;
import com.tanlong.exercise.ui.activity.view.recyclerview.layoutmanager.MyLinearLayoutManager;
import com.tanlong.exercise.ui.activity.view.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView实现下拉刷新、上拉加载更多
 * Created by 龙 on 2017/3/3.
 */

public class RecyclerViewRefreshActivity extends BaseRecyclerActivity implements PtrRecyclerLayout.OnRefreshListener, PtrRecyclerLayout.OnLoadMoreListener{

    List<NewsItem> mDatas;
    NewsSingleAdapter mContentAdapter;
    HeaderAndFooterWrapper<NewsItem> mWrapperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        initView();
    }

    private void initData() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mDatas.add(new NewsItem("标题" + i, "内容" + i, ""));
        }
    }

    private void initView() {
        setTitle(getString(R.string.recycler_view_refresh));
        mContentAdapter = new NewsSingleAdapter(this, mDatas, R.layout.item_news_type_2);
        mWrapperAdapter = new HeaderAndFooterWrapper<>(mContentAdapter);
        setLayoutManager(new MyLinearLayoutManager(this));
        setAdapter(mWrapperAdapter);

        setRefreshListener(this);
        setLoadMoreListener(this);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int size = mDatas.size();
                for (int i = size; i < size + 10; i++) {
                    mDatas.add(new NewsItem("标题" + i, "内容" + i, ""));
                }
                mContentAdapter.notifyDataSetChanged();
                onRefreshComplete();
            }
        }, 2000);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDatas.clear();
                for (int i = 0; i < 20; i++) {
                    mDatas.add(new NewsItem("标题" + i, "内容" + i, ""));
                }
                mContentAdapter.notifyDataSetChanged();
                onRefreshComplete();
            }
        }, 2000);
    }

    @Override
    public void showTips() {
        super.showTips();
//        StringBuilder stringBuilder = new StringBuilder();
    }
}
