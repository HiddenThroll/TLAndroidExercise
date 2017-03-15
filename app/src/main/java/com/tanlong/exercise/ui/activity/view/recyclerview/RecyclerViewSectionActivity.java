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

        for (int i = 0; i < 3; i++) {
            SectionData<NewsItem> sectionData;
            if (i == 0) {
                sectionData = new SectionData<>(true, 1, "第一类新闻");
            } else {
                NewsItem newsItem = new NewsItem("标题 + 大图形式 ", "");
                sectionData = new SectionData<>(newsItem);
            }
            mDatas.add(sectionData);
        }

        for (int i = 0; i < 3; i++) {
            SectionData<NewsItem> sectionData;
            if (i == 0) {
                sectionData = new SectionData<>(true, 1, "第三类新闻");
            } else {
                NewsItem newsItem = new NewsItem("标题 + 3副图片 ", "", "", "");
                sectionData = new SectionData<>(newsItem);
            }
            mDatas.add(sectionData);
        }

        for (int i = 0; i < 3; i++) {
            SectionData<NewsItem> sectionData;
            if (i == 0) {
                sectionData = new SectionData<>(true, 2, "第四类新闻");
            } else {
                NewsItem newsItem = new NewsItem("测试");
                sectionData = new SectionData<>(newsItem);
            }
            mDatas.add(sectionData);
        }
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
