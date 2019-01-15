package com.tanlong.exercise.util;

import android.text.TextUtils;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author 龙
 */
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

/************************** RSA begin *******************************************/
    /**
     * 私钥
     */
    private static RSAPrivateKey privateKey;

    /**
     * 公钥
     */
    private static RSAPublicKey publicKey;

    /**
     * 字节数据转字符串专用集合
     */
    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    /**
     * 获取私钥
     *
     * @return 当前的私钥对象
     */
    public static RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * 获取公钥
     *
     * @return 当前的公钥对象
     */
    public static RSAPublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * 随机生成密钥对
     */
    public static void genKeyPair() {
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyPairGen.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        privateKey = (RSAPrivateKey) keyPair.getPrivate();
        publicKey = (RSAPublicKey) keyPair.getPublic();
    }

    /**
     * 从文件输入流中加载公钥
     *
     * @param in 公钥输入流
     * @throws Exception 加载公钥时产生的异常
     */
    public static void loadPublicKey(InputStream in) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPublicKey(sb.toString());
        } catch (IOException e) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥输入流为空");
        }
    }


    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public static void loadPublicKey(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = EnDecryptionUtil.base64Decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 从文件输入流中加载私钥
     *
     * @param in 私钥输入流
     * @return 是否成功
     * @throws Exception
     */
    public static void loadPrivateKey(InputStream in) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPrivateKey(sb.toString());
        } catch (IOException e) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥输入流为空");
        }
    }

    /**
     * 从字符串中加载私钥
     * @param privateKeyStr
     * @throws Exception
     */
    public static void loadPrivateKey(String privateKeyStr) throws Exception {
        try {
            byte[] buffer = EnDecryptionUtil.base64Decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    /**
     * RSA加密过程
     *
     * @param publicKey     公钥
     * @param plainTextData 明文数据
     * @return
     * @throws Exception 加密过程中的异常信息
     */
    public static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception {
        if (publicKey == null) {
            throw new Exception("加密公钥为空, 请设置");
        }
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(plainTextData);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
    }

    /**
     * RSA解密
     *
     * @param privateKey 私钥
     * @param cipherData 密文数据
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception {
        if (privateKey == null) {
            throw new Exception("解密私钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(cipherData);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("解密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        }
    }


    /**
     * 字节数据转十六进制字符串
     *
     * @param data 输入数据
     * @return 十六进制内容
     */
    public static String byteArrayToString(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            //取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
            //取出字节的低四位 作为索引得到相应的十六进制标识符
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
            if (i < data.length - 1) {
                stringBuilder.append(' ');
            }
        }
        return stringBuilder.toString();
    }
/************************** RSA end *******************************************/
}
