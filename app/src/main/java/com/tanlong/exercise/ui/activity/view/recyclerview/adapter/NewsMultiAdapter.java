package com.tanlong.exercise.ui.activity.view.recyclerview.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tanlong.exercise.R;
import com.tanlong.exercise.model.entity.NewsItem;
import com.tanlong.exercise.ui.activity.view.recyclerview.adapter.base.MultiItemTypeAdapter;
import com.tanlong.exercise.ui.activity.view.recyclerview.viewholder.base.ItemViewDelegate;
import com.tanlong.exercise.ui.activity.view.recyclerview.viewholder.base.ViewHolder;

import java.util.List;

/**
 * Created by 龙 on 2017/3/2.
 */

public class NewsMultiAdapter extends MultiItemTypeAdapter<NewsItem> {

    public NewsMultiAdapter(Context mContext, List<NewsItem> mDatas) {
        super(mContext, mDatas);
        mItemViewDelegateManager.addDelegate(new NewsTypeOneDelegate());
        mItemViewDelegateManager.addDelegate(new NewsTypeTwoDelegate());
        mItemViewDelegateManager.addDelegate(new NewsTypeThreeDelegate());
    }


    public class NewsTypeOneDelegate implements ItemViewDelegate<NewsItem> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_news_type_1;
        }

        @Override
        public boolean isForViewType(NewsItem item, int position) {
            return item.getType() == NewsItem.NEWS_TYPE_1;
        }

        @Override
        public void convert(ViewHolder holder, NewsItem newsItem, int position) {
            holder.setText(R.id.tv_news_title, newsItem.getTitle());
            ImageView imageView = holder.getView(R.id.iv_news_title_picture);
//            Glide.with(mContext).load(R.mipmap.ic_launcher).into(imageView);
        }
    }

    public class NewsTypeTwoDelegate implements ItemViewDelegate<NewsItem> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_news_type_2;
        }

        @Override
        public boolean isForViewType(NewsItem item, int position) {
            return item.getType() == NewsItem.NEWS_TYPE_2;
        }

        @Override
        public void convert(ViewHolder holder, NewsItem newsItem, int position) {
            ImageView imageView = holder.getView(R.id.iv_news_left_picture);
//            Glide.with(mContext).load(R.mipmap.ic_launcher).into(imageView);
            holder.setText(R.id.tv_news_title, newsItem.getTitle());
            holder.setText(R.id.tv_news_summary, newsItem.getSummary());
        }
    }

    public class NewsTypeThreeDelegate implements ItemViewDelegate<NewsItem> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_news_type_3;
        }

        @Override
        public boolean isForViewType(NewsItem item, int position) {
            return item.getType() == NewsItem.NEWS_TYPE_3;
        }

        @Override
        public void convert(ViewHolder holder, NewsItem newsItem, int position) {
            holder.setText(R.id.tv_news_title, newsItem.getTitle());

            ImageView ivPic1 = holder.getView(R.id.iv_news_content_picture_1);
            ImageView ivPic2 = holder.getView(R.id.iv_news_content_picture_2);
            ImageView ivPic3 = holder.getView(R.id.iv_news_content_picture_3);
//            Glide.with(mContext).load(R.mipmap.ic_launcher).into(ivPic1);
//            Glide.with(mContext).load(R.mipmap.ic_launcher).into(ivPic2);
//            Glide.with(mContext).load(R.mipmap.ic_launcher).into(ivPic3);

        }
    }
}
