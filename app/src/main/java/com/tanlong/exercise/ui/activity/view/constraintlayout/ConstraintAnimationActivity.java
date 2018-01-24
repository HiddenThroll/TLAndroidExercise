package com.tanlong.exercise.ui.activity.view.constraintlayout;

import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.transition.TransitionManager;
import android.view.View;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 龙 on 2017/8/3.
 */

public class ConstraintAnimationActivity extends BaseActivity {

    @BindView(R.id.cl_main)
    ConstraintLayout clMain;

    ConstraintSet applyConstraintSet = new ConstraintSet();
    ConstraintSet resetConstraintSet = new ConstraintSet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_constraint_animation);
        ButterKnife.bind(this);

        applyConstraintSet.clone(clMain);
        resetConstraintSet.clone(clMain);

    }

    @OnClick({R.id.btn_apply, R.id.btn_reset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_apply:
                apply();
                break;
            case R.id.btn_reset:
                reset();
                break;
        }
    }

    private void apply() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(clMain);// 为动画添加延时效果
        }
        // 改变按钮1左边距
//        applyConstraintSet.setMargin(R.id.btn_first, ConstraintSet.START, 8);

        // 全部按钮居中, 先清除左右边距
//        applyConstraintSet.setMargin(R.id.btn_first, ConstraintSet.START, 0);
//        applyConstraintSet.setMargin(R.id.btn_first, ConstraintSet.END, 0);
//        applyConstraintSet.setMargin(R.id.btn_second, ConstraintSet.START, 0);
//        applyConstraintSet.setMargin(R.id.btn_second, ConstraintSet.END, 0);
//        applyConstraintSet.setMargin(R.id.btn_third, ConstraintSet.START, 0);
//        applyConstraintSet.setMargin(R.id.btn_third, ConstraintSet.END, 0);
//
//        applyConstraintSet.centerHorizontally(R.id.btn_first, R.id.cl_main);
//        applyConstraintSet.centerHorizontally(R.id.btn_second, R.id.cl_main);
//        applyConstraintSet.centerHorizontally(R.id.btn_third, R.id.cl_main);

        // 按钮3居中
//        applyConstraintSet.setMargin(R.id.btn_third, ConstraintSet.START, 0);
//        applyConstraintSet.setMargin(R.id.btn_third, ConstraintSet.END, 0);
//        applyConstraintSet.setMargin(R.id.btn_third, ConstraintSet.TOP, 0);
//        applyConstraintSet.setMargin(R.id.btn_third, ConstraintSet.BOTTOM, 0);
//
//        applyConstraintSet.centerHorizontally(R.id.btn_third, R.id.cl_main);
//        applyConstraintSet.centerVertically(R.id.btn_third, R.id.cl_main);

        // 设置所有按钮宽度为600px
//        applyConstraintSet.constrainWidth(R.id.btn_first, 600);
//        applyConstraintSet.constrainWidth(R.id.btn_second, 600);
//        applyConstraintSet.constrainWidth(R.id.btn_third, 600);

        // 按钮2 3消失，按钮1 全屏显示
//        applyConstraintSet.setVisibility(R.id.btn_second, ConstraintSet.GONE);
//        applyConstraintSet.setVisibility(R.id.btn_third, ConstraintSet.GONE);
//
//        applyConstraintSet.clear(R.id.btn_first);//清空约束
//        applyConstraintSet.connect(R.id.btn_first, ConstraintSet.LEFT, R.id.cl_main, ConstraintSet.LEFT, 0);//最后一个参数是外边距
//        applyConstraintSet.connect(R.id.btn_first, ConstraintSet.RIGHT, R.id.cl_main, ConstraintSet.RIGHT, 0);
//        applyConstraintSet.connect(R.id.btn_first, ConstraintSet.TOP, R.id.cl_main, ConstraintSet.TOP, 0);
//        applyConstraintSet.connect(R.id.btn_first, ConstraintSet.BOTTOM, R.id.cl_main, ConstraintSet.BOTTOM, 0);

        // 全部按钮与屏幕的顶端对齐并且水平居中
        applyConstraintSet.clear(R.id.btn_first);
        applyConstraintSet.clear(R.id.btn_second);
        applyConstraintSet.clear(R.id.btn_third);

        applyConstraintSet.connect(R.id.btn_first, ConstraintSet.LEFT, R.id.cl_main,
                ConstraintSet.LEFT, 0);//button 1 左对齐
        applyConstraintSet.connect(R.id.btn_first, ConstraintSet.TOP, R.id.cl_main,
                ConstraintSet.TOP, 0);
        applyConstraintSet.connect(R.id.btn_third, ConstraintSet.RIGHT, R.id.cl_main,
                ConstraintSet.RIGHT, 0);//button 3 右对齐
        applyConstraintSet.connect(R.id.btn_third, ConstraintSet.TOP, R.id.cl_main,
                ConstraintSet.TOP, 0);
        // 建立 Chain
        applyConstraintSet.connect(R.id.btn_second, ConstraintSet.LEFT, R.id.btn_first,
                ConstraintSet.RIGHT, 0);
        applyConstraintSet.connect(R.id.btn_first, ConstraintSet.RIGHT, R.id.btn_second,
                ConstraintSet.LEFT, 0);

        applyConstraintSet.connect(R.id.btn_second, ConstraintSet.RIGHT, R.id.btn_third,
                ConstraintSet.LEFT, 0);
        applyConstraintSet.connect(R.id.btn_third, ConstraintSet.LEFT, R.id.btn_second,
                ConstraintSet.RIGHT, 0);

        // 设置Chain样式
        applyConstraintSet.createHorizontalChain(
                ConstraintSet.PARENT_ID, ConstraintSet.LEFT,  // 整个Chain的头部要连接到哪一个控件/Parent id 的 哪一边
                ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, // 整个Chain的尾部要连接到哪一个控件/Parent id 的 哪一边
                new int[]{R.id.btn_first, R.id.btn_second,
                R.id.btn_third}, null, ConstraintWidget.CHAIN_PACKED);

        // 恢复所有按钮的宽高
        applyConstraintSet.constrainWidth(R.id.btn_first, ConstraintSet.WRAP_CONTENT);
        applyConstraintSet.constrainWidth(R.id.btn_second, ConstraintSet.WRAP_CONTENT);
        applyConstraintSet.constrainWidth(R.id.btn_third, ConstraintSet.WRAP_CONTENT);

        applyConstraintSet.constrainHeight(R.id.btn_first, ConstraintSet.WRAP_CONTENT);
        applyConstraintSet.constrainHeight(R.id.btn_second, ConstraintSet.WRAP_CONTENT);
        applyConstraintSet.constrainHeight(R.id.btn_third, ConstraintSet.WRAP_CONTENT);

        applyConstraintSet.applyTo(clMain);
    }

    private void reset() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(clMain);
        }
        resetConstraintSet.applyTo(clMain);
    }
}
