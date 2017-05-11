package com.tanlong.exercise.ui.activity.view.animator.propertyanimator;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.model.entity.PointItem;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.animator.typeevaluator.PointEvaluator;
import com.tanlong.exercise.ui.view.customview.CustomPointView;
import com.tanlong.exercise.util.DisplayUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 龙 on 2017/5/11.
 */

public class BallAnimatorActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;
    @Bind(R.id.cv_point)
    CustomPointView cvPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ball_animator);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        tvTitle.setText(R.string.ball_fall_animator);
        btnHelp.setVisibility(View.VISIBLE);

        startAnim();
    }

    private void startAnim() {
        int width = DisplayUtil.getDisplay(this).x;
        int heiht = DisplayUtil.getDisplay(this).y;
        PointItem startPoint = new PointItem(width / 2, cvPoint.getCIRCLE_RADIUS());
        PointItem endPoint = new PointItem(width / 2, heiht - cvPoint.getCIRCLE_RADIUS()
                - DisplayUtil.getToolsBarHeight(this) - DisplayUtil.getStatusBarHeight(this));
        ObjectAnimator animator = ObjectAnimator.ofObject(cvPoint, "pointItem", new PointEvaluator(),
                startPoint, endPoint);
        animator.setDuration(5 * 1000);
        animator.setInterpolator(new BounceInterpolator());
        animator.start();
    }

    @OnClick({R.id.iv_back, R.id.btn_help})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                break;
        }
    }
}
