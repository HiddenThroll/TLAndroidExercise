package com.tanlong.exercise.ui.activity.view.animator.lottie;

import android.animation.ValueAnimator;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import com.airbnb.lottie.LottieAnimationView;
import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityLottieBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;

/**
 * @author 龙
 */
public class AndroidLottieActivity extends BaseActivity {
    ActivityLottieBinding binding;
    LottieAnimationView lottieView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_lottie);
        binding.setActivity(this);
        initView();
    }

    private void initView() {
        lottieView = binding.lottieView;
        //加载动画
        lottieView.setAnimation("temp1.json");
    }

    public void loopAnimation() {
        lottieView.cancelAnimation();
        //循环播放动画
        lottieView.setRepeatCount(Animation.INFINITE);
        lottieView.playAnimation();
    }

    public void customAnimation() {
        lottieView.cancelAnimation();

        ValueAnimator animator = ValueAnimator.ofFloat(0, 1)
                .setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                lottieView.setProgress(progress);
            }
        });
        animator.start();
    }
}
