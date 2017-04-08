package com.tanlong.exercise.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.tanlong.exercise.aidl.Book;
import com.tanlong.exercise.aidl.BookManager;
import com.tanlong.exercise.util.LogTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 龙 on 2017/4/7.
 */

public class AIDLService extends Service {

    public final String TAG = this.getClass().getSimpleName();

    private List<Book> mBooks = new ArrayList<>();

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
            if (mBooks == null) {
                mBooks = new ArrayList<>();
            }

            if (book == null) {
                book = new Book();
                book.setName("未命名");
            }

            //尝试修改book的参数，主要是为了观察其到客户端的反馈
            book.setPrice(6666);
            if (!mBooks.contains(book)) {
                mBooks.add(book);
            }
            //打印mBooks列表，观察客户端传过来的值
            LogTool.e(TAG, "book list " + new Gson().toJson(mBooks));
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Book book = new Book();
        book.setName("Android开发艺术探索");
        book.setPrice(28);
        mBooks.add(book);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogTool.e(TAG, "onBind " + intent.toString());
        return mBookManager;
    }
}
