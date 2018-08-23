package com.tanlong.exercise.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

/**
 * 通过Messenger实现IPC服务
 * Created by 龙 on 2017/4/5.
 */

public class MessengerServiceDemo extends Service {

    static class ServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msgFromClient) {
            switch (msgFromClient.what) {
                case MessengerServiceContent.MSG_FROM_CLIENT:
                    //接收到来自客户端的信息
                    String data = msgFromClient.getData().getString(MessengerServiceContent.MSG_DATA_KEY, "无内容");
                    Logger.e("接收到客户端发送消息 " + data);
                    break;
                default:
                    break;
            }
        }
    }
    /**
     *  接收客户端发送给服务器消息的Messenger
     */
    final Messenger mMessenger = new Messenger(new ServiceHandler());

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.e("onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.e("onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Logger.e("onBind");
        return mMessenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.e("onUnbind " + intent.toString());
        return super.onUnbind(intent);
    }
}
