package com.tanlong.exercise.util;

import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EnDecryptionUtil {
/************************** Base64 begin *******************************************/
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
    public static String base64Encode(String content, String charsetName) {
        if (TextUtils.isEmpty(charsetName)) {
            charsetName = "UTF-8";
        }
        try {
            byte[] contentByte = content.getBytes(charsetName);
            return base64Encode(contentByte);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Base64加密byte[]
     * @param content
     * @return
     */
    public static String base64Encode(byte[] content) {
        return Base64.encodeToString(content, Base64.DEFAULT);
    }

    /**
     * Base64解密字符串
     * @param content -- 待解密字符串
     * @param charsetName -- 字符串编码方式
     * @return
     */
    public static String base64Decode(String content, String charsetName) {
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

    /**
     * Base64解密字符串
     * @param content
     * @return
     */
    public static byte[] base64Decode(String content) {
        return Base64.decode(content, Base64.DEFAULT);
    }
/************************** Base64 end *******************************************/

/************************** MD5 begin *******************************************/
    /**
     * 16位MD5加密
     * 实际是截取的32位加密结果的中间部分(8-24位)
     * @param content
     * @return
     */
    public static String md5Decode16(String content) {
        return md5Decode(content).substring(8, 24);
    }

    /**
     * 32位MD5加密
     * @param content -- 待加密内容
     * @return
     */
    public static String md5Decode(String content) {
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
/************************** MD5 end *******************************************/

/************************** AES begin *******************************************/
    /**
     * AES加密,返回BASE64编码后的加密字符串
     *
     * @param sSrc           -- 待加密内容
     * @param encodingFormat -- 字符串编码方式
     * @param algorithm      -- 使用的算法 算法/模式/补码方式, 目前支持ECB和CBC模式
     * @param sKey           -- 加密密钥
     * @param ivParameter    -- 偏移量,CBC模式时需要
     * @return Base64编码后的字符串
     * @throws Exception
     */
    public static String aesEncrypt(String sSrc, String encodingFormat, String algorithm, String sKey, String ivParameter) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        byte[] raw = sKey.getBytes(encodingFormat);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        //使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes(encodingFormat));
        if (algorithm.contains("CBC")) {
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        }
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(encodingFormat));
        //此处使用BASE64做转码。
        return EnDecryptionUtil.base64Encode(encrypted);
    }

    /**
     * AES解密
     * @param sSrc -- 待解密Base64字符串
     * @param encodingFormat -- 字符串编码方式
     * @param algorithm -- 使用的算法 算法/模式/补码方式, 目前支持ECB和CBC模式
     * @param sKey -- 加密密钥
     * @param ivParameter -- 偏移量,CBC模式时需要
     * @return 解密后的字符串
     * @throws Exception
     */
    public static String aesDecrypt(String sSrc, String encodingFormat, String algorithm, String sKey, String ivParameter) throws Exception {
        byte[] raw = sKey.getBytes(encodingFormat);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance(algorithm);
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes(encodingFormat));
        if (algorithm.contains("CBC")) {
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        } else {//ECB模式
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        }
        //先用base64解密
        byte[] encrypted1 = EnDecryptionUtil.base64Decode(sSrc);
        byte[] original = cipher.doFinal(encrypted1);
        return new String(original, encodingFormat);
    }
/************************** AES end *******************************************/
}
