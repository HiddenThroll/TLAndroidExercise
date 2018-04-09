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
import com.tanlong.exercise.ui.activity.view.recyclerview.adapter.NewsSectionAdapter;
import com.tanlong.exercise.ui.activity.view.recyclerview.entity.SectionData;
import com.tanlong.exercise.ui.activity.view.recyclerview.wrapper.PtrRecycler;
import com.tanlong.exercise.ui.activity.view.recyclerview.wrapper.PtrRecyclerHelper;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 龙 on 2017/3/15.
 */

public class RecyclerViewSectionActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_help)
    Button btnHelp;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private List<SectionData<NewsItem>> mDatas;
    private NewsSectionAdapter mContentAdapter;

    SectionMultiNewsPtrRecycler ptrRecycler;

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

    private List<SectionData<NewsItem>> addNewsItem(int count, int type) {
        List<SectionData<NewsItem>> datas = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            SectionData<NewsItem> sectionData;
            String header = "";
            NewsItem newsItem = null;
            switch (type) {
                case NewsItem.NEWS_TYPE_1:
                    header = "第一类新闻";
                    newsItem = new NewsItem("标题 + 大图形式 ", "");
                    break;
                case NewsItem.NEWS_TYPE_2:
                    header = "第二类新闻";
                    newsItem = new NewsItem("左侧图片 + 右侧标题 + 描述字段", "一段内容", "");
                    break;
                case NewsItem.NEWS_TYPE_3:
                    header = "第三类新闻";
                    newsItem = new NewsItem("标题 + 3副图片 ", "", "", "");
                    break;
            }
            if (i == 0) {
                sectionData = new SectionData<>(true, 0, header);
            } else {
                sectionData = new SectionData<>(newsItem);
            }
            datas.add(sectionData);
        }
        return datas;
    }


    private void initView() {
        tvTitle.setText(R.string.recycler_view_section);
        btnHelp.setVisibility(View.VISIBLE);
        ptrRecycler = new SectionMultiNewsPtrRecycler(this);
        ptrRecycler.init();
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

    private class SectionMultiNewsPtrRecycler extends PtrRecycler<SectionData<NewsItem>> {

        public SectionMultiNewsPtrRecycler(Context context) {
            super(context);
        }

        @Override
        protected void getRefreshData() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDatas.clear();
                    mDatas.addAll(addNewsItem(getmPageSize(), NewsItem.NEWS_TYPE_1));
                    mDatas.addAll(addNewsItem(getmPageSize(), NewsItem.NEWS_TYPE_2));
                    mDatas.addAll(addNewsItem(getmPageSize(), NewsItem.NEWS_TYPE_3));
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
                    if (size % 3 == 0) {
                        mDatas.addAll(addNewsItem(getmPageSize(), NewsItem.NEWS_TYPE_1));
                    } else if (size % 3 == 1) {
                        mDatas.addAll(addNewsItem(getmPageSize(), NewsItem.NEWS_TYPE_2));
                    } else if (size % 3 == 2) {
                        mDatas.addAll(addNewsItem(getmPageSize(), NewsItem.NEWS_TYPE_3));
                    }
                    finishLoadMore(getmPageSize());
                }
            }, 2000);
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
            mContentAdapter = new NewsSectionAdapter(getContext(), mDatas, R.layout.layout_recycler_section);
            mContentAdapter.setmEmptyLayoutId(R.layout.layout_recycler_empty);
            return mContentAdapter;
        }
    }

    public void showTips() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("添加Section原理: \n")
                .append("1. SectionData<T>类: \n")
                .append("1.1 新建SectionData<T>类，包含isHeader属性指明是否是Header和T data属性包含业务实体类\n")
                .append("1.2 数据源List<SectionData<T>>包含两种数据，一种是无业务实体的Header SectionData，一种是有业务实体的Content SectionData\n")
                .append("2. 覆写Adapter：\n")
                .append("2.1 覆写getItemViewType(int position)，当对应的SectionData.isHeader为true时，返回特殊的SectionViewType\n")
                .append("2.2 覆写onCreateViewHolder(ViewGroup parent, int viewType)，当viewType等于SectionViewType时，创建SectionHeaderViewHolder\n")
                .append("2.3 覆写onBindViewHolder(ViewHolder holder, int position)，当对应的SectionData.isHeader为true时，调用onBindSectionHeaderViewHolder()方法实现Section的View绑定与赋值\n");

        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }
}
