package com.tanlong.exercise.ui.activity.view.animator.mdanimator;

import android.animation.Animator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;
import com.tanlong.exercise.util.ToastHelp;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/7/31.
 */

public class CircularRevealActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;
    @Bind(R.id.iv_oval)
    ImageView ivOval;
    @Bind(R.id.iv_rect)
    ImageView ivRect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_circular_reveal);
        ButterKnife.bind(this);

        tvTitle.setText("圆形动画效果");
        btnHelp.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.iv_back, R.id.btn_help, R.id.iv_oval, R.id.iv_rect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                showTips();
                break;
            case R.id.iv_oval:
                showOvalAnimator();
                break;
            case R.id.iv_rect:
                showRectAnimator();
                break;
        }
    }

    public void showTips() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("实现CircularReveal动画效果:\n")
                .append("ViewAnimationUtils.createCircularReveal(View view, int centerX, int centerY, float startRadius, float endRadius)\n")
                .append("参数意义分别是\n")
                .append("(1) 目标View\n")
                .append("(2) 动画开始中心点X\n")
                .append("(3) 动画开始中心点Y\n")
                .append("(4) 动画开始半径\n")
                .append("(5) 动画结束半径");
        ShowTipsFragment.newInstance(stringBuilder.toString()).show(getSupportFragmentManager(), "");
    }

    public void showOvalAnimator() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Animator ovalAnimator = ViewAnimationUtils.createCircularReveal(ivOval, ivOval.getWidth() / 2,
                    ivOval.getHeight() / 2, ivOval.getWidth(), 0);
            ovalAnimator.setDuration(2000);
            ovalAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            ovalAnimator.start();
        } else {
            ToastHelp.showShortMsg(this, "不支持CircularReveal动画");
        }
    }

    public void showRectAnimator() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Animator rectAnimator = ViewAnimationUtils.createCircularReveal(ivRect, 0, 0, 0,
                    (float) Math.hypot(ivRect.getWidth(), ivRect.getHeight()));
            rectAnimator.setDuration(2000);
            rectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            rectAnimator.start();
        } else {
            ToastHelp.showShortMsg(this, "不支持CircularReveal动画");
        }
    }


}
