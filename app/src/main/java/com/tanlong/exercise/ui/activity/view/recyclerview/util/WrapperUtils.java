package com.tanlong.exercise.ui.activity.view.recyclerview.util;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

/**
 * Created by 龙 on 2017/3/6.
 */

public class WrapperUtils {
    /**
     * 当布局管理器是GridLayoutManager时，设置每个Item的长度占几个cell（HeaderView/FooterView为总列数，普通Item为1个cell）
     */
    public interface SpanSizeCallback {
        int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position);
    }

    /**
     * Adapter附着到RecyclerView时调用，处理当布局管理器是GridLayoutManager时，HeaderView/FooterView长度为总列数
     * @param innerAdapter -- 被包装的Adapter
     * @param recyclerView -- RecyclerView
     * @param callback -- 回调
     */
    public static void onAttachedToRecyclerView(RecyclerView.Adapter innerAdapter, RecyclerView recyclerView, final SpanSizeCallback callback) {
        innerAdapter.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return callback.getSpanSize(gridLayoutManager, spanSizeLookup, position);
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }
}
