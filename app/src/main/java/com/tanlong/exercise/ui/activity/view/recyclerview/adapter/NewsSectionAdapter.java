package com.tanlong.exercise.ui.activity.view.recyclerview.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.model.entity.NewsItem;
import com.tanlong.exercise.ui.activity.view.recyclerview.adapter.base.SectionAdapter;
import com.tanlong.exercise.ui.activity.view.recyclerview.entity.SectionData;
import com.tanlong.exercise.ui.activity.view.recyclerview.viewholder.base.ItemViewDelegate;
import com.tanlong.exercise.ui.activity.view.recyclerview.viewholder.base.ViewHolder;

import java.util.List;


/**
 *
 * Created by 龙 on 2017/3/15.
 */

public class NewsSectionAdapter extends SectionAdapter<NewsItem> {

    public NewsSectionAdapter(Context mContext, List<SectionData<NewsItem>> mDatas, int sectionHeaderLayoutId) {
        super(mContext, mDatas, sectionHeaderLayoutId);

        addItemViewDelegate(new NewsTypeOneDelegate());
        addItemViewDelegate(new NewsTypeTwoDelegate());
        addItemViewDelegate(new NewsTypeThreeDelegate());
    }

    @Override
    protected void onBindSectionHeaderViewHolder(ViewHolder holder, SectionData sectionData, int position) {
        holder.setText(R.id.tv_section_title, sectionData.getHeader());
    }

    public class NewsTypeOneDelegate implements ItemViewDelegate<SectionData<NewsItem>> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_news_type_1;
        }

        @Override
        public boolean isForViewType(SectionData<NewsItem> item, int position) {
            return item.getData().getType() == NewsItem.NEWS_TYPE_1;
        }

        @Override
        public void convert(ViewHolder holder, SectionData<NewsItem> newsItemSectionData, int position) {
            holder.setText(R.id.tv_news_title_1, newsItemSectionData.getData().getTitle());
        }

    }

    public class NewsTypeTwoDelegate implements ItemViewDelegate<SectionData<NewsItem>> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_news_type_2;
        }

        @Override
        public boolean isForViewType(SectionData<NewsItem> item, int position) {
            return item.getData().getType() == NewsItem.NEWS_TYPE_2;
        }

        @Override
        public void convert(ViewHolder holder, SectionData<NewsItem> newsItem, int position) {
            holder.setText(R.id.tv_news_title_2, newsItem.getData().getTitle());
            holder.setText(R.id.tv_news_summary, newsItem.getData().getSummary());
        }
    }

    public class NewsTypeThreeDelegate implements ItemViewDelegate<SectionData<NewsItem>> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_news_type_3;
        }

        @Override
        public boolean isForViewType(SectionData<NewsItem> item, int position) {
            return item.getData().getType() == NewsItem.NEWS_TYPE_3;
        }

        @Override
        public void convert(ViewHolder holder, SectionData<NewsItem> newsItem, int position) {
            holder.setText(R.id.tv_news_title_3, newsItem.getData().getTitle());
        }
    }

    @Override
    protected void onBindEmptyViewHolder(ViewHolder holder, int position) {
        TextView tvEmptyTips = holder.getView(R.id.tv_recycler_empty_tips);
        tvEmptyTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRefreshEmptyView != null) {
                    onRefreshEmptyView.onRefreshEmptyView();
                }
            }
        });

    }
}
