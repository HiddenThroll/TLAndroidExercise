package com.tanlong.exercise.ui.activity.view.customview;

import android.animation.ObjectAnimator;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.LinearInterpolator;

import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityCustomAudioPulsationBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.view.customview.CustomAudioPulsation;

/**
 * @author 龙
 */
public class CustomAudioPulsationActivity extends BaseActivity {
    ActivityCustomAudioPulsationBinding binding;
    CustomAudioPulsation customAudioPulsation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_custom_audio_pulsation);
        customAudioPulsation = findViewById(R.id.custom_audio_pulsation);
        startAnimator();
    }

    private void startAnimator() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                customAudioPulsation.invalidate();
                startAnimator();
            }
        }, 200);
    }
}
