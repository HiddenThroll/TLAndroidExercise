package com.tanlong.exercise.ui.activity.view.recyclerview.divider;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 线性布局使用的常用分割线
 * Created by 龙 on 2017/2/20.
 */

public class LinearDividerItemDecoration extends RecyclerView.ItemDecoration {
    private final String TAG = getClass().getSimpleName();
    private int leftRight;
    private int topBottom;

    private Drawable mDivider;

    public LinearDividerItemDecoration(int leftRight, int topBottom) {
        this.leftRight = leftRight;
        this.topBottom = topBottom;
    }


    public LinearDividerItemDecoration(int leftRight, int topBottom, int color) {
        this.leftRight = leftRight;
        this.topBottom = topBottom;
        if (color != 0) {
            mDivider = new ColorDrawable(color);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        //没有子view或者没有没有颜色直接return
        if (mDivider == null || layoutManager.getChildCount() == 0) {
            return;
        }

        int left, right, top, bottom;
        final int childCount = parent.getChildCount();
        if (layoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
            for (int i = 0; i < childCount - 1; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                //计算Divider绘制范围
                left = parent.getPaddingLeft() + params.leftMargin;
                right = parent.getWidth() - parent.getPaddingRight() - params.rightMargin;
                top = child.getBottom() + params.bottomMargin +
                        Math.round(ViewCompat.getTranslationY(child));
                bottom = top + topBottom;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);//绘制Divider
            }
        } else {
            for (int i = 0; i < childCount - 1; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                //计算Divider绘制范围
                left = (int) (child.getRight() + params.rightMargin +
                        Math.round(ViewCompat.getTranslationX(child)));
                right = left + leftRight;
                top = parent.getPaddingTop() + params.topMargin;
                bottom = parent.getHeight() - parent.getPaddingBottom() - params.bottomMargin;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
        // getItemOffsets 中为 outRect 设置的4个方向的值，将被计算进所有 decoration 的尺寸中，
        // 而这个尺寸，被计入了 RecyclerView 每个 item view 的 padding 中
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        //竖直方向的
        if (layoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, 0, topBottom);
        } else {
            outRect.set(0, 0, leftRight, 0);
        }
    }
}
