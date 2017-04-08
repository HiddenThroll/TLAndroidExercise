package com.tanlong.exercise.service;

/**
 * Created by 龙 on 2017/4/5.
 */

public class MessengerServiceContent {
    // 来自客户端的消息
    public static final int MSG_FROM_CLIENT = 1;
    // 来自服务器的消息
    public static final int MSG_FROM_SERVER = 2;
    // 传递数据使用的Key
    public static final String MSG_DATA_KEY = "msg_data_key";
    // 绑定的service在Manifests文件里面声明的action，用于Messenger
    public static final String SERVICE_ACTION = "com.tanlong.exercise.messenger.demo";
    // 绑定的service在Manifests文件里面声明的action，用于AIDL
    public static final String SERVICE_ACTION_AIDL = "com.tanlong.exercise.aidl.book";
}
