package com.tanlong.exercise.ui.activity.ipc.aidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tanlong.exercise.R;
import com.tanlong.exercise.aidl.Book;
import com.tanlong.exercise.model.event.AddBookEvent;
import com.tanlong.exercise.model.event.SetBookEvent;
import com.tanlong.exercise.service.AIDLService;
import com.tanlong.exercise.service.GameService;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.activity.view.listview.adapter.BookAdapter;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;
import com.tanlong.exercise.util.ToastHelp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by 龙 on 2017/4/10.
 */

public class AIDLIPCActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_help)
    Button btnHelp;
    @BindView(R.id.et_reply)
    EditText etReply;
    @BindView(R.id.btn_confirm_book)
    Button btnConfirmReply;
    @BindView(R.id.lv_book)
    ListView lvBook;
    @BindView(R.id.btn_query_game_price)
    Button btnQueryGamePrice;

    BookAdapter mAdapter;
    List<Book> mListBook;

    IBinder gameServiceBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_aidl_ipc);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mListBook = new ArrayList<>();

        startBookAIDLService();
        bindGameService();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        btnHelp.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.aidl_ipc);
        mAdapter = new BookAdapter(this, mListBook, R.layout.item_book_list);
        lvBook.setAdapter(mAdapter);
    }

    private void startBookAIDLService() {
        Intent intent = new Intent(this, AIDLService.class);
        startService(intent);
    }

    private void bindGameService() {
        Intent intent = new Intent(this, GameService.class);
        bindService(intent, gameServiceConnection, BIND_AUTO_CREATE);
    }

    @OnClick({R.id.iv_back, R.id.btn_help, R.id.btn_confirm_book, R.id.btn_query_game_price})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                showTips();
                break;
            case R.id.btn_confirm_book:
                setBook();
                break;
            case R.id.btn_query_game_price:
                queryGamePrice();
                break;
            default:
                break;
        }
    }

    private void queryGamePrice() {
        if (gameServiceBinder == null) {
            return;
        }
        try {
            Logger.e("queryGamePrice " + getPrice(GameService.GAME_1));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private int getPrice(String gameName) throws RemoteException{
        //构造输入参数
        Parcel data = Parcel.obtain();
        //接收返回值
        Parcel reply = Parcel.obtain();
        int result;
        try {
            //写输入参数
            data.writeString(gameName);
            //通过Binder调用GameService的指定方法, 之后线程挂起
            Logger.e("client 调用GameService的指定方法");
            gameServiceBinder.transact(GameService.INDEX_QUERY_GAME_PRICE, data, reply, 0);
            //接收GameService相应方法返回值
            Logger.e("client 读取GameService指定方法的返回值");
            result = reply.readInt();
        } finally {
            data.recycle();
            reply.recycle();
        }
        return result;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddBookEvent(AddBookEvent event) {
        mListBook.add(event.getData());
        mAdapter.notifyDataSetChanged();
    }

    private void setBook() {
        String price = etReply.getText().toString();
        if (TextUtils.isEmpty(price)) {
            ToastHelp.showShortMsg(this, "请输入价格");
            return;
        }
        Book book = new Book();
        book.setPrice(Integer.valueOf(price));
        new SetBookEvent().setData(book).post();
    }

    private void showTips() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("详见已收藏文章“Android：学习AIDL，这一篇文章就够了(上)");

        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }



    private ServiceConnection gameServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            gameServiceBinder = service;
            Logger.e("game service connected " + name);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            gameServiceBinder = null;
            Logger.e("game service disconnected " + name);
        }
    };
}
