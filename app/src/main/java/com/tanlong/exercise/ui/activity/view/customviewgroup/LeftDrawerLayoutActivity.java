package com.tanlong.exercise.ui.activity.view.customviewgroup;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.view.customviewgroup.LeftDrawerLayout;
import com.tanlong.exercise.util.LogTool;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * LeftDrawerLayout测试Activity
 * Created by 龙 on 2016/8/4.
 */
public class LeftDrawerLayoutActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.left_drawer_layout)
    LeftDrawerLayout mLeftDrawerLayout;
    @Bind(R.id.rl_container)
    RelativeLayout rlContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_left_drawer_layout);
        ButterKnife.bind(this);

        mTvTitle.setText(R.string.left_drawer_layout);
    }

    @OnClick({R.id.iv_back, R.id.rl_container})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_container:
                LogTool.e(TAG, "关闭抽屉");
                mLeftDrawerLayout.closeDrawer();
                break;
        }
    }

}
