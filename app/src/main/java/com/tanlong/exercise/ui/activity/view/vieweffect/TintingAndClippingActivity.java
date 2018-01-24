package com.tanlong.exercise.ui.activity.view.vieweffect;

import android.annotation.TargetApi;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.util.DisplayUtil;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/19.
 */

public class TintingAndClippingActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_help)
    Button btnHelp;
    @BindView(R.id.tv_clip_1)
    TextView tvClip1;
    @BindView(R.id.tv_clip_2)
    TextView tvClip2;

    ViewOutlineProvider providerRoundRect;
    ViewOutlineProvider providerCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tinting_clipping);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        tvTitle.setText("着色和裁剪");
        btnHelp.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            providerRoundRect = new ViewOutlineProvider() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(),
                            DisplayUtil.dip2px(TintingAndClippingActivity.this, 16));
                }
            };


            providerCircle = new ViewOutlineProvider() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setOval(0, 0, view.getWidth(), view.getHeight());
                }
            };

        }
    }


    private void showTips() {

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @OnClick({R.id.iv_back, R.id.btn_help, R.id.tv_clip_1, R.id.tv_clip_2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                showTips();
                break;
            case R.id.tv_clip_1:
                tvClip1.setOutlineProvider(providerRoundRect);
                break;
            case R.id.tv_clip_2:
                tvClip2.setOutlineProvider(providerCircle);
                break;
        }
    }
}
