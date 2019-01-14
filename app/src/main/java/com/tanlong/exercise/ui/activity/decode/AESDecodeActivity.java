package com.tanlong.exercise.ui.activity.decode;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.google.gson.Gson;
import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.util.LogTool;

import java.security.Security;

/**
 * @author 龙
 */
public class AESDecodeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataBindingUtil.setContentView(this, R.layout.activity_aes_decode);

        LogTool.e("test", new Gson().toJson(Security.getProviders()));
    }
}
