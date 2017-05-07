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
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;

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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("属性动画基本使用:\n")
                .append("1. ObjectAnimator.ofFloat(Object target, String propertyName, Float... values)\n")
                .append("1.1 target是操作的对象, 这里是View\n")
                .append("1.2 propertyName是操作的属性, 该属性必须有get和set方法, 系统通过反射操作属性\n")
                .append("1.3  Float... values是变长参数, 是属性变化的取值过程\n")
                .append("2. AnimatorSet, 对同一个对象的多个属性作用多种动画, 实现精确的顺序控制\n")
                .append("2.1 playTogether() 同时作用\n")
                .append("2.2 playSequentially() 顺序执行\n")
                .append("2.3 play(animator1).with(animator2) 同时作用,与playTogether一样\n")
                .append("2.4 play(animator1).before(animator2) 先animator1, 再animator2\n")
                .append("2.5 play(animator1).after(animator2) 先animator2, 再animator1");

        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
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
        animatorSet.play(translationAnim).with(scaleXAnim).with(scaleYAnim)//同时位移和缩放
                .after(alphaAnim).before(rotateAnim);   //先透明度, 后旋转
        animatorSet.start();

    }
}
