package com.tanlong.exercise.ui.activity.view.surfaceview;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 龙 on 2017/4/28.
 */

public class CustomSurfaceClockActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_help)
    Button btnHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_surface_clock);
        ButterKnife.bind(this);

        tvTitle.setText(R.string.custom_surface_clock);
        btnHelp.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.iv_back, R.id.btn_help})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                showTips();
                break;
        }
    }

    private void showTips() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("使用SurfaceView实现时钟:\n")
                .append("1. 创建自定义View继承SurfaceView并实现SurfaceHolder.Callback接口和Runnable接口\n")
                .append("1.1 在surfaceCreated回调方法中启动绘制线程,在surfaceDestroyed回调方法中终止绘制线程\n")
                .append("1.2 绘制线程中通过SurfaceHolder.lockCanvas()获得画布Canvas进行绘制,通过SurfaceHolder.unlockCanvasAndPost()方法提交绘制内容\n")
                .append("1.3 绘制逻辑参考CustomSimpleClock类");

        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }
}
