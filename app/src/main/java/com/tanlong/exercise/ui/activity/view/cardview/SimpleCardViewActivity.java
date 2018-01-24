package com.tanlong.exercise.ui.activity.view.cardview;

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
 * 简单的CardView练习
 * Created by 龙 on 2017/6/20.
 */

public class SimpleCardViewActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.btn_help)
    Button btnHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_simple_card_view);
        ButterKnife.bind(this);

        tvTitle.setText(R.string.simple_cardview_exercise);
        btnHelp.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.iv_back, R.id.btn_help})
    public void onViewClicked(View view) {
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
        stringBuilder.append("CardView基本使用\n")
                .append("1. CardView是一个带圆角和阴影背景的FrameLayout\n")
                .append("2. 阴影Padding\n")
                .append("2.1 在5.0之前系统，CardView会自动添加一些额外的padding空间来绘制阴影部分，这也导致了以5.0为分界线的不同系统上CardView的尺寸大小不同\n")
                .append("2.2 可以使用不同API版本的dimension资源适配，即借助values和values-21文件夹中不同的dimens.xml文件来解决这个问题\n")
                .append("2.3 还可以设置app:cardUseCompatPadding=true来保证CardView在不同系统使用相同的Padding值\n")
                .append("3. 圆角覆盖\n")
                .append("3.1 在5.0之前系统，CardView不会裁剪内容元素以满足圆角需求，而是使用添加padding的替代方案，从而使内容元素不会覆盖CardView的圆角。控制这个行为的属性就是cardPreventCornerOverlap，默认值为true\n")
                .append("4. Ripple(涟漪)效果\n")
                .append("4.1 使用android:foreground=?android:attr/selectableItemBackground实现5.0以上系统点击时的涟漪效果");
        ShowTipsFragment.newInstance(stringBuilder.toString()).show(getSupportFragmentManager(), "");
    }
}
