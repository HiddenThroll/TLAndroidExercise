package com.tanlong.exercise.ui.activity.view.recyclerview.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.tanlong.exercise.ui.activity.view.recyclerview.viewholder.base.ItemViewDelegate;
import com.tanlong.exercise.ui.activity.view.recyclerview.viewholder.base.ViewHolder;

import java.util.List;

/**
 * 单个Item布局使用的通用Adapter
 * Created by 龙 on 2017/3/1.
 */

public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> {

    protected int mLayoutId;
    protected LayoutInflater mInflater;

    public CommonAdapter(Context mContext, List<T> mDatas, int layoutId) {
        super(mContext, mDatas);
        this.mLayoutId = layoutId;
        mInflater = LayoutInflater.from(mContext);

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return mLayoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;//单个Item布局，直接返回true
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder holder, T item, int position);
}
