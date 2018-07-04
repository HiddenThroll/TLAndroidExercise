package com.tanlong.exercise.ui.activity;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.util.RSAUtil;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import Decoder.BASE64Encoder;

/**
 * @author 龙
 */
public class TestActivity extends BaseActivity {

    private final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCuyZxd4GCkDui/XXxw8k/YoSUK" + "\n" +
            "5gDwbVdPtl9FkUvpWD7IgxP3iU+OOuivp9a30C/VWsialCVlQoGIgCdZwydvFirR" + "\n" +
            "qguUkbgvfDuOW1JaH7LYC3qb4+h/YDxX6Ulf1IbvJYiKViFeDizGSfc4MhbZsRhq" + "\n" +
            "W7vyoXhDc/HO6ZaTWQIDAQAB" + "\n";

    private final String ALGORITHM = "RSA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String content = "abcdEfp294zIIP";
        Log.e("test", "加密前字符" + content);

        RSAUtil rsaUtil = new RSAUtil();
        try {
            rsaUtil.loadPublicKey(getAssets().open("rsa_public_key"));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("test", "加载公钥失败");
        }

        try {
            byte[] encryptBytes = rsaUtil.encrypt(rsaUtil.getPublicKey(), content.getBytes());
            BASE64Encoder base64Encoder = new BASE64Encoder();
            Log.e("test", "加密后数据 " + base64Encoder.encode(encryptBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public PublicKey getPublicKey() throws Exception {
        byte[] buffer = Base64.decode(PUBLIC_KEY, Base64.DEFAULT);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    public byte[] encrypt(String content) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
        return cipher.doFinal(content.getBytes());
    }

    public String base64ToString(byte[] encryptContent) {
        return Base64.encodeToString(encryptContent, Base64.DEFAULT);
    }
}
