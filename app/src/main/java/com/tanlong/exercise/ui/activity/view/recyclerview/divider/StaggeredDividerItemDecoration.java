package com.tanlong.exercise.ui.activity.view.recyclerview.divider;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * 瀑布流布局使用的常用分割线
 * Created by 龙 on 2017/2/21.
 */

public class StaggeredDividerItemDecoration extends RecyclerView.ItemDecoration {
    private final String TAG = getClass().getSimpleName();
    private int leftRight;
    private int topBottom;

    private Drawable mDivider;

    public StaggeredDividerItemDecoration(int leftRight, int topBottom) {
        this.leftRight = leftRight;
        this.topBottom = topBottom;
    }

    public StaggeredDividerItemDecoration(int leftRight, int topBottom, int color) {
        this.leftRight = leftRight;
        this.topBottom = topBottom;
        if (color != 0) {
            mDivider = new ColorDrawable(color);
        }

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) parent.getLayoutManager();
        if (mDivider == null || layoutManager.getChildCount() == 0) {
            return;
        }
        int left = 0;
        int right;
        int top = 0;
        int bottom;

        final int spanCount = layoutManager.getSpanCount();
        final int childCount = parent.getChildCount();

        //换个思路，每个item都画上边和右边，当然第一排的不需要上边，最右边的不需要右边
        if (layoutManager.getOrientation() == StaggeredGridLayoutManager.VERTICAL) {
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) child.getLayoutParams();
                //得到它在总数里面的位置
                final int position = parent.getChildAdapterPosition(child);
                //它在每列的位置
                final int spanPosition = params.getSpanIndex();
                //画上边的横线
                if (position > spanCount - 1) {//不是第一行
                    left = child.getLeft();
                    right = child.getRight();
                    top = (child.getTop() - topBottom);
                    bottom = child.getTop();
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }

                //画右边的竖线
                if ((spanPosition + 1) % spanCount != 0) {//不是最后一列
                    left = child.getRight();
                    right = left + leftRight;
                    if (position > spanCount - 1) {//不是第一排的，要冒出来一些
                        top = child.getTop() - topBottom;
                    } else {//第一排的不需要上面那一丢丢
                        top = child.getTop();
                    }
                    bottom = child.getBottom();
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
            }
        } else {
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) child.getLayoutParams();
                //得到它在总数里面的位置
                final int position = parent.getChildAdapterPosition(child);
                //它在每列的位置
                final int spanPosition = params.getSpanIndex();
                //画左边
                if (position > spanCount - 1) {//不是第一列
                    left =  (child.getLeft() - leftRight);
                    right = child.getLeft();
                    top =  (child.getTop());
                    bottom = child.getBottom();
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }

                //画bottom的
                if (spanPosition > 0) {
                    if (position > spanCount - 1) {//不是第一列
                        left = child.getLeft() - leftRight;
                    } else {
                        left = child.getLeft();
                    }
                    right = child.getRight();
                    top =  (child.getTop() - topBottom);
                    bottom = child.getTop();
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
            }

        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) parent.getLayoutManager();
        final StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
        final int childPosition = parent.getChildAdapterPosition(view);
        final int spanCount = layoutManager.getSpanCount();
        //这边我们换下思路，除第一排以外的Item都有top，除最右边以外的Item都有right
        if (layoutManager.getOrientation() == StaggeredGridLayoutManager.VERTICAL) {
            if (childPosition < spanCount) {//第一排的不需要上面
                outRect.top = 0;
            } else {
                outRect.top = topBottom;
            }
            if (layoutParams.getSpanIndex() == spanCount - 1) {//最边上的不需要右边
                outRect.right = 0;
            } else {
                outRect.right = leftRight;
            }
            outRect.bottom = 0;
            outRect.left = 0;
        } else {
            //这边我们换下思路，除第一列以外的Item都有left,除最后一行以外的Item都有bottom
            if (childPosition < spanCount) {//第一列的不需要left
                outRect.left = 0;
            } else {
                outRect.left = leftRight;
            }

            if (layoutParams.getSpanIndex() == spanCount - 1) {//最后一行的不需要bottom
                outRect.bottom = 0;
            } else {
                outRect.bottom = topBottom;
            }
            outRect.right = 0;
            outRect.top = 0;
        }

    }
}
