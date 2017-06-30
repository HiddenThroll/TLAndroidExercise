package com.tanlong.exercise.ui.activity.view.animator.activityanimator;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.util.ToastHelp;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/26.
 */

public class ActivityAnimatorCategoryActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;
    @Bind(R.id.iv_share)
    ImageView ivShare;
    @Bind(R.id.btn_transition_explode)
    Button btnTransitionExplode;
    @Bind(R.id.btn_transition_slide)
    Button btnTransitionSlide;
    @Bind(R.id.btn_transition_fade)
    Button btnTransitionFade;
    @Bind(R.id.btn_transition_change_bounds)
    Button btnTransitionChangeBounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator_category);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.iv_back, R.id.btn_help, R.id.btn_transition_explode, R.id.btn_transition_slide,
            R.id.btn_transition_fade, R.id.btn_transition_change_bounds})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                showTips();
                break;
            case R.id.btn_transition_explode:
                startActivityTransitionAnimator(TransitionActivity.MODE_EXPLODE);
                break;
            case R.id.btn_transition_slide:
                startActivityTransitionAnimator(TransitionActivity.MODE_SLIDE);
                break;
            case R.id.btn_transition_fade:
                startActivityTransitionAnimator(TransitionActivity.MODE_FADE);
                break;
            case R.id.btn_transition_change_bounds:
                startShareActivityTransitionAnimator();
                break;
        }
    }

    private void showTips() {

    }

    private void startActivityTransitionAnimator(int flag) {
        Intent intent = new Intent(this, TransitionActivity.class);
        intent.putExtra(TransitionActivity.SELECT_MODE, flag);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
        } else {
            ToastHelp.showShortMsg(this, "不支持过场动画");
            startActivity(intent);
        }
    }

    private void startShareActivityTransitionAnimator() {
        Intent intent = new Intent(this, TransitionActivity.class);
        intent.putExtra(TransitionActivity.SELECT_MODE, TransitionActivity.MODE_SHARE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            String key = getString(R.string.share_activity);
            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    ivShare, key).toBundle());
        } else {
            ToastHelp.showShortMsg(this, "不支持过场动画");
            startActivity(intent);
        }
    }
}
