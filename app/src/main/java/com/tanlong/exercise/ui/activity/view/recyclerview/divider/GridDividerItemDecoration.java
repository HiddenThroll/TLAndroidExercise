package com.tanlong.exercise.ui.activity.view.recyclerview.divider;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 *
 * Created by 龙 on 2017/3/10.
 */

public class GridDividerItemDecoration extends RecyclerView.ItemDecoration {

    private final String TAG = getClass().getSimpleName();
    private int leftRight;
    private int topBottom;

    private Drawable mDivider;

    public GridDividerItemDecoration(int leftRight, int topBottom) {
        this.leftRight = leftRight;
        this.topBottom = topBottom;
    }

    public GridDividerItemDecoration(int leftRight, int topBottom, int color) {
        this.leftRight = leftRight;
        this.topBottom = topBottom;
        if (color != 0) {
            mDivider = new ColorDrawable(color);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        if (mDivider == null || layoutManager.getChildCount() == 0) {
            return;
        }
        if (layoutManager.getOrientation() == GridLayoutManager.VERTICAL) {
            drawDividerInVerticalMode(c, parent, state);
        } else {
            drawDividerInHorizontalMode(c, parent, state);
        }
    }

    /**
     * 竖直模式下，绘制分割线
     * @param c
     * @param parent
     * @param state
     */
    private void drawDividerInVerticalMode(Canvas c, RecyclerView parent, RecyclerView.State state) {
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        int totalCount = layoutManager.getItemCount();
        int surplusCount = totalCount % layoutManager.getSpanCount();
        int childCount = parent.getChildCount();

        int left;
        int right;
        int top;
        int bottom;

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            //得到它在总数里面的位置
            int position = parent.getChildAdapterPosition(child);
            // 是否是最后一行
            boolean isLast = surplusCount == 0 ?
                    position > totalCount - layoutManager.getSpanCount() - 1 :
                    position > totalCount - surplusCount - 1;
            // 是否是最后一列
            //能被整除的不需要右边的分割线,并且当Item总数量比列数小的时候最后一项不需要右边
            boolean first = totalCount >= layoutManager.getSpanCount()//Item总数量比列数多或一样
                    && (position + 1) % layoutManager.getSpanCount() != 0;//该Item位置不能被整除
            boolean second = totalCount < layoutManager.getSpanCount()//Item总数量比列数少
                    && position + 1 != totalCount;//该Item不是最后一个
            // 绘制水平分割线
            if (!isLast){
                if (first || second) {
                    right = child.getRight() + leftRight;
                } else {//最后一个不需要多出的那一点，即leftRight
                    right = child.getRight();
                }
                left = child.getLeft();
                top = child.getBottom() + params.bottomMargin;
                bottom = top + topBottom;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }

            // 绘制竖直分割线
            if (first || second) {//不是最后一个
                left = child.getRight();
                right = left + leftRight;
                top = child.getTop();
                bottom = child.getBottom();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    private void drawDividerInHorizontalMode(Canvas c, RecyclerView parent, RecyclerView.State state) {
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        int totalCount = layoutManager.getItemCount();
        int surplusCount = totalCount % layoutManager.getSpanCount();
        int childCount = parent.getChildCount();

        int left;
        int right;
        int top;
        int bottom;
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            //得到它在总数里面的位置
            int position = parent.getChildAdapterPosition(child);
            // 是否为最后一列
            boolean isLast = surplusCount == 0 ?
                    position > totalCount - layoutManager.getSpanCount() - 1 :
                    position > totalCount - surplusCount - 1;
            // 是否为最后一行
            boolean first = totalCount > layoutManager.getSpanCount() && (position + 1) % layoutManager.getSpanCount() != 0;
            boolean second = totalCount < layoutManager.getSpanCount() && position + 1 != totalCount;
            // 绘制垂直线
            if (!isLast) {//不是最后一列
                left = child.getRight();
                right = left + leftRight;
                top = child.getTop();
                bottom = child.getBottom();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }

            // 绘制水平线
            if (first || second) {//不是最后一行
                if (isLast) {// 是最后一列
                    right = child.getRight();
                } else {
                    right = child.getRight() + leftRight;
                }
                left = child.getLeft();
                top = child.getBottom() + params.bottomMargin;
                bottom = top + topBottom;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        //判断Item总数量是否可以被总列数整除
        int totalCount = layoutManager.getItemCount();
        int surplusCount = totalCount % layoutManager.getSpanCount();
        int childPosition = parent.getChildAdapterPosition(view);

        if (layoutManager.getOrientation() == GridLayoutManager.VERTICAL) {//竖直方向布局
            if (surplusCount == 0 && childPosition > totalCount - layoutManager.getSpanCount() - 1) {
                // 可以被整除并且是最后一行，不需要bottom
                outRect.bottom = 0;
            } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1){
                // 不能被整除并且是最后一行，不需要bottom
                outRect.bottom = 0;
            } else {//其他情况，需要bottom
                outRect.bottom = topBottom;
            }

            if ((childPosition + 1) % layoutManager.getSpanCount() == 0) {//可以被整除的，不需要右边
                outRect.right = 0;
            } else {
                outRect.right = leftRight;
            }

            outRect.left = 0;
            outRect.top = 0;
        } else {//水平方向布局
            if (surplusCount == 0 && childPosition > totalCount - layoutManager.getSpanCount() - 1) {
                //可以被整除并且是最后一列，不需要右边
                outRect.right = 0;
            } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1) {
                //不能被整除并且是最后一列，不需要右边
                outRect.right = 0;
            } else {
                outRect.right = leftRight;
            }

            if ((childPosition + 1) % layoutManager.getSpanCount() == 0) {//可以被整除的，不需要bottom
                outRect.bottom = 0;
            } else {
                outRect.bottom = topBottom;
            }

            outRect.left = 0;
            outRect.top = 0;
        }
    }
}
