package com.tanlong.exercise.ui.activity.updateversion;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;

/**
 * @author 龙
 */
public class UpdateVersionActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataBindingUtil.setContentView(this, R.layout.activity_update_version);
    }
}
