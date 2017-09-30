package com.tanlong.exercise.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.tanlong.exercise.aidl.Book;
import com.tanlong.exercise.aidl.BookManager;
import com.tanlong.exercise.model.event.AddBookEvent;
import com.tanlong.exercise.model.event.SetBookEvent;
import com.tanlong.exercise.util.LogTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * AIDL服务Demo
 * Created by 龙 on 2017/4/7.
 */

public class AIDLService extends Service {

    public final String TAG = this.getClass().getSimpleName();

    private List<Book> mBooks = new ArrayList<>();

    private Book mSetBook;

    //由AIDL文件生成的BookManager
    private BookManager.Stub mBookManager = new BookManager.Stub() {

        @Override
        public List<Book> getBooks() throws RemoteException {
            synchronized (this) {
                if (mBooks != null) {
                    return mBooks;
                }
                return new ArrayList<>();
            }
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            synchronized (this) {
                //尝试修改book的参数，主要是为了观察其到客户端的反馈
                if (mSetBook != null) {
                    book.setPrice(mSetBook.getPrice());
                }
                mBooks.add(book);

                new AddBookEvent().setData(book).post();
            }

        }
    };

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
        LogTool.e(TAG, "onBind " + intent.toString());
        return mBookManager;
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onSetBookEvent(SetBookEvent event) {
        mSetBook = event.getData();
    }
}
