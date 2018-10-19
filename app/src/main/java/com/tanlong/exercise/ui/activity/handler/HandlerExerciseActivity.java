package com.tanlong.exercise.ui.activity.handler;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.orhanobut.logger.Logger;
import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityHandlerExerciseBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;

import java.lang.ref.WeakReference;

/**
 * @author 龙
 */
public class HandlerExerciseActivity extends BaseActivity {
    ActivityHandlerExerciseBinding binding;

    private static final int MSG_REFRESH_UI = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_handler_exercise);
        binding.setActivity(this);
    }

    private Handler handlerMsg = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH_UI:
                    refreshUI((String) msg.obj);
                default:
                    break;
            }
        }
    };

    public void sendMsgToRefreshUI() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = Message.obtain();
                message.what = MSG_REFRESH_UI;
                message.obj = "send Message 更新UI";
                handlerMsg.sendMessage(message);
            }
        }).start();
    }

    private void refreshUI(String content) {
        binding.tvTest.setText(content);
    }

    public void postRunnableToRefreshUI() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                handlerMsg.post(new Runnable() {
                    @Override
                    public void run() {
                        refreshUI("post Runnable 更新UI");
                    }
                });
            }
        }).start();
    }


    private static class DelayHandler extends Handler {

        final WeakReference<HandlerExerciseActivity> reference;

        public DelayHandler(HandlerExerciseActivity activity) {
            reference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_REFRESH_UI:
                    Logger.e("DelayHandler接收到消息");
                    reference.get().refreshUI((String) msg.obj);
                    break;
                default:
                    break;
            }
        }
    }


    DelayHandler delayHandler;

    public void sendDelayMessage() {
        Logger.e("sendDelayMessage");
        delayHandler = new DelayHandler(this);
        Message msg = Message.obtain();
        msg.what = MSG_REFRESH_UI;
        msg.obj = "延迟消息更新界面";
        delayHandler.sendMessageDelayed(msg, 60 * 1000);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Logger.e("onDestroy");
        //建议进行GC操作, 触发Activity被销毁,释放弱引用
        System.gc();
        delayHandler.removeMessages(MSG_REFRESH_UI);
    }
}
