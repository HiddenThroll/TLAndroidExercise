package com.tanlong.exercise.ui.activity.view.recyclerview.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tanlong.exercise.ui.activity.view.recyclerview.entity.SectionData;
import com.tanlong.exercise.ui.activity.view.recyclerview.viewholder.base.ItemViewDelegate;
import com.tanlong.exercise.ui.activity.view.recyclerview.viewholder.base.ItemViewDelegateManager;
import com.tanlong.exercise.ui.activity.view.recyclerview.viewholder.base.ViewHolder;

import java.util.List;

/**
 * RecyclerView的多类型Item通用Adapter
 * Created by 龙 on 2017/3/2.
 */

public class MultiItemTypeAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    // 统一管理
    public static final int VIEW_TYPE_HEADER = 100000;
    /** Footer View类型*/
    public static final int VIEW_TYPE_FOOTER = 200000;
    /** Section头类型*/
    public static final int VIEW_TYPE_SECTION_HEADER = 300000;//为防止与其他viewtype冲突, 使用一个较大的值
    /** Empty View类型*/
    public static final int VIEW_TYPE_EMPTY = 400000;

    protected Context mContext;
    protected List<T> mDatas;
    protected View mEmptyView;
    protected int mEmptyLayoutId;
    protected OnRefreshEmptyView onRefreshEmptyView;

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
        if (isEmpty()) {//先判断数据源是否为空
            return VIEW_TYPE_EMPTY;
        }

        if (!useItemViewDelegateManager()) {//判断是否使用ItemViewDelegateManager
            return super.getItemViewType(position);//不使用，调用父类方法
        } else {//使用，调用ItemViewDelegateManager.getItemViewType方法
            return mItemViewDelegateManager.getItemViewType(mDatas.get(position), position);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_EMPTY) {
            return onCreateEmptyViewHolder(parent, viewType);
        }

        int layoutId = mItemViewDelegateManager.getItemViewLayoutId(viewType);
        return ViewHolder.createViewHolder(mContext, parent, layoutId);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (isEmpty()) {
            onBindEmptyViewHolder(holder, position);
        } else {
            convert(holder, mDatas.get(position));
        }
    }

    public void convert(ViewHolder holder, T t) {
        mItemViewDelegateManager.convert(holder, t, holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        if (isEmpty()) {//这里返回的1，是Empty View
            return 1;
        } else {
            return mDatas.size();
        }
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

    /**
     * 数据源是否为空
     * @return
     */
    protected boolean isEmpty() {
        return (mEmptyView != null || mEmptyLayoutId != 0) && mDatas.size() == 0;
    }

    public void setmEmptyView(View mEmptyView) {
        this.mEmptyView = mEmptyView;
    }

    public void setmEmptyLayoutId(int mEmptyLayoutId) {
        this.mEmptyLayoutId = mEmptyLayoutId;
    }

    public void setOnRefreshEmptyView(OnRefreshEmptyView onRefreshEmptyView) {
        this.onRefreshEmptyView = onRefreshEmptyView;
    }

    protected ViewHolder onCreateEmptyViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder;
        if (mEmptyView != null) {
            holder = ViewHolder.createViewHolder(mContext, mEmptyView);
        } else {
            holder = ViewHolder.createViewHolder(mContext, parent, mEmptyLayoutId);
        }
        return holder;
    }

    /**
     * 由子类选择覆写
     * @param holder
     * @param position
     */
    protected void onBindEmptyViewHolder(ViewHolder holder, int position) {

    }

    public interface OnRefreshEmptyView {
        void onRefreshEmptyView();
    }
}
