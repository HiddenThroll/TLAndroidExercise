package com.tanlong.exercise.ui.activity.view.animator.activityanimator;

import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/26.
 */

public class TransitionActivity extends BaseActivity {

    public final static String SELECT_MODE = "select_mode";

    public final static int MODE_EXPLODE = 1;
    public final static int MODE_SLIDE = 2;
    public final static int MODE_FADE = 3;
    public final static int MODE_SHARE = 4;

    private final int DURATION_TIME = 500;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_help)
    Button btnHelp;
    @BindView(R.id.iv_share)
    ImageView ivShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 允许使用Transition
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            int flag = getIntent().getExtras().getInt(SELECT_MODE);
            switch (flag) {
                case MODE_EXPLODE:
                    Explode explode = new Explode();
                    explode.setDuration(DURATION_TIME);
                    getWindow().setEnterTransition(explode);//设置进入动画
                    getWindow().setExitTransition(explode);//设置退出动画
                    break;
                case MODE_SLIDE:
                    Slide slide = new Slide();
                    slide.setDuration(DURATION_TIME);
                    getWindow().setEnterTransition(slide);
                    getWindow().setExitTransition(slide);
                    break;
                case MODE_FADE:
                    Fade fade = new Fade();
                    fade.setDuration(DURATION_TIME);
                    getWindow().setEnterTransition(fade);
                    getWindow().setExitTransition(fade);
                    break;
                case MODE_SHARE:

                    break;
                default:
                    break;
            }
        }
        // 所有操作在设置内容视图之前
        setContentView(R.layout.activity_transition);
        ButterKnife.bind(this);
        tvTitle.setText(R.string.activity_transition);
    }

    @OnClick({R.id.iv_back, R.id.btn_help})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                break;
            default:
                break;
        }
    }
}
