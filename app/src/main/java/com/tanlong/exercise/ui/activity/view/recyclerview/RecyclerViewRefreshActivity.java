package com.tanlong.exercise.ui.activity.view.recyclerview;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.model.entity.NewsItem;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.activity.view.recyclerview.adapter.NewsSingleAdapter;
import com.tanlong.exercise.ui.activity.view.recyclerview.wrapper.PtrRecycler;
import com.tanlong.exercise.ui.activity.view.recyclerview.wrapper.PtrRecyclerHelper;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * RecyclerView实现下拉刷新、上拉加载更多
 * Created by 龙 on 2017/3/3.
 */

public class RecyclerViewRefreshActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    List<NewsItem> mDatas;
    NewsSingleAdapter mContentAdapter;
    SingleNewsPtrRecycler singleNewsPtrRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_recycler_view);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    private void initData() {
        mDatas = new ArrayList<>();
    }

    private void initView() {
        tvTitle.setText(R.string.recycler_view_refresh);
        btnHelp.setVisibility(View.VISIBLE);
        singleNewsPtrRecycler = new SingleNewsPtrRecycler(this);
        singleNewsPtrRecycler.init();
    }

    @OnClick({R.id.iv_back, R.id.btn_help})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                showTips();
                break;
        }
    }

    private class SingleNewsPtrRecycler extends PtrRecycler<NewsItem> {

        public SingleNewsPtrRecycler(Context context) {
            super(context);
        }

        @Override
        protected void getRefreshData() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDatas.clear();
                    for (int i = 0; i < getmPageSize(); i++) {
                        mDatas.add(new NewsItem("标题" + i, "内容" + i, ""));
                    }
                    finishPullRefresh();
                }
            }, 2000);
        }

        @Override
        protected void getLoadMoreData() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int size = mDatas.size();
                    for (int i = size; i < size + getmPageSize(); i++) {
                        mDatas.add(new NewsItem("标题" + i, "内容" + i, ""));
                    }
                    finishLoadMore(getmPageSize());
                }
            }, 5000);
        }

        @Override
        protected PtrRecyclerHelper getPtrRecyclerHelper() {
            View mFooterView = LayoutInflater.from(getContext()).inflate(R.layout.layout_refresh_item, null);
            TextView tvLoadMoreTips = (TextView) mFooterView.findViewById(R.id.tv_refresh_tips);
            tvLoadMoreTips.setText("正在加载...");

            PtrRecyclerHelper ptrRecyclerHelper = new PtrRecyclerHelper(swipeRefreshLayout,
                    recyclerView, mFooterView);
            return ptrRecyclerHelper;
        }

        @Override
        protected RecyclerView.Adapter getAdapter() {
            mContentAdapter = new NewsSingleAdapter(getContext(), mDatas, R.layout.item_news_type_2);
            mContentAdapter.setmEmptyLayoutId(R.layout.layout_recycler_empty);
            return mContentAdapter;
        }
    }


    public void showTips() {
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
