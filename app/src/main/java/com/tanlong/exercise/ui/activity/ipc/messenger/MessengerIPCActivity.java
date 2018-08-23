package com.tanlong.exercise.ui.activity.ipc.messenger;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tanlong.exercise.R;
import com.tanlong.exercise.service.MessengerServiceContent;
import com.tanlong.exercise.service.MessengerService;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Messenger实现IPC
 * Created by 龙 on 2017/4/5.
 */

public class MessengerIPCActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_help)
    Button btnHelp;
    @BindView(R.id.tb_toolbar)
    Toolbar tbToolbar;
    @BindView(R.id.et_reply)
    EditText etReply;
    @BindView(R.id.btn_confirm_reply)
    Button btnConfirmReply;
    @BindView(R.id.lv_msg)
    ListView lvMsg;

    List<String> mListData;
    ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_messenger_ipc);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    private void initData() {
        mListData = new ArrayList<>();
        clientMessenger = new Messenger(new ClientHandler());
        bindMessengerService();
    }

    private void initView() {
        tvTitle.setText(R.string.messenger_ipc);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mListData);
        lvMsg.setAdapter(mAdapter);
        btnHelp.setVisibility(View.VISIBLE);
    }

    /**
     * 对于魅族手机，需要通过startService启动服务后，其他应用才能绑定该服务，操。。。
     * 对于华为手机，当bindService失败时，尝试将提供服务的APP设置为全部信任，操。。。
     */
    private void bindMessengerService() {
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private Messenger serviceMessenger;
    private Messenger clientMessenger;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceMessenger = new Messenger(service);
            Logger.e("onServiceConnected " + name);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceMessenger = null;
            Logger.e("onServiceDisconnected " + name);
        }
    };

    @OnClick({R.id.iv_back, R.id.btn_help, R.id.btn_confirm_reply})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                showTips();
                break;
            case R.id.btn_confirm_reply:
                sendMsgToService();
                break;
            default:
                break;
        }
    }

    private void showTips() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Messenger实现IPC步骤：\n")
                .append("1. 服务端设置\n")
                .append("1.1 实现一个Handler，由其处理来自客户端的每个消息，构造返回客户端的消息，并通过Message.replyTo.send(Message)方法发送返回给客户端的消息，这里的第一个Message是服务器接收的来自客户端的消息，第二个Message是回复给客户端的消息\n")
                .append("1.2 使用实现的Handler创建Messenger对象\n")
                .append("1.3 将Messenger.getBinder()方法获得的一个IBinder对象，通过Service.onBind()方法返回给客户端\n")
                .append("2. 客户端配置\n")
                .append("2.1 实现一个Handler，由其处理来自服务端的返回消息\n")
                .append("2.2 使用实现的Handler创建Messenger对象HandleServerMessenger\n")
                .append("2.3 客户端使用服务连接成功时返回的IBinder实例化Messenger（引用服务端的Handler），然后使用它将Message对象发送给服务，发送消息时，调用 Message.replyTo = HandleServerMessenger 设置处理服务器返回消息的Messenger\n")
                .append("3. 绑定服务\n")
                .append("3.1 服务端设置\n")
                .append("3.1.1 在AndroidManifest文件中注册服务时，设置intent-filter的action和category\n")
                .append("3.1.2 对于魅族手机，需要通过startService启动服务后，其他应用才能绑定该服务\n")
                .append("3.1.3 对于华为手机，当bindService失败时，尝试将提供服务的APP设置为全部信任\n")
                .append("3.2 客户端设置\n")
                .append("3.2.1 新建Intent，设置action, 此处的action是service在Manifests文件里面声明的\n")
                .append("3.2.2 自Android 5.0起，谷歌不允许通过隐式Intent启动服务，故需调用Intent.setComponent(ComponentName)方法，指明服务的包名和类名\n");
        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }

    private void sendMsgToService() {
        String reply = etReply.getText().toString();
        if (TextUtils.isEmpty(reply)) {
            showShortMessage("请输入发送内容");
            return;
        }
        if (serviceMessenger == null) {
            bindMessengerService();
            return;
        }
        Message msg = Message.obtain();
        msg.what = MessengerServiceContent.MSG_FROM_CLIENT;
        Bundle bundle = new Bundle();
        bundle.putString(MessengerServiceContent.MSG_DATA_KEY, reply);
        msg.setData(bundle);
        //设置回复消息使用的Messenger
        msg.replyTo = clientMessenger;
        try {
            serviceMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Handler {
        public ClientHandler() {
            super();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MessengerServiceContent.MSG_FROM_SERVER:
                    String data = msg.getData().getString(MessengerServiceContent.MSG_DATA_KEY);
                    Logger.e("接收到服务器回复 " + data);
                    break;
                default:
                    break;
            }
        }
    }
}
