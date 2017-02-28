package com.tanlong.exercise.ui.activity.view.vieweffect;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.fragment.ShowTipsFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/28.
 */

public class ShadowBackgroundActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shadow_background);
        ButterKnife.bind(this);

        tvTitle.setText(R.string.view_shadow_background);
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

        stringBuilder.append("1. 在XML中使用图层实现阴影背景效果: \n")
                .append("1.1 layer-list的第一个Item作为背景图提供露出来的四个边\n")
                .append("1.2 layer-list的第二个Item在第一个Item上面,设置一定的top/left/right/bottom值,露出第一个Item的四个边,类似于padding效果\n")
                .append("2 为凸显阴影效果,第一个Item使用gradient作为填充,gradient属性为: \n")
                .append("2.1 公共属性:\n")
                .append("2.1.1 startColor 颜色渐变的开始颜色\n")
                .append("2.1.2 endColor 颜色渐变的结束颜色\n")
                .append("2.1.3 centerColor 颜色渐变的中间颜色, 主要用于多彩\n")
                .append("2.2 线性渐变: \n")
                .append("2.2.1 设置type属性为linear, 默认值\n")
                .append("2.2.2 angle 颜色渐变的角度, 只适用于线性渐变, 0代表从左到右, 90代表从下到上, 必须是45的整数倍\n")
                .append("2.3 圆形渐变: \n")
                .append("2.3.1 设置type属性为radial, 起始颜色从centerX, centerY开始\n")
                .append("2.3.2 centerX 相对于X的渐变位置, 取值范围0 - 1.0\n")
                .append("2.3.3 centerY 相对于Y的渐变位置, 取值范围0 - 1.0\n")
                .append("2.3.4 gradientRadius 渐变颜色半径, 单位为px\n")
                .append("2.4 扫描线性渐变: \n")
                .append("2.4.1 设置type属性为sweeping, 起始颜色从centerX, centerY开始\n")
                .append("2.4.2 centerX 相对于X的渐变位置, 取值范围0 - 1.0\n")
                .append("2.4.3 centerY 相对于Y的渐变位置, 取值范围0 - 1.0\n");

        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");

    }
}
