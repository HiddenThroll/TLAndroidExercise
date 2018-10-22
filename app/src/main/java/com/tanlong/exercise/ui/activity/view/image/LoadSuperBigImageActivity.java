package com.tanlong.exercise.ui.activity.view.image;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityLoadSuperBigImageBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author 龙
 */
public class LoadSuperBigImageActivity extends BaseActivity {
    ActivityLoadSuperBigImageBinding binding;
    InputStream inputStream;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_load_super_big_image);

        try {
            inputStream = getAssets().open("qm.jpg");
            binding.largeVIew.setInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.e("onDestroy");
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
