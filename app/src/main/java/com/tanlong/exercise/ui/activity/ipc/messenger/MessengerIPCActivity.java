package com.tanlong.exercise.ui.activity.ipc.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.model.event.GetMsgItemEvent;
import com.tanlong.exercise.model.event.SetMessageReplyEvent;
import com.tanlong.exercise.service.MessengerServiceDemo;
import com.tanlong.exercise.ui.activity.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Messenger实现IPC
 * Created by 龙 on 2017/4/5.
 */

public class MessengerIPCActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;
    @Bind(R.id.tb_toolbar)
    Toolbar tbToolbar;
    @Bind(R.id.et_reply)
    EditText etReply;
    @Bind(R.id.btn_confirm_reply)
    Button btnConfirmReply;
    @Bind(R.id.lv_msg)
    ListView lvMsg;

    List<String> mListData;
    ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_messenger_ipc);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initData();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        mListData = new ArrayList<>();
        startMessengerService();
    }

    private void initView() {
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mListData);
        lvMsg.setAdapter(mAdapter);
    }

    /**
     * 对于魅族手机，需要通过startService启动服务后，其他应用才能绑定该服务，操。。。
     * 对于华为手机，当bindService失败时，尝试将提供服务的APP设置为全部信任，操。。。
     */
    private void startMessengerService() {
        startService(new Intent(this, MessengerServiceDemo.class));
    }

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
                setReplyContent();
                break;
        }
    }

    private void showTips() {

    }

    private void setReplyContent() {
        String reply = etReply.getText().toString();
        if (TextUtils.isEmpty(reply)) {
            showShortMessage("请输入回复内容");
            return;
        }
        new SetMessageReplyEvent().setData(reply).post();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMsgItemEvent(GetMsgItemEvent event) {
        mListData.add(event.getData().getMsgSend());
        mAdapter.notifyDataSetChanged();
    }
}
