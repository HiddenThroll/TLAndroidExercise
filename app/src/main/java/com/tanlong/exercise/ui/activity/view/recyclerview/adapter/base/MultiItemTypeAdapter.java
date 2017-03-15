package com.tanlong.exercise.ui.activity.view.recyclerview.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tanlong.exercise.ui.activity.view.recyclerview.viewholder.base.ItemViewDelegate;
import com.tanlong.exercise.ui.activity.view.recyclerview.viewholder.base.ItemViewDelegateManager;
import com.tanlong.exercise.ui.activity.view.recyclerview.viewholder.base.ViewHolder;

import java.util.List;

/**
 * RecyclerView的多类型Item通用Adapter
 * Created by 龙 on 2017/3/2.
 */

public class MultiItemTypeAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected Context mContext;
    protected List<T> mDatas;

    protected ItemViewDelegateManager mItemViewDelegateManager;

    public MultiItemTypeAdapter(Context mContext, List<T> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mItemViewDelegateManager = new ItemViewDelegateManager();
    }

    /**
     * 获得ItemViewType
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (!useItemViewDelegateManager()) {//判断是否使用ItemViewDelegateManager
            return super.getItemViewType(position);//不使用，调用父类方法
        } else {//使用，调用ItemViewDelegateManager.getItemViewType方法
            return mItemViewDelegateManager.getItemViewType(mDatas.get(position), position);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = mItemViewDelegateManager.getItemViewLayoutId(viewType);
        return ViewHolder.createViewHolder(mContext, parent, layoutId);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        convert(holder, mDatas.get(position));
    }

    public void convert(ViewHolder holder, T t) {
        mItemViewDelegateManager.convert(holder, t, holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 是否使用ItemViewDelegateManager
     * @return 当ItemViewDelegateManager中有ItemViewDelegate时返回true，否则返回false
     */
    protected boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }

    public MultiItemTypeAdapter addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    public MultiItemTypeAdapter addItemViewDelegate(int viewType, ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate, viewType);
        return this;
    }

    public void addDataAll(List data) {
        mDatas.addAll(data);
    }

    public void clearData() {
        mDatas.clear();
    }
}
