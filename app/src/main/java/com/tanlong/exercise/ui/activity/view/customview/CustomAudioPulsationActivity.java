package com.tanlong.exercise.ui.activity.view.customview;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityCustomAudioPulsationBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.view.customview.AudioPulsationView;

/**
 * @author 龙
 */
public class CustomAudioPulsationActivity extends BaseActivity {
    ActivityCustomAudioPulsationBinding binding;
    AudioPulsationView audioPulsationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_custom_audio_pulsation);
        audioPulsationView = findViewById(R.id.custom_audio_pulsation);

//        audioPulsationView.setPillar(8, 16, new int[] {ContextCompat.getColor(this, R.color.colorPrimary), Color.RED});
        audioPulsationView.setPillar(8, 16, ContextCompat.getColor(this, R.color.colorPrimary));
        audioPulsationView.setRhythm(1);
    }

}
