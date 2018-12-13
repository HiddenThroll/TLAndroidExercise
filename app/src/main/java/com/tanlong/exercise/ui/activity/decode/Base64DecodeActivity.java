package com.tanlong.exercise.ui.activity.decode;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;

import com.orhanobut.logger.Logger;
import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityBase64DecodeBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.util.ToastHelp;

import java.io.UnsupportedEncodingException;

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
        String encodedContent = base64Encode(content, "UTF-8");
        Logger.e("encoded content is " + encodedContent);
        binding.etDecode.setText(encodedContent);
    }

    public void decodeContent() {
        String content = binding.etDecode.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastHelp.showShortMsg(this, "待解码内容不能为空");
            return;
        }
        String decodeContent = base64Decode(content, "UTF-8");
        Logger.e("decoded content is " + decodeContent);
        binding.etEncode.setText(decodeContent);
    }

    /**
     * CRLF：使用CR LF这一对作为一行的结尾而不是Unix风格的LF
     * DEFAULT：这个参数是默认，使用默认的方法来加密
     * NO_PADDING：这个参数是略去加密字符串最后的“=”
     * NO_WRAP：这个参数意思是略去所有的换行符（设置后CRLF就没用了）
     * URL_SAFE：这个参数意思是加密时不使用对URL和文件名有特殊意义的字符来作为加密字符，具体就是以-和_取代+和/
     * @param content
     * @param charsetName
     * @return
     */

    /**
     * Base64加密字符串
     * @param content -- 代加密字符串
     * @param charsetName -- 字符串编码方式
     * @return
     */
    private String base64Encode(String content, String charsetName) {
        if (TextUtils.isEmpty(charsetName)) {
            charsetName = "UTF-8";
        }
        try {
            byte[] contentByte = content.getBytes(charsetName);
            return Base64.encodeToString(contentByte, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Base64解密字符串
     * @param content -- 待解密字符串
     * @param charsetName -- 字符串编码方式
     * @return
     */
    private String base64Decode(String content, String charsetName) {
        if (TextUtils.isEmpty(charsetName)) {
            charsetName = "UTF-8";
        }
        byte[] contentByte = Base64.decode(content, Base64.DEFAULT);
        try {
            return new String(contentByte, charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
