package com.tanlong.exercise.ui.activity.view.recyclerview.viewholder.base;

/**
 *
 * Created by 龙 on 2017/3/1.
 */

public interface ItemViewDelegate<T> {
    /**
     * 获取ItemView对应的布局Id
     * @return Item对应的布局ID
     */
    int getItemViewLayoutId();

    /**
     * 是否是当前ViewType
     * @param item
     * @param position
     * @return
     */
    boolean isForViewType(T item, int position);

    /**
     * 进行View数据绑定
     * @param holder
     * @param t
     * @param position
     */
    void convert(ViewHolder holder, T t, int position);
}
