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
import android.text.TextUtils;
import android.util.Log;

import com.tanlong.exercise.model.entity.MsgItem;
import com.tanlong.exercise.model.event.GetMsgItemEvent;
import com.tanlong.exercise.model.event.SetMessageReplyEvent;
import com.tanlong.exercise.util.LogTool;
import com.tanlong.exercise.util.ToastHelp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 通过Messenger实现IPC服务
 * Created by 龙 on 2017/4/5.
 */

public class MessengerServiceDemo extends Service {
    private final String TAG = getClass().getSimpleName();
    private String mReplyContent = "";

    class ServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msgFromClient) {
            Message msgToClient = Message.obtain(msgFromClient);
            switch (msgFromClient.what) {
                case MessengerServiceContent.MSG_FROM_CLIENT://接收到来自客户端的信息
                    String data = msgFromClient.getData().getString(MessengerServiceContent.MSG_DATA_KEY, "无内容");
                    ToastHelp.showShortMsg(getApplicationContext(), "服务器接收到客户端信息：" + data);

                    MsgItem msgItem = new MsgItem(data);
                    new GetMsgItemEvent().setData(msgItem).post();

                    msgToClient.what = MessengerServiceContent.MSG_FROM_SERVER;
                    if (!TextUtils.isEmpty(mReplyContent)) {
                        LogTool.e(TAG, "mReplyContent is " + mReplyContent);
                        Bundle bundle = new Bundle();
                        bundle.putString(MessengerServiceContent.MSG_DATA_KEY, mReplyContent);
                        msgToClient.setData(bundle);
                    }

                    try {
                        msgFromClient.replyTo.send(msgToClient);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        ToastHelp.showShortMsg(getApplicationContext(), "服务器回复客户端消息失败");
                    }

                    break;
            }
        }
    }
    // 接收客户端发送给服务器消息的Messenger
    final Messenger mMessenger = new Messenger(new ServiceHandler());

    @Override
    public void onCreate() {
        super.onCreate();
        LogTool.e(TAG, "onCreate");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onGetMsgReply(SetMessageReplyEvent event) {
        LogTool.e(TAG, "接收到SetMessageReplyEvent");
        mReplyContent = event.getData();
    }
}
