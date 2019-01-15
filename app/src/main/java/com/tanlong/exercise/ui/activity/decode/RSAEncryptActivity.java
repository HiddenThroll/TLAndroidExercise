package com.tanlong.exercise.ui.activity.decode;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;

import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityRsaEncryptBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.util.EnDecryptionUtil;


/**
 * @author 龙
 */
public class RSAEncryptActivity extends BaseActivity {
    ActivityRsaEncryptBinding binding;

    String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDO2TVhB1VIpUhNb5+owamkjmU8dNEMJyTbakBT" +
            "b7GysKaA+byMRqJLRAbVPj+eD15erREOkv9A1z4mOMo7i+7hb6J8LuktCDWC5QeusvbwlpOjjIE6" +
            "Sq8pETHPnHX5dd+ORFYWPrbd7drSIv0Fbm3R7zi0LhuJn3JWkLf1JEFDywIDAQAB";
    String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAM7ZNWEHVUilSE1vn6jBqaSOZTx0" +
            "0QwnJNtqQFNvsbKwpoD5vIxGoktEBtU+P54PXl6tEQ6S/0DXPiY4yjuL7uFvonwu6S0INYLlB66y" +
            "9vCWk6OMgTpKrykRMc+cdfl1345EVhY+tt3t2tIi/QVubdHvOLQuG4mfclaQt/UkQUPLAgMBAAEC" +
            "gYB+M3Xm4iN9dCI95JnDy4ymMp6/mQImaQeKuzPN9Dq1rCOaU0RfTYUdaL7GgfkshXHtT6g1fSgx" +
            "NmHbzhBM7l5qoYJNwz/8KQ9rTphdc0JQFzu3ECkDZvOe+yjSKWpuZFGizLsB2j3Og4MF3fOKNfN7" +
            "c1ucNNEYpzbGPyOPC23TWQJBAPOU5pLEwMV4VSC3LgJcjorvf5DQqJVpnPYyHRE7MFhsnVHfmbIq" +
            "nTBhOcdomwOde+yQjerGIi7W7RIjobgoZdUCQQDZZOLEo5qdIXn319R3ucPsAHLPcTa8kfxpUHLo" +
            "rN8INKUj7dR3FtWc9cye5fzWRg+NkBx6OC408l95qeGvBbMfAkEAqQJ7DgFJBHtfDcksOmVAXnSZ" +
            "XcD6CFn0l/rjok4gWGpcqi9stGvPD3+WmJ8jV9nQ367ZWbpKg5eLfReOIXqeVQJBAMuxwuVbIoE+" +
            "n8kBm1w/XHuig/EpdI9F/oszTSgE6soGggHjU6PuamMy0PLGLp0bcnFDadt/DpSf0aPu8L8NCSMC" +
            "QQDvL5cjxQqIPJSP1T+JKktvd+nWPULt7Adir1fab022e0XfEod73Eoo4rp0GmN0hSZUH0VBBvqf" +
            "lNY1P23tZP3C";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rsa_encrypt);
        binding.setActivity(this);


        try {
            EnDecryptionUtil.loadPublicKey(publicKey);
            EnDecryptionUtil.loadPrivateKey(privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        rsaUtil.genKeyPair();
//        Log.e("test", "公钥:" + EnDecryptionUtil.base64Encode(rsaUtil.getPublicKey().getEncoded()));
//        Log.e("test", "私钥:" + EnDecryptionUtil.base64Encode(rsaUtil.getPrivateKey().getEncoded()));
    }

    public void encodeContent() {
        String content = binding.etEncode.getText().toString();
        try {
            byte[] result = EnDecryptionUtil.encrypt(EnDecryptionUtil.getPublicKey(), content.getBytes("utf-8"));
            String encodeResult = EnDecryptionUtil.base64Encode(result);
            Log.e("test", "加密后Base64数据" + encodeResult);
            binding.etDecode.setText(encodeResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void decodeContent() {
        String content = binding.etDecode.getText().toString();
        byte[] decodedContent = EnDecryptionUtil.base64Decode(content);
        try {
            byte[] result = EnDecryptionUtil.decrypt(EnDecryptionUtil.getPrivateKey(), decodedContent);
            String decodeResult = new String(result, "utf-8");
            Log.e("test", "解密后数据" + decodeResult);
            binding.etEncode.setText(decodeResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
