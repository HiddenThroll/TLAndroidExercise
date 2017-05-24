package com.tanlong.exercise.ui.activity.view.animator.propertyanimator;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;
import com.tanlong.exercise.util.DisplayUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 平滑显示/隐藏布局
 * Created by 龙 on 2017/5/24.
 */

public class SmoothHideActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;
    @Bind(R.id.ll_show_title)
    LinearLayout llShowTitle;
    @Bind(R.id.ll_hide_content)
    LinearLayout llHideContent;

    private int hideContentHeight;
    private boolean isHidden;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_smooth_hide);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        tvTitle.setText("平滑显示/隐藏布局");
        btnHelp.setVisibility(View.VISIBLE);
        hideContentHeight = DisplayUtil.dip2px(this, 32);
        isHidden = true;
    }

    @OnClick({R.id.iv_back, R.id.btn_help, R.id.ll_show_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                showTips();
                break;
            case R.id.ll_show_title:
                if (isHidden) {
                    isHidden = false;
                    showContent();
                } else {
                    isHidden = true;
                    hideContent();
                }
                break;
        }
    }

    private void showContent() {
        startAnimator(llHideContent, 0, hideContentHeight);
    }

    private void hideContent() {
        startAnimator(llHideContent, hideContentHeight, 0);
    }

    private void startAnimator(final LinearLayout target, int startValue, int endValue) {
        ValueAnimator animator = ValueAnimator.ofInt(startValue, endValue);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams params = target.getLayoutParams();
                params.height = height;
                target.setLayoutParams(params);
            }
        });
        animator.setDuration(1000).start();
    }

    private void showTips() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("实现平滑显示/隐藏布局\n")
                .append("1. 通过ValueAnimator.ofInt(startValue, endValue)实现startValue到endValue的变化\n")
                .append("2. ValueAnimator.addUpdateListener()的onAnimationUpdate()方法中设置布局的LayoutParams");

        ShowTipsFragment.newInstance(stringBuilder.toString()).show(getSupportFragmentManager(), "");
    }
}
