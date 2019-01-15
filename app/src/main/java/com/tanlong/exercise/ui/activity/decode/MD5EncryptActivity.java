package com.tanlong.exercise.ui.activity.decode;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;

import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityMd5DecodeBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.util.EnDecryptionUtil;
import com.tanlong.exercise.util.ToastHelp;

/**
 * MD5加密
 * @author Administrator
 */
public class MD5EncryptActivity extends BaseActivity {
    ActivityMd5DecodeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_md5_decode);
        binding.setActivity(this);
    }

    public void doMD5Decode16() {
        String content = binding.etDecodeContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastHelp.showShortMsg(this, "请输入加密内容");
            return;
        }

        String result = "加密结果: " + EnDecryptionUtil.md5Decode16(content);
        binding.tvResult.setText(result);
    }

    public void doMD5Decode32() {
        String content = binding.etDecodeContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastHelp.showShortMsg(this, "请输入加密内容");
            return;
        }

        String result = "加密结果: " + EnDecryptionUtil.md5Decode(content);
        binding.tvResult.setText(result);
    }

}
