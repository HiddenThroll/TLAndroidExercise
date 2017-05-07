package com.tanlong.exercise.ui.activity.view.animator.propertyanimator;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 基本属性动画, 展示 移动,旋转,缩放和透明度变化
 * Created by Administrator on 2017/5/7.
 */

public class SimpleAnimatorActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;
    @Bind(R.id.tv_simple_animator)
    TextView tvSimpleAnimator;

    AnimatorSet animatorSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_simple_animator);
        ButterKnife.bind(this);

        tvTitle.setText(R.string.simple_property_animator);
        btnHelp.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.iv_back, R.id.btn_help, R.id.tv_simple_animator})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                showTips();
                break;
            case R.id.tv_simple_animator:
                startAnimator();
                break;
        }
    }

    private void showTips() {

    }

    private void startAnimator() {
        Logger.e("startAnimator");
        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(tvSimpleAnimator, "alpha", 1f, 0f, 1f);
        ObjectAnimator translationAnim = ObjectAnimator.ofFloat(tvSimpleAnimator, "translationX", 0, 500, 0);
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(tvSimpleAnimator, "scaleX", 1, 0, 1);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(tvSimpleAnimator, "scaleY", 1, 0, 1);
        ObjectAnimator rotateAnim = ObjectAnimator.ofFloat(tvSimpleAnimator, "rotation", 0, 360);
        animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000 * 10);
        animatorSet.play(translationAnim).with(scaleXAnim).with(scaleYAnim)
                .after(alphaAnim).before(rotateAnim);
        animatorSet.start();

    }
}
