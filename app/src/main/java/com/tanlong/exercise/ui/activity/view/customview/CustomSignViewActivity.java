package com.tanlong.exercise.ui.activity.view.customview;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.view.customview.CustomSignView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 龙 on 2017/4/24.
 */

public class CustomSignViewActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;
    @Bind(R.id.cv_sign)
    CustomSignView mCvSign;
    @Bind(R.id.btn_reset)
    Button btnReset;
    @Bind(R.id.btn_save)
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_custom_sign_view);
        ButterKnife.bind(this);

        tvTitle.setText(R.string.custom_sign_view);
    }

    @OnClick({R.id.iv_back, R.id.btn_help, R.id.btn_reset, R.id.btn_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                break;
            case R.id.btn_reset:
                mCvSign.resetSign();
                break;
            case R.id.btn_save:
                break;
        }
    }

}
