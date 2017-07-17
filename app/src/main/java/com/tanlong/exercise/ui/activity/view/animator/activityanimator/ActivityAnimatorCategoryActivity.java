package com.tanlong.exercise.ui.activity.view.animator.activityanimator;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
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
 * Created by Administrator on 2017/6/26.
 */

public class ActivityAnimatorCategoryActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;
    @Bind(R.id.iv_share)
    ImageView ivShare;
    @Bind(R.id.btn_transition_explode)
    Button btnTransitionExplode;
    @Bind(R.id.btn_transition_slide)
    Button btnTransitionSlide;
    @Bind(R.id.btn_transition_fade)
    Button btnTransitionFade;
    @Bind(R.id.btn_transition_change_bounds)
    Button btnTransitionChangeBounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator_category);
        ButterKnife.bind(this);
        tvTitle.setText(R.string.activity_transition);
        btnHelp.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.iv_back, R.id.btn_help, R.id.btn_transition_explode, R.id.btn_transition_slide,
            R.id.btn_transition_fade, R.id.btn_transition_change_bounds})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                showTips();
                break;
            case R.id.btn_transition_explode:
                startActivityTransitionAnimator(TransitionActivity.MODE_EXPLODE);
                break;
            case R.id.btn_transition_slide:
                startActivityTransitionAnimator(TransitionActivity.MODE_SLIDE);
                break;
            case R.id.btn_transition_fade:
                startActivityTransitionAnimator(TransitionActivity.MODE_FADE);
                break;
            case R.id.btn_transition_change_bounds:
                startShareActivityTransitionAnimator();
                break;
        }
    }

    private void showTips() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Activity转场动画\n")
                .append("1. 分类\n")
                .append("1.1 进入动画, 决定Activity中所有View怎么进入屏幕\n")
                .append("1.2 退出动画, 决定Activity中所有View怎么退出屏幕\n")
                .append("1.3 共享动画, 决定两个Activity之间怎么共享它们的View\n")
                .append("2. 效果\n")
                .append("2.1 进入和退出动画包括\n")
                .append("2.1.1 explode分解, 从屏幕中间进或出, 移动视图\n")
                .append("2.1.2 slide滑动, 从屏幕边缘进或出, 移动视图\n")
                .append("2.1.3 fade淡入淡出, 改变屏幕上视图的不透明度来添加或移除视图\n")
                .append("2.2 共享元素动画包括\n")
                .append("2.2.1 changeBounds 改变View的布局边界\n")
                .append("2.2.2 changeClipBounds 裁剪View边界\n")
                .append("2.2.3 changeTransform 改变View的缩放比例和旋转角度\n")
                .append("2.2.4 changeImageTransform 改变图片的大小和缩放比例\n")
                .append("3. 使用\n")
                .append("3.1 进入和退出\n")
                .append("3.1.1 ActivityA\n")
                .append("通过 startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())启动ActivityB\n")
                .append("3.1.2 ActivityB\n")
                .append("在setContentVIew()之前\n")
                .append("(1) getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITION)允许使用转场动画\n")
                .append("(2) getWindow().setEnterTransition(new Explode()) 设置进入动画\n")
                .append("(3) getWindow().setExitTransition(new Explode()) 设置退出动画\n")
                .append("3.2 共享元素\n")
                .append("3.2.1 布局文件\n")
                .append("在ActivityA和ActivityB的布局文件中的共享元素(View)设置 android:transitionName 为同一个字符串,指定共享元素\n")
                .append("3.2.2 ActivityA\n")
                .append("通过 startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedView, sharedName))启动ActivityB");
        ShowTipsFragment.newInstance(stringBuilder.toString()).show(getSupportFragmentManager(), "");
    }

    private void startActivityTransitionAnimator(int flag) {
        Intent intent = new Intent(this, TransitionActivity.class);
        intent.putExtra(TransitionActivity.SELECT_MODE, flag);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
        } else {
            ToastHelp.showShortMsg(this, "不支持过场动画");
            startActivity(intent);
        }
    }

    private void startShareActivityTransitionAnimator() {
        Intent intent = new Intent(this, TransitionActivity.class);
        intent.putExtra(TransitionActivity.SELECT_MODE, TransitionActivity.MODE_SHARE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            String key = getString(R.string.share_activity);
            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    ivShare, key).toBundle());
        } else {
            ToastHelp.showShortMsg(this, "不支持过场动画");
            startActivity(intent);
        }
    }
}
