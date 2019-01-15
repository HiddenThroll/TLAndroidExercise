package com.tanlong.exercise.ui.activity.decode;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityBase64DecodeBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.util.EnDecryptionUtil;
import com.tanlong.exercise.util.ToastHelp;

public class Base64DecodeActivity extends BaseActivity {
    ActivityBase64DecodeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_base64_decode);
        binding.setActivity(this);
    }

    public void encodeContent() {
        String content = binding.etEncode.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastHelp.showShortMsg(this, "待编码内容不能为空");
            return;
        }
        String encodedContent = EnDecryptionUtil.base64Encode(content, "UTF-8");
        Logger.e("encoded content is " + encodedContent);
        binding.etDecode.setText(encodedContent);
    }

    public void decodeContent() {
        String content = binding.etDecode.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastHelp.showShortMsg(this, "待解码内容不能为空");
            return;
        }
        String decodeContent = EnDecryptionUtil.base64Decode(content, "UTF-8");
        Logger.e("decoded content is " + decodeContent);
        binding.etEncode.setText(decodeContent);
    }

}
