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
 *
 * @author Administrator
 * @date 2016/5/16
 */
public class CustomImageViewActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.btn_help)
    Button btnHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_custom_image_view);
        ButterKnife.bind(this);

        mTvTitle.setText(R.string.custom_image_view);
        btnHelp.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.iv_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.btn_help)
    public void onClick() {
        StringBuilder sb = new StringBuilder();
        sb.append("1. 宽度取文字与图片宽度中的较大值,高度取文字与图片高度之和\n")
                .append("2. 当设置的宽度小于字体需要的宽度时，将字体改为xxx...\n")
                .append("2.1 CharSequence TextUtils.ellipsize(CharSequence text, TextPaint p, float avail, TruncateAt where)可实现该功能\n")
                .append("2.2 当宽度(avail)足够展示内容时,返回完整text;宽度不够时,返回截断后的文字和...");
        ShowTipsFragment fragment = ShowTipsFragment.newInstance(sb.toString());
        fragment.show(getSupportFragmentManager(), "");
    }
}
