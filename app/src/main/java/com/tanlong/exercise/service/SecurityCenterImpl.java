package com.tanlong.exercise.service;

import android.os.RemoteException;

import com.tanlong.exercise.ISecurityCenter;

/**
 * ISecurityCenter.Stub的实现类
 * @author 龙
 */
public class SecurityCenterImpl extends ISecurityCenter.Stub {
    private final static char SECRET_CHAR = '^';
    @Override
    public String encrypt(String content) throws RemoteException {
        char[] chars = content.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] ^= SECRET_CHAR;
        }
        return new String(chars);
    }

    @Override
    public String decrypt(String deContent) throws RemoteException {
        return encrypt(deContent);
    }
}
