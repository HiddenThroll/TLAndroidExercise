package com.tanlong.exercise.ndk;

public class NdkHelper {

    static {
        System.loadLibrary("math-lib");
    }

    public static native int addFromC(int a, int b);

}
