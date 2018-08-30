// ISecurityCenter.aidl
package com.tanlong.exercise;

// Declare any non-default types here with import statements

interface ISecurityCenter {
    String encrypt(String content);
    String decrypt(String deContent);
}
