package com.tanlong.exercise.ui.activity.decode;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;

import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityRsaEncryptBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.util.EnDecryptionUtil;
import com.tanlong.exercise.util.RSAUtil;

/**
 * @author 龙
 */
public class RSAEncryptActivity extends BaseActivity {
    ActivityRsaEncryptBinding binding;
    RSAUtil rsaUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rsa_encrypt);
        binding.setActivity(this);

        rsaUtil = new RSAUtil();
        rsaUtil.genKeyPair();
        Log.e("test", "公钥:" + EnDecryptionUtil.base64Encode(rsaUtil.getPublicKey().getEncoded()));
        Log.e("test", "私钥:" + EnDecryptionUtil.base64Encode(rsaUtil.getPrivateKey().getEncoded()));
    }

    public void encodeContent() {
        String content = binding.etEncode.getText().toString();
        try {
            byte[] result = rsaUtil.encrypt(rsaUtil.getPublicKey(), content.getBytes("utf-8"));
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
            byte[] result = rsaUtil.decrypt(rsaUtil.getPrivateKey(), decodedContent);
            String decodeResult = new String(result, "utf-8");
            Log.e("test", "解密后数据" + decodeResult);
            binding.etEncode.setText(decodeResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
