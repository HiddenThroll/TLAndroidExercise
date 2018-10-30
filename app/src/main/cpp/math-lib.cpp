//
// Created by 龙 on 2018/10/30.
//
#include "jni.h"

extern "C"
JNIEXPORT jint JNICALL
Java_com_tanlong_exercise_ndk_NdkHelper_addFromC(JNIEnv *env, jobject thiz, jint a, jint b){
    return a + b;
}
