package com.tanlong.exercise.ui.activity.view.vieweffect;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/26.
 */

public class RoundImageByXfermodeActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_round_image);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        tvTitle.setText(R.string.view_round_image);
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
        stringBuilder.append("1. 使用PorterDuffXfermode实现圆角图片效果:")
                .append("1.1 PorterDuffXfermode用于实现新绘制的像素与Canvas上对应位置已有像素,按照混合规则进行颜色混合\n")
                .append("1.2 DST是先画的图形, SRC是后画的图形\n")
                .append("1.3 一般需配合Canvas.saveLayer()保存图层方法和Canvas.restoreToCount()恢复图层方法使用, 关键代码位于两个方法之间\n")
                .append("1.4 为保证在不同机型上的效果，需关闭硬件加速，即View.setLayerType(View.LAYER_TYPE_SOFTWARE, null)");

        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }
}
