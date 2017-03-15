package com.tanlong.exercise.ui.activity.view.recyclerview.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.model.entity.NewsItem;
import com.tanlong.exercise.ui.activity.view.recyclerview.adapter.base.CommonAdapter;
import com.tanlong.exercise.ui.activity.view.recyclerview.viewholder.base.ViewHolder;
import com.tanlong.exercise.util.LogTool;
import com.tanlong.exercise.util.ToastHelp;

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
    protected void convert(ViewHolder holder, NewsItem item, final int position) {
        holder.setText(R.id.tv_news_title_2, item.getTitle());
        holder.setText(R.id.tv_news_summary, item.getSummary());

        ImageView ivIcon = holder.getView(R.id.iv_news_left_picture);
        ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastHelp.showShortMsg(mContext, "position is " + position + " title is " + mDatas.get(position).getTitle());
                LogTool.e("NewsSingleAdapter", "position is " + position + " title is " + mDatas.get(position).getTitle());
            }
        });
    }
}
