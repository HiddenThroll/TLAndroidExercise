package com.tanlong.exercise.ui.activity.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 单一Fragment的宿主Activity
 * Created by 龙 on 2016/11/3.
 */

public abstract class SingleFragmentActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_fragment);
        ButterKnife.bind(this);

        btnHelp.setVisibility(View.VISIBLE);

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.rl_fragment_container);
        if (fragment == null) {
            fragment = createFragment();
            manager.beginTransaction().add(R.id.rl_fragment_container, fragment).commit();
        }
    }

    /**
     * 创建Fragment
     *
     * @return
     */
    protected abstract Fragment createFragment();

    @OnClick({R.id.iv_back, R.id.btn_help})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                showTips();
                break;
        }
    }

    protected void setTitle(String title) {
        tvTitle.setText(title);
    }

    protected void showTips() {
        String content = getTips();
        if (TextUtils.isEmpty(content)) {
            return;
        }
        ShowTipsFragment fragment = ShowTipsFragment.newInstance(content);
        fragment.show(getSupportFragmentManager(), "");
    }

    /**
     * 获取知识要点
     * @return
     */
    protected abstract String getTips();
}
