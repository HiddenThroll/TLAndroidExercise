package com.tanlong.exercise.ui.activity.hotfix;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;

import com.orhanobut.logger.Logger;
import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityHotFixSampleBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.util.FixDexUtil;

/**
 * @author 龙
 */
public class HotFixSampleActivity extends BaseActivity {
    ActivityHotFixSampleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_hot_fix_sample);
        binding.setActivity(this);
    }

    public void calculate() {
        SimpleHotFixBugTest test = new SimpleHotFixBugTest();
        test.test(this);
    }

    public void hotFix() {
        Logger.e("加载Dex补丁");
        FixDexUtil.loadFixedDex(this, Environment.getExternalStorageDirectory());
    }
}
