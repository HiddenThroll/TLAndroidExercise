package com.tanlong.exercise.ui.activity.view.recyclerview;

import android.os.Bundle;

import com.tanlong.exercise.R;
import com.tanlong.exercise.model.entity.NewsItem;
import com.tanlong.exercise.ui.activity.view.recyclerview.adapter.NewsSectionAdapter;
import com.tanlong.exercise.ui.activity.view.recyclerview.base.BaseRecyclerActivity;
import com.tanlong.exercise.ui.activity.view.recyclerview.entity.SectionData;
import com.tanlong.exercise.ui.activity.view.recyclerview.layoutmanager.MyLinearLayoutManager;
import com.tanlong.exercise.ui.activity.view.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by 龙 on 2017/3/15.
 */

public class RecyclerViewSectionActivity extends BaseRecyclerActivity {

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
        mDatas.addAll(addNewsItem(10, NewsItem.NEWS_TYPE_1));
        mDatas.addAll(addNewsItem(10, NewsItem.NEWS_TYPE_2));
        mDatas.addAll(addNewsItem(10, NewsItem.NEWS_TYPE_3));
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
        mContentAdapter = new NewsSectionAdapter(this, mDatas, R.layout.layout_recycler_section);
        mWrapperAdapter = new HeaderAndFooterWrapper<>(mContentAdapter);
        setLayoutManager(new MyLinearLayoutManager(this));
        setAdapter(mWrapperAdapter);

        enableLoadMore(false);
        enablePullToRefresh(false);
    }
}
