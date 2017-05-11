package com.tanlong.exercise.ui.activity.view.animator.propertyanimator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;
import com.tanlong.exercise.ui.view.customview.CustomPointView;
import com.tanlong.exercise.util.DisplayUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.baidu.location.g.h.A;
import static com.baidu.location.g.h.n;

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
        // 终点纵坐标 = 屏幕高度 - 状态栏高度 - toolsBar高度 - 小球半径
        PointItem endPoint = new PointItem(width / 2, heiht - cvPoint.getCIRCLE_RADIUS()
                - DisplayUtil.getToolsBarHeight(this) - DisplayUtil.getStatusBarHeight(this));
        ObjectAnimator animator = ObjectAnimator.ofObject(cvPoint, "pointItem", new PointEvaluator(),
                startPoint, endPoint);
        animator.setDuration(5 * 1000);
        animator.setInterpolator(new BounceInterpolator());//设置插值器
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.start();
    }

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

    private void showTips() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("小球落地动画\n")
                .append("1. TypeEvaluator\n")
                .append("1.1 一个接口，通过计算告诉动画系统如何从初始值过度到结束值\n")
                .append("1.2 public T evaluate(float fraction, T startValue, T endValue)计算方法\n")
                .append("1.2.1 fraction参数表示动画的完成度，根据它来计算当前动画的值应该是多少\n")
                .append("1.2.2 startValue动画初始值\n")
                .append("1.2.3 endValue动画结束值\n")
                .append("1.3 这里我们创建了一个PointEvaluator继承TypeEvaluator，定义初始坐标到终点坐标的变化过程\n")
                .append("2. TimeInterpolator\n")
                .append("2.1 一个接口，可以控制动画的变化速率\n")
                .append("2.2 float getInterpolation(float input)\n")
                .append("2.2.1 input参数根据设定的动画时长匀速增加，范围从0到1，对应动画的开始到结束\n")
                .append("2.2.2 方法返回值就是T evaluate(float fraction, T startValue, T endValue)方法中的fraction!!!");

        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }
}
