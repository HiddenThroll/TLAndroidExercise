package com.tanlong.exercise.ui.activity.view.customview;

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
 *
 * @author 龙
 * @date 2016/6/27
 */
public class CustomCircleViewActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.btn_help)
    Button btnHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_custom_circle_view);
        ButterKnife.bind(this);

        mTvTitle.setText(R.string.custom_circle_view);
        btnHelp.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.iv_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.btn_help)
    public void onClick() {
        StringBuilder sb = new StringBuilder();
        sb.append("1. 非UI线程, 使用postInvalidate()方法更新View\n")
                .append("2. Canvas.drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)方法画弧\n")
                .append("RectF -- 绘制图形的外接矩形\n")
                .append("startAngle -- 绘制起始角度，X正半轴为0度\n")
                .append("sweepAngle -- 绘制扫过的角度\n")
                .append("useCenter -- 是否绘制(连接)弧形的椭圆中心\n")
                .append("paint -- 使用的画笔");
        ShowTipsFragment fragment = ShowTipsFragment.newInstance(sb.toString());
        fragment.show(getSupportFragmentManager(), "");
    }
}
