package com.tanlong.exercise.ui.activity.view.animator.lottie;

import android.animation.ValueAnimator;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import com.airbnb.lottie.LottieAnimationView;
import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityLottieBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.activity.view.recyclerview.adapter.SimpleRecyclerViewAdapter;
import com.tanlong.exercise.ui.view.customview.TestRecyclerView;
import com.tanlong.exercise.util.DisplayUtil;
import com.tanlong.exercise.util.LogTool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 龙
 */
public class AndroidLottieActivity extends BaseActivity {
    ActivityLottieBinding binding;
    LottieAnimationView lottieView;

    TestRecyclerView rvList;
    SimpleRecyclerViewAdapter adapter;
    List<String> mDatas;

    private float triggerHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_lottie);
        binding.setActivity(this);
        initData();
        initView();
    }

    private void initData() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mDatas.add("item " + i);
        }
        triggerHeight = DisplayUtil.dip2px(AndroidLottieActivity.this, 48f);
    }

    private void initView() {
        lottieView = binding.lottieView;
        //加载动画
        lottieView.setAnimation("temp1.json");

        rvList = binding.rvList;
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setiPullListener(iPullListener);
        adapter = new SimpleRecyclerViewAdapter(this, mDatas);
        rvList.setAdapter(adapter);
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

    private TestRecyclerView.IPullListener iPullListener = new TestRecyclerView.IPullListener() {
        @Override
        public void onPull(float y) {
            if (y > triggerHeight) {
                y = triggerHeight;
            }
            float progress = y / triggerHeight;
            LogTool.e("test", "y is " + y + " triggerHeight is " + triggerHeight);
            lottieView.setProgress(progress);
        }

        @Override
        public void reset() {
            lottieView.setProgress(0);
        }
    };
}
