package com.tanlong.exercise.ui.activity.view.optimization;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;

/**
 * @author 龙
 */
public class OverdrawBackgroundActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_overdraw_background);

        setContentView(R.layout.activity_overdraw_background_opt);
        getWindow().getDecorView().setBackgroundColor(
                ContextCompat.getColor(this, R.color.color_eeeeee));
    }
}
