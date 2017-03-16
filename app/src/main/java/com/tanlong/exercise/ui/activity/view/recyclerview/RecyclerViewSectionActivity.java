package com.tanlong.exercise.ui.activity.view.recyclerview;

import android.os.Bundle;
import android.os.Handler;

import com.tanlong.exercise.R;
import com.tanlong.exercise.model.entity.NewsItem;
import com.tanlong.exercise.ui.activity.view.recyclerview.adapter.NewsSectionAdapter;
import com.tanlong.exercise.ui.activity.view.recyclerview.adapter.base.MultiItemTypeAdapter;
import com.tanlong.exercise.ui.activity.view.recyclerview.base.BaseRecyclerActivity;
import com.tanlong.exercise.ui.activity.view.recyclerview.entity.SectionData;
import com.tanlong.exercise.ui.activity.view.recyclerview.layoutmanager.MyLinearLayoutManager;
import com.tanlong.exercise.ui.activity.view.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.tanlong.exercise.ui.activity.view.recyclerview.wrapper.PtrRecyclerLayout;
import com.tanlong.exercise.ui.fragment.ShowTipsFragment;
import com.tanlong.exercise.util.ToastHelp;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by 龙 on 2017/3/15.
 */

public class RecyclerViewSectionActivity extends BaseRecyclerActivity implements PtrRecyclerLayout.OnRefreshListener,
        PtrRecyclerLayout.OnLoadMoreListener{

    private List<SectionData<NewsItem>> mDatas;
    private NewsSectionAdapter mContentAdapter;
    private HeaderAndFooterWrapper<NewsItem> mWrapperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        initView();
    }

    private void initData() {
        mDatas = new ArrayList<>();
//        mDatas.addAll(addNewsItem(10, NewsItem.NEWS_TYPE_1));
//        mDatas.addAll(addNewsItem(10, NewsItem.NEWS_TYPE_2));
//        mDatas.addAll(addNewsItem(10, NewsItem.NEWS_TYPE_3));
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
                    newsItem = new NewsItem("左侧图片 + 右侧标题 + 描述字段","一段内容","");
                    break;
                case NewsItem.NEWS_TYPE_3:
                    header = "第三类新闻";
                    newsItem = new NewsItem("标题 + 3副图片 ", "", "", "");
                    break;
            }
            if (i == 0) {
                sectionData = new SectionData<NewsItem>(true, 0, header);
            } else {
                sectionData = new SectionData<NewsItem>(newsItem);
            }
            datas.add(sectionData);
        }
        return datas;
    }



    private void initView() {
        setTitle(getString(R.string.recycler_view_section));
        mContentAdapter = new NewsSectionAdapter(this, mDatas, R.layout.layout_recycler_section);
        mWrapperAdapter = new HeaderAndFooterWrapper<>(mContentAdapter);
        setLayoutManager(new MyLinearLayoutManager(this));
        mContentAdapter.setmEmptyLayoutId(R.layout.layout_recycler_empty);
        mContentAdapter.setOnRefreshEmptyView(new MultiItemTypeAdapter.OnRefreshEmptyView() {
            @Override
            public void onRefreshEmptyView() {
                ToastHelp.showShortMsg(getApplicationContext(), "正在刷新...");
                onRefresh();
            }
        });
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
                if (size % 3 == 0) {
                    mDatas.addAll(addNewsItem(10, NewsItem.NEWS_TYPE_1));
                } else if (size % 3 == 1) {
                    mDatas.addAll(addNewsItem(10, NewsItem.NEWS_TYPE_2));
                } else if (size % 3 == 2) {
                    mDatas.addAll(addNewsItem(10, NewsItem.NEWS_TYPE_3));
                }

                mWrapperAdapter.notifyDataSetChanged();
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
                mDatas.addAll(addNewsItem(10, NewsItem.NEWS_TYPE_1));
                mDatas.addAll(addNewsItem(10, NewsItem.NEWS_TYPE_2));
                mDatas.addAll(addNewsItem(10, NewsItem.NEWS_TYPE_3));
                mWrapperAdapter.notifyDataSetChanged();
                onRefreshComplete();
            }
        }, 2000);
    }

    @Override
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
