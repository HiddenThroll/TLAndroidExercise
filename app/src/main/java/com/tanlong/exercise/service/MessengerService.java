package com.tanlong.exercise.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;

/**
 * 通过Messenger实现IPC服务
 *
 * @author 龙
 * @date 2017/4/5
 */

public class MessengerService extends Service {

    static class ServiceHandler extends Handler {

        private WeakReference<MessengerService> reference;

        public ServiceHandler(MessengerService service) {
            reference = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msgFromClient) {
            switch (msgFromClient.what) {
                case MessengerServiceContent.MSG_FROM_CLIENT:
                    //接收到来自客户端的信息
                    String data = msgFromClient.getData().getString(MessengerServiceContent.MSG_DATA_KEY, "无内容");
                    Logger.e("接收到客户端发送消息 " + data);
                    reference.get().replyClient(msgFromClient);
                    break;
                default:
                    break;
            }
        }
    }

    private void replyClient(Message msgFromClient) {
        String data = msgFromClient.getData().getString(MessengerServiceContent.MSG_DATA_KEY,
                "无内容");
        String result = "已收到信息 " + data;
        Message message = Message.obtain();
        message.what = MessengerServiceContent.MSG_FROM_SERVER;
        Bundle bundle = new Bundle();
        bundle.putString(MessengerServiceContent.MSG_DATA_KEY, result);
        message.setData(bundle);
        Messenger clientMessenger = msgFromClient.replyTo;
        if (clientMessenger != null) {
            try {
                clientMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     *  接收客户端发送给服务器消息的Messenger
     */
    final Messenger mMessenger = new Messenger(new ServiceHandler(this));

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
