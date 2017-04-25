package com.tanlong.exercise.ui.activity.view.customviewgroup;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;
import com.tanlong.exercise.ui.view.customviewgroup.VerticalLinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/22.
 */
public class VerticalLinearLayoutActivity extends BaseActivity {

    @Bind(R.id.vll_activity_vertical_linear_layout)
    VerticalLinearLayout mVerticalLinearLayout;
    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vertical_linear_layout);
        ButterKnife.bind(this);

        mTvTitle.setText(R.string.vertical_linear_layout);
        btnHelp.setVisibility(View.VISIBLE);

        mVerticalLinearLayout.setOnPageChangeListener(new VerticalLinearLayout.OnPageChangeListener() {
            @Override
            public void onPageChange(int currentPage) {
                showShortMessage("currentPage is " + (currentPage + 1));
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.btn_help)
    public void onClick() {
        StringBuilder sb = new StringBuilder();
        sb.append("1. 在onMeasure方法中使用measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec)方法测量单个子View，使用measureChildren(int widthMeasureSpec, int heightMeasureSpec)方法测量所有非GONE子View\n")
                .append("2. 主布局的高度 = 屏幕高度 * 子View个数\n")
                .append("3. 在onLayout方法中，调用每个子View的layout，让每个子View依次纵向排列\n")
                .append("4. 使用Scroller类实现滑动\n")
                .append("5. 使用VelocityTracker类监测触摸事件（Y轴方向）加速度\n");
        ShowTipsFragment fragment = ShowTipsFragment.newInstance(sb.toString());
        fragment.show(getSupportFragmentManager(), "");
    }

}
