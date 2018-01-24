package com.tanlong.exercise.ui.activity.view.constraintlayout;

import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.util.ToastHelp;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 龙 on 2017/8/3.
 */

public class ConstraintChangeActivity extends BaseActivity {

    @BindView(R.id.iv_cl_pic_1)
    ImageView ivClPic1;
    @BindView(R.id.tv_before)
    TextView tvBefore;
    @BindView(R.id.tv_after)
    TextView tvAfter;
    @BindView(R.id.cl_main)
    ConstraintLayout clMain;

    ConstraintSet constraintSetOld = new ConstraintSet();
    ConstraintSet constraintSetNew = new ConstraintSet();

    boolean isShowNew = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_constraint_pic_1);
        ButterKnife.bind(this);

        constraintSetOld.clone(clMain);
        constraintSetNew.clone(this, R.layout.activity_constraint_pic_2);
    }

    @OnClick({R.id.iv_cl_pic_1, R.id.tv_before, R.id.tv_after})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_cl_pic_1:
                if (isShowNew) {
                    isShowNew = false;
                    showOldLayout();
                } else {
                    isShowNew = true;
                    showNewLayout();
                }
                break;
            case R.id.tv_before:
                ToastHelp.showShortMsg(this, "Before");
                break;
            case R.id.tv_after:
                ToastHelp.showShortMsg(this, "After");
                break;
        }
    }

    private void showNewLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(clMain);
        }
        tvAfter.setVisibility(View.VISIBLE);
        tvBefore.setVisibility(View.VISIBLE);
        constraintSetNew.applyTo(clMain);
    }

    private void showOldLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(clMain);
        }
        tvAfter.setVisibility(View.GONE);
        tvBefore.setVisibility(View.GONE);
        constraintSetOld.applyTo(clMain);
    }
}
