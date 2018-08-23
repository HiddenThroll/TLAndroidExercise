package com.tanlong.exercise.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.tanlong.exercise.aidl.Book;
import com.tanlong.exercise.aidl.BookManager;
import com.tanlong.exercise.util.LogTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * AIDL服务Demo
 *
 * @author 龙
 * @date 2017/4/7
 */

public class BookService extends Service {

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
            synchronized (this) {
                mBooks.add(book);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        LogTool.e(TAG, "onCreate");
        mBooks.add(new Book("Android群英传", new Random().nextInt()));
        mBooks.add(new Book("Android开发艺术探索", new Random().nextInt()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogTool.e(TAG, "onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogTool.e(TAG, "onBind " + intent.toString());
        return mBookManager;
    }
}
