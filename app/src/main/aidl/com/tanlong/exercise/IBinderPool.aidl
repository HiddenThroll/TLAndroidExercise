// IBinderPool.aidl
package com.tanlong.exercise;

// Declare any non-default types here with import statements

interface IBinderPool {
    //根据binderCode 获取 IBinder
    IBinder queryBinder(int binderCode);
}
