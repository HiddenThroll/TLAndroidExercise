package com.tanlong.exercise.ui.activity.ipc.aidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.tanlong.exercise.IGameService;
import com.tanlong.exercise.IOnNewBookArrivedListener;
import com.tanlong.exercise.R;
import com.tanlong.exercise.aidl.Book;
import com.tanlong.exercise.aidl.BookManager;
import com.tanlong.exercise.service.BookService;
import com.tanlong.exercise.service.GameService;
import com.tanlong.exercise.service.IGameInterface;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.activity.view.listview.adapter.BookAdapter;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author 龙
 * @date 2017/4/10
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

    IGameService mRemote;
    BookManager mRemoteBookManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_aidl_ipc);
        ButterKnife.bind(this);

        mListBook = new ArrayList<>();

        bindBookService();
//        bindGameService();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRemoteBookManager != null && mRemoteBookManager.asBinder().isBinderAlive()) {
            try {
                mRemoteBookManager.unregisterListener(bookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

//        unbindService(gameServiceConnection);
        unbindService(bookServiceConnection);
    }

    private void initView() {
        btnHelp.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.aidl_ipc);
        mAdapter = new BookAdapter(this, mListBook, R.layout.item_book_list);
        lvBook.setAdapter(mAdapter);
    }

    private void bindBookService() {
        Intent intent = new Intent(this, BookService.class);
        bindService(intent, bookServiceConnection, BIND_AUTO_CREATE);
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
                addBook();
                break;
            case R.id.btn_query_game_price:
                queryGamePrice();
                break;
            default:
                break;
        }
    }

    private void addBook() {
        String bookName = etReply.getText().toString();
        Book book = new Book(bookName, new Random().nextInt());
        if (mRemoteBookManager != null) {
            try {
                mRemoteBookManager.addBook(book);
                Logger.e("book list is " + new Gson().toJson(mRemoteBookManager.getBooks()));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void queryGamePrice() {
        if (mRemote == null) {
            return;
        }
        try {
            Logger.e("queryGamePrice " + mRemote.getPrice(IGameInterface.GAME_1));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
            mRemote = IGameService.Stub.asInterface(service);
            Logger.e("game service connected " + name);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemote = null;
            Logger.e("game service disconnected " + name);
        }
    };

    private ServiceConnection bookServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mRemoteBookManager = BookManager.Stub.asInterface(service);
            Logger.e("book service connected " + name);
            try {
                mRemoteBookManager.registerListener(bookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            try {
                Logger.e("book list is " + new Gson().toJson(mRemoteBookManager.getBooks()));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteBookManager = null;
            Logger.e("book service connected " + name);
        }
    };

    private IOnNewBookArrivedListener bookArrivedListener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            Logger.e(Thread.currentThread().getName() + "新到书籍 " + newBook.toString());
        }
    };
}
