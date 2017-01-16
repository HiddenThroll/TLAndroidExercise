package com.tanlong.exercise.ui.activity.view.customviewgroup;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.fragment.ShowTipsFragment;
import com.tanlong.exercise.ui.view.customviewgroup.LeftDrawerLayout2;
import com.tanlong.exercise.util.LogTool;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.fragment;

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
    LeftDrawerLayout2 mLeftDrawerLayout;
    @Bind(R.id.rl_container)
    RelativeLayout rlContainer;
    @Bind(R.id.btn_help)
    Button btnHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_left_drawer_layout_2);
        ButterKnife.bind(this);

        mTvTitle.setText(R.string.left_drawer_layout);
        btnHelp.setVisibility(View.VISIBLE);

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

    @OnClick(R.id.btn_help)
    public void onClick() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("1. 自定义ViewGroup，实现子View的测量与布局\n")
                .append("2. 根据上一小节内容实现菜单View跟随手指滑动\n")
                .append("3. 使用ViewDragHelper.smoothSlideViewTo(int left, int top)->View.invalidate()->View.computeScroll()->ViewDragHelper.continueSettling(boolean deferCallbacks)->View.invalidate()实现平滑移动效果，即打开/关闭菜单\n")
                .append("4. 覆写ViewDragHelper.onViewReleased()方法，根据MenuView距父布局的左边距判断打开/关闭菜单");
        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }
}
