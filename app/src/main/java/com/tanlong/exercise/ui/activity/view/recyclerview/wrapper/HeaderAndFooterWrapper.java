package com.tanlong.exercise.ui.activity.view.recyclerview.wrapper;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.tanlong.exercise.ui.activity.view.recyclerview.adapter.base.MultiItemTypeAdapter;
import com.tanlong.exercise.ui.activity.view.recyclerview.util.WrapperUtils;
import com.tanlong.exercise.ui.activity.view.recyclerview.viewholder.base.ViewHolder;
import com.tanlong.exercise.util.LogTool;


/**
 * Created by 龙 on 2017/3/3.
 */

public class HeaderAndFooterWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();

    private RecyclerView.Adapter mInnerAdapter;
    private RecyclerView.Adapter mNotifyAdapter;

    /**
     * 对已有Adapter进行包装，使其具有添加Header和Footer能力
     * @param adapter -- 需包装的Adapter
     */
    public HeaderAndFooterWrapper(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            ViewHolder holder = ViewHolder.createViewHolder(parent.getContext(), mHeaderViews.get(viewType));
            return holder;

        } else if (mFootViews.get(viewType) != null) {
            ViewHolder holder = ViewHolder.createViewHolder(parent.getContext(), mFootViews.get(viewType));
            return holder;
        }

        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {//是否是HeaderView位置，
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPos(position)) {//是否是FooterView位置，
            return mFootViews.keyAt(position - getHeadersCount() - getRealItemCount());
        }
        return mInnerAdapter.getItemViewType(position - getHeadersCount());
    }

    /**
     * 获取真实Item数量，不包含HeaderView和FooterView数量
     * @return
     */
    private int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }

    /**
     * 传入位置是否是HeaderView的位置
     *
     * @param position -- 传入位置
     * @return
     */
    private boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }

    /**
     * 传入位置是否是FooterView的位置
     *
     * @param position -- 传入位置
     * @return
     */
    private boolean isFooterViewPos(int position) {
        return position >= getHeadersCount() + getRealItemCount();
    }

    /**
     * 获得Header View的个数
     *
     * @return
     */
    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    /**
     * 获得Footer View的个数
     *
     * @return
     */
    public int getFootersCount() {
        return mFootViews.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderViewPos(position)) {
            return;
        }
        if (isFooterViewPos(position)) {
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position - getHeadersCount());
    }

    @Override
    public int getItemCount() {//总Item数量 = HeaderView数量 + FooterView数量 + 真实数据数量
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    /**
     * Adapter附着到RecyclerView时调用，处理当布局管理器是GridLayoutManager时，HeaderView/FooterView长度为总列数
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        LogTool.e("test", "onAttachedToRecyclerView");
        mNotifyAdapter = recyclerView.getAdapter();
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {

            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                int viewType = getItemViewType(position);
                if (mHeaderViews.get(viewType) != null) {
                    return layoutManager.getSpanCount();
                } else if (mFootViews.get(viewType) != null) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null)
                    return oldLookup.getSpanSize(position);
                return 1;
            }
        });
    }

    /**
     * 处理当布局管理器是StaggeredGridLayoutManager时,HeaderView/FooterView长度为总列数
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        int position = holder.getLayoutPosition();
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        } else {
            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(false);
            }
        }
    }

    public void addHeaderView(View view) {
        int key = findHeaderKeyByView(view);
        if (key == -1) {
            mHeaderViews.put(mHeaderViews.size() + MultiItemTypeAdapter.VIEW_TYPE_HEADER, view);
            if (mNotifyAdapter != null)
                mNotifyAdapter.notifyDataSetChanged();
        }
    }

    public void deleteHeaderView(View view) {
        int key = findHeaderKeyByView(view);
        if (key != -1) {
            mHeaderViews.remove(key);
            if (mNotifyAdapter != null)
                mNotifyAdapter.notifyDataSetChanged();
        }
    }

    public void addFootView(View view) {
        int key = findFooterKeyByView(view);
        if (key == -1) {
            mFootViews.put(mFootViews.size() + MultiItemTypeAdapter.VIEW_TYPE_FOOTER, view);
            if (mNotifyAdapter != null) {
                mNotifyAdapter.notifyDataSetChanged();
            }
        }
    }

    public void deleteFooterView(View view) {
        int key = findFooterKeyByView(view);
        if (key != -1) {
            mFootViews.remove(key);
            if (mNotifyAdapter != null) {
                mNotifyAdapter.notifyDataSetChanged();
            }
        }
    }

    private int findHeaderKeyByView(View view) {
        for (int i = 0; i < mHeaderViews.size(); i++) {
            int key = mHeaderViews.keyAt(i);
            if (mHeaderViews.get(key) == view) {
                return key;
            }
        }
        return -1;
    }

    private int findFooterKeyByView(View view) {
        for (int i = 0; i < mFootViews.size(); i++) {
            int key = mFootViews.keyAt(i);
            if (mFootViews.get(key) == view) {
                return key;
            }
        }
        return -1;
    }
}
