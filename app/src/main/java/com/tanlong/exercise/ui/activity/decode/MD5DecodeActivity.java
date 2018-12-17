package com.tanlong.exercise.ui.activity.decode;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;

import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityMd5DecodeBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.util.ToastHelp;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密
 * @author Administrator
 */
public class MD5DecodeActivity extends BaseActivity {
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

        String result = "加密结果: " + md5Decode16(content);
        binding.tvResult.setText(result);
    }

    public void doMD5Decode32() {
        String content = binding.etDecodeContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastHelp.showShortMsg(this, "请输入加密内容");
            return;
        }

        String result = "加密结果: " + md5Decode(content);
        binding.tvResult.setText(result);
    }

    /**
     * 16位MD5加密
     * 实际是截取的32位加密结果的中间部分(8-24位)
     * @param content
     * @return
     */
    public String md5Decode16(String content) {
        return md5Decode(content).substring(8, 24);
    }

    /**
     * 32位MD5加密
     * @param content -- 待加密内容
     * @return
     */
    public String md5Decode(String content) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException",e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }
        //对生成的16字节数组进行补零操作
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10){
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }


}
