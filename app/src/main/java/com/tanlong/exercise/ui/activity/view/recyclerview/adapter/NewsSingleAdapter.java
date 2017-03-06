package com.tanlong.exercise.ui.activity.view.recyclerview.adapter;

import android.content.Context;

import com.tanlong.exercise.R;
import com.tanlong.exercise.model.entity.NewsItem;
import com.tanlong.exercise.ui.activity.view.recyclerview.adapter.base.CommonAdapter;
import com.tanlong.exercise.ui.activity.view.recyclerview.viewholder.base.ViewHolder;

import java.util.List;

/**
 *
 * Created by 龙 on 2017/3/3.
 */

public class NewsSingleAdapter extends CommonAdapter<NewsItem>{

    public NewsSingleAdapter(Context mContext, List<NewsItem> mDatas, int layoutId) {
        super(mContext, mDatas, layoutId);
    }

    @Override
    protected void convert(ViewHolder holder, NewsItem item, int position) {
        holder.setText(R.id.tv_news_title, item.getTitle());
        holder.setText(R.id.tv_news_summary, item.getSummary());
    }
}
