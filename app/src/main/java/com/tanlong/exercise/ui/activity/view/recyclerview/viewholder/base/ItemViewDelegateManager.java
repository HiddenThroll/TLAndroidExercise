package com.tanlong.exercise.ui.activity.view.recyclerview.viewholder.base;

import android.support.v4.util.SparseArrayCompat;

/**
 * Created by 龙 on 2017/3/1.
 */

public class ItemViewDelegateManager<T> {

    SparseArrayCompat<ItemViewDelegate<T>> delegates = new SparseArrayCompat<>();

    public int getItemViewDelegateCount() {
        return delegates.size();
    }

    /**
     * 添加ItemViewDelegate
     *
     * @param itemViewDelegate -- 添加的数据
     * @return
     */
    public ItemViewDelegateManager<T> addDelegate(ItemViewDelegate<T> itemViewDelegate) {
        int viewType = delegates.size();
        if (itemViewDelegate != null) {
            delegates.put(viewType, itemViewDelegate);
        }
        return this;
    }

    /**
     * 在指定位置添加ItemViewDelegate
     *
     * @param itemViewDelegate -- 添加的数据
     * @param viewType         -- 指定的位置
     * @return
     */
    public ItemViewDelegateManager<T> addDelegate(ItemViewDelegate<T> itemViewDelegate, int viewType) {
        if (delegates.get(viewType) == null) {
            delegates.put(viewType, itemViewDelegate);
        }
        return this;
    }

    /**
     * 移除指定的ItemViewDelegate
     *
     * @param itemViewDelegate -- 移除的ItemViewDelegate
     * @return
     */
    public ItemViewDelegateManager<T> removeItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        if (itemViewDelegate == null) {
            return this;
        }
        int index = delegates.indexOfValue(itemViewDelegate);
        if (index >= 0) {
            delegates.removeAt(index);
        }
        return this;
    }

    /**
     * 移除指定类型的ItemViewDelegate
     *
     * @param itemType -- 指定的类型
     * @return
     */
    public ItemViewDelegateManager<T> removeItemViewDelegate(int itemType) {
        int index = delegates.indexOfKey(itemType);
        if (index >= 0) {
            delegates.removeAt(index);
        }
        return this;
    }

    public void convert(ViewHolder viewHolder, T item, int position) {
        int count = delegates.size();
        for (int i = 0; i < count; i++) {
            ItemViewDelegate<T> delegate = delegates.valueAt(i);
            if (delegate.isForViewType(item, position)) {
                delegate.convert(viewHolder, item, position);
                return;
            }
        }
    }

    public int getItemViewLayoutId(int viewType) {
        return delegates.get(viewType).getItemViewLayoutId();
    }

    public int getItemViewType(ItemViewDelegate itemViewDelegate) {
        return delegates.indexOfValue(itemViewDelegate);
    }

    public int getItemViewType(T item, int position) {
        int delegatesCount = delegates.size();
        for (int i = delegatesCount - 1; i >= 0; i--) {
            ItemViewDelegate<T> delegate = delegates.valueAt(i);
            if (delegate.isForViewType(item, position)) {
                return delegates.keyAt(i);
            }
        }
        throw new IllegalArgumentException(
                "No ItemViewDelegate added that matches position=" + position + " in data source");
    }

}
