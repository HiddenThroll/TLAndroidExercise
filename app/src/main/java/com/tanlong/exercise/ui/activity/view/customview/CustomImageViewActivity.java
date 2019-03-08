package com.tanlong.exercise.ui.activity.view.customview;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_custom_image_view);
        ButterKnife.bind(this);

        mTvTitle.setText(R.string.custom_image_view);
    }

    @OnClick(R.id.iv_back)
    public void onBack() {
        finish();
    }
}
