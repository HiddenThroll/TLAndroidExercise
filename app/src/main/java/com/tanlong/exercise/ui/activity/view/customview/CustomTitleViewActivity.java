package com.tanlong.exercise.ui.activity.view.customview;

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
 * Created by Administrator on 2016/5/15.
 */
public class CustomTitleViewActivity extends BaseActivity {
    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.tv_title)
    TextView mTvitle;
    @Bind(R.id.btn_help)
    Button btnHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_custom_title_view);
        ButterKnife.bind(this);

        initView();
    }

    public void initView() {
        mTvitle.setText(R.string.custom_title_view);
        btnHelp.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.iv_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.btn_help)
    public void onClick() {
        ShowTipsFragment fragment = ShowTipsFragment.newInstance("测试");
        fragment.show(getSupportFragmentManager(), "");
    }
}
