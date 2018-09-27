package com.tanlong.exercise.ui.activity.view.drawable;

import android.animation.ValueAnimator;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;

import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityDrawableTestBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;

/**
 * @author 龙
 */
public class DrawableTestActivity extends BaseActivity {
    ActivityDrawableTestBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_drawable_test);
        binding.setActivity(this);

        ClipDrawable drawable = (ClipDrawable) binding.ivClip.getBackground();
        drawable.setLevel(10000);
    }

    public void changeDrawableLevel() {
        Drawable drawable = binding.ivLevel.getBackground();
        if (drawable.getLevel() > 1) {
            drawable.setLevel(1);
        } else {
            drawable.setLevel(2);
        }
    }

    private boolean transition = false;

    public void changeDrawableTransition() {
        TransitionDrawable drawable = (TransitionDrawable) binding.ivTransition.getBackground();
        if (transition) {
            transition = false;
            drawable.reverseTransition(1000);
        } else {
            transition = true;
            drawable.startTransition(1000);
        }
    }

    public void clipDrawable() {
        final ClipDrawable drawable = (ClipDrawable) binding.ivClip.getBackground();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(10000, 0, 10000).setDuration(10 * 1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int curLevel = (int) animation.getAnimatedValue();
                drawable.setLevel(curLevel);
            }
        });
        valueAnimator.start();
    }
}
