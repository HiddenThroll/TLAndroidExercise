package com.tanlong.exercise.ui.activity.decode;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;

import com.orhanobut.logger.Logger;
import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityAesDecodeBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.util.EnDecryptionUtil;
import com.tanlong.exercise.util.ToastHelp;

/**
 * @author 龙
 */
public class AESDecodeActivity extends BaseActivity {

    ActivityAesDecodeBinding binding;
    String decodeMode;
    String padding;
    String algorithm;
    String password;
    String iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_aes_decode);
        binding.setActivity(this);
        initView();
    }

    private void initView() {
        String[] decodeModeArray = new String[]{
                "ECB", "CBC"
        };
        ArrayAdapter<String> decodeModeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, decodeModeArray);
        binding.spDecodeMode.setAdapter(decodeModeAdapter);

        String[] paddingArray = new String[]{
                "NoPadding", "PKCS5Padding", "PKCS7Padding"
        };
        ArrayAdapter<String> paddingAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, paddingArray);
        binding.spPaddingMode.setAdapter(paddingAdapter);
    }

    private boolean prepareData() {
        decodeMode = (String) binding.spDecodeMode.getSelectedItem();
        padding = (String) binding.spPaddingMode.getSelectedItem();
        algorithm = "AES/" + decodeMode + "/" + padding;
        Logger.e("algorithm is " + algorithm);

        password = binding.etPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            ToastHelp.showShortMsg(this, "密码不能为空");
            return false;
        }

        iv = binding.etIv.getText().toString();
        if (TextUtils.isEmpty(iv) || iv.length() < 16) {
            ToastHelp.showShortMsg(this, "偏移量至少为16字节长度");
            return false;
        }
        return true;
    }

    public void encodeContent() {
        if (!prepareData()) {
            return;
        }

        String content = binding.etEncode.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastHelp.showShortMsg(this, "加密内容不能为空");
            return;
        }

        try {
            String result = EnDecryptionUtil.aesEncrypt(content, "utf-8", algorithm, password, iv);
            binding.etDecode.setText(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void decodeContent() {
        if (!prepareData()) {
            return;
        }

        String content = binding.etDecode.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastHelp.showShortMsg(this, "解密内容不能为空");
            return;
        }

        try {
            String result = EnDecryptionUtil.aesDecrypt(content, "utf-8", algorithm, password, iv);
            binding.etEncode.setText(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
