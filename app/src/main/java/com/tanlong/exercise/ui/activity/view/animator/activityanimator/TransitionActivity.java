package com.tanlong.exercise.ui.activity.view.animator.activityanimator;

import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Window;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;

/**
 * Created by Administrator on 2017/6/26.
 */

public class TransitionActivity extends BaseActivity {

    public final static String SELECT_MODE = "select_mode";

    public final static int MODE_EXPLODE = 1;
    public final static int MODE_SLIDE = 2;
    public final static int MODE_FADE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            int flag = getIntent().getExtras().getInt(SELECT_MODE);
            switch (flag) {
                case MODE_EXPLODE:
                    getWindow().setEnterTransition(new Explode());
                    getWindow().setExitTransition(new Explode());
                    break;
                case MODE_SLIDE:
                    getWindow().setEnterTransition(new Slide());
                    getWindow().setExitTransition(new Slide());
                    break;
                case MODE_FADE:
                    getWindow().setEnterTransition(new Fade());
                    getWindow().setExitTransition(new Fade());
                    break;
            }
        }

        setContentView(R.layout.activity_transition);
    }
}
