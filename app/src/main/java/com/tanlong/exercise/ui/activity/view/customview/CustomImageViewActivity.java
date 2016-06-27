package com.tanlong.exercise.ui.activity.view.customview;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 *
 * Created by Administrator on 2016/5/16.
 */
public class CustomImageViewActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_custom_image_view);
        ButterKnife.bind(this);

        mTvTitle.setText(R.string.custom_Image_view);
    }

    @OnClick(R.id.iv_back)
    public void onBack() {
        finish();
    }
}
