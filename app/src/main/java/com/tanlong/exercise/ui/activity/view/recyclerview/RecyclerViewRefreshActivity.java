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
import com.tanlong.exercise.ui.fragment.ShowTipsFragment;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.fragment;

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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("实现下拉刷新上拉加载的原理为: ")
                .append("1. 下拉刷新通过SwipeRefreshLayout控件实现\n")
                .append("2. 上拉加载通过RecyclerView.addOnScrollListener()实现，具体为：\n")
                .append("2.1 当总Item个数 - 最后一个可见Item的位置小于某个值时，触发加载更多\n")
                .append("2.1.1 LinearLayoutManager和GridLayoutManager的findLastVisibleItemPosition()返回int, StaggerGridLayoutManager的findLastVisibleItemPositions()返回int[]，这里使用策略模式，统一返回数据\n")
                .append("2.2 加载更多使用的FooterView实质是在Adapter中通过不同的Item ViewType添加的Item View\n")
                .append("2.2.1 针对GridLayoutManager，需通过GridLayoutManager.setSpanCount(int)方法设置每个Item占用的列数，即普通Item为1，FooterView为总列数\n")
                .append("2.2.2 针对StaggeredGridLayoutManager，需通过StaggeredGridLayoutManager.LayoutParams.setFullSpan(boolean)设置每个Item是否占用整列\n");

        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }
}
