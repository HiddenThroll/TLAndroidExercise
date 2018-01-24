package com.tanlong.exercise.ui.activity.view.animator.mdanimator;

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
 * Created by Administrator on 2017/7/31.
 */

public class StateListAnimatorActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_help)
    Button btnHelp;
    @BindView(R.id.iv_oval)
    Button ivOval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_statelist_animator);
        ButterKnife.bind(this);

        tvTitle.setText("StateListAnimator");
        btnHelp.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.iv_back, R.id.btn_help, R.id.iv_oval})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                showTips();
                break;
            case R.id.iv_oval:
                showStateListAnimator();
                break;
        }
    }

    public void showTips() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("在Android5.X中,可以使用动画来作为视图改变的效果\n")
                .append("1. 在res/animator文件夹下新建文件 anim_change.xml\n")
                .append("2. anim_change中通过 selector -> item -> set -> objectAnimator 定义属性动画\n")
                .append("3. 通过 android:stateListAnimator='@drawable/anim_change'使用动画");
        ShowTipsFragment.newInstance(stringBuilder.toString()).show(getSupportFragmentManager(), "");
    }

    public void showStateListAnimator() {

    }
}
