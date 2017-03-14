package com.tanlong.exercise.ui.activity.view.recyclerview.layoutmanager;

import android.support.v7.widget.RecyclerView;

/**
 * 自定义ILayoutManager需实现接口
 * Created by 龙 on 2017/3/14.
 */

public interface ILayoutManager {
    /**
     * 获得LayoutManager
     * @return
     */
    RecyclerView.LayoutManager getLayoutManager();

    /**
     * 获得 最后一个 可见Item 的 位置
     * @return
     */
    int findLastVisiblePosition();
}
