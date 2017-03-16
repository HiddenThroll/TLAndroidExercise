package com.tanlong.exercise.ui.activity.view.recyclerview.adapter.base;


import android.content.Context;
import android.view.ViewGroup;

import com.tanlong.exercise.ui.activity.view.recyclerview.entity.SectionData;
import com.tanlong.exercise.ui.activity.view.recyclerview.viewholder.base.ViewHolder;

import java.util.List;

/**
 *
 * Created by 龙 on 2017/3/15.
 */

public abstract class SectionAdapter<T> extends MultiItemTypeAdapter<SectionData<T>> {
    private int mSectionHeaderLayoutId;

    public SectionAdapter(Context mContext, List<SectionData<T>> mDatas, int sectionHeaderLayoutId) {
        super(mContext, mDatas);
        mSectionHeaderLayoutId = sectionHeaderLayoutId;
    }


    @Override
    public int getItemViewType(int position) {
        if (isEmpty()) {
            return VIEW_TYPE_EMPTY;
        } else if (mDatas.get(position).isHeader()) {
            return VIEW_TYPE_SECTION_HEADER;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isEmpty()) {
            return onCreateEmptyViewHolder(parent, viewType);
        } else if (viewType == VIEW_TYPE_SECTION_HEADER) {
            return onCreateSectionHeaderViewHolder(parent);
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (isEmpty()) {
            onBindEmptyViewHolder(holder, position);
        } else if (mDatas.get(position).isHeader()) {
            onBindSectionHeaderViewHolder(holder, mDatas.get(position), position);
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    /**
     * 创建Section Header使用的ViewHolder
     * @param parent
     * @return
     */
    private ViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent) {
        return ViewHolder.createViewHolder(mContext, parent, mSectionHeaderLayoutId);
    }

    protected abstract void onBindSectionHeaderViewHolder(ViewHolder holder, SectionData sectionData, int position);
}
