package com.tanlong.exercise.ui.activity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.util.RSAUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import Decoder.BASE64Encoder;

/**
 * @author 龙
 */
public class TestActivity extends BaseActivity implements View.OnClickListener {

    private final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCuyZxd4GCkDui/XXxw8k/YoSUK" + "\n" +
            "5gDwbVdPtl9FkUvpWD7IgxP3iU+OOuivp9a30C/VWsialCVlQoGIgCdZwydvFirR" + "\n" +
            "qguUkbgvfDuOW1JaH7LYC3qb4+h/YDxX6Ulf1IbvJYiKViFeDizGSfc4MhbZsRhq" + "\n" +
            "W7vyoXhDc/HO6ZaTWQIDAQAB" + "\n";

    private final String ALGORITHM = "RSA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);
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
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_write_file).setOnClickListener(this);
        findViewById(R.id.btn_read_file).setOnClickListener(this);
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

    private final String path = Environment.getExternalStorageDirectory() + File.separator
            + "test" + File.separator;
    private final String fileName = "test.txt";

    public void writeFile() {
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File file = new File(path + fileName);
        if (file.exists()) {
            file.delete();
        }

        FileOutputStream outputStream = null;
        try {
            file.createNewFile();
            outputStream = new FileOutputStream(file);
            String temp;
            for (int i = 0; i < 100; i++) {
                temp = "temp_" + i + "\n";
                outputStream.write(temp.getBytes("UTF-8"));
            }
            Logger.e("已写入文件");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void readFile() {
        File file = new File(path + fileName);
        if (!file.exists()) {
            Logger.e("文件不存在" + file.getAbsolutePath());
            return;
        }
        FileReader inputStream = null;
        try {
            inputStream = new FileReader(file);
            StringBuilder stringBuilder = new StringBuilder();
            char[] buffer = new char[256];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                Logger.e("len is " + len);
                if (len < 256) {
                    stringBuilder.append(buffer, 0, len);
                } else {
                    stringBuilder.append(buffer);
                }
            }
            Logger.e("stringBuilder length is " + stringBuilder.length());
            String result = stringBuilder.toString();
            Logger.e("result is " + result.split("\n").length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_write_file:
                writeFile();
                break;
            case R.id.btn_read_file:
                readFile();
                break;
            default:
                break;
        }
    }
}
