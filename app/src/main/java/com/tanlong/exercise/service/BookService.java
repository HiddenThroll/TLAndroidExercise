package com.tanlong.exercise.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tanlong.exercise.IOnNewBookArrivedListener;
import com.tanlong.exercise.aidl.Book;
import com.tanlong.exercise.aidl.BookManager;
import com.tanlong.exercise.util.LogTool;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * AIDL服务Demo
 *
 * @author 龙
 * @date 2017/4/7
 */

public class BookService extends Service {

    public final String TAG = this.getClass().getSimpleName();

    private CopyOnWriteArrayList<Book> bookList = new CopyOnWriteArrayList<>();
    /**
     * 使用RemoteCallbackList存储跨进程Listener
     * RemoteCallbackList内部使用一个Map来存储接口,key是IBinder,value是Callback,Callback封装了真实的接口
     * RemoteCallbackList并不是一个List,遍历RemoteCallbackList需配对使用beginBroadcast和finishBroadcast
     */
    private RemoteCallbackList<IOnNewBookArrivedListener> listenerList = new RemoteCallbackList<>();
    private AtomicBoolean isServiceDestroyed = new AtomicBoolean(false);

    private BookManager.Stub mBookManager = new BookManager.Stub() {

        @Override
        public List<Book> getBooks() throws RemoteException {
            return bookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            bookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            listenerList.register(listener);
            int size = listenerList.beginBroadcast();
            Log.e(TAG, "register listener current size is " + size);
            listenerList.finishBroadcast();
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            listenerList.unregister(listener);

            int size = listenerList.beginBroadcast();
            Log.e(TAG, "unregister listener current size is " + size);
            listenerList.finishBroadcast();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        LogTool.e(TAG, "onCreate");
        bookList.add(new Book("Android群英传", new Random().nextInt()));
        bookList.add(new Book("Android开发艺术探索", new Random().nextInt()));
        new Thread(new ServiceWorker()).start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LogTool.e(TAG, "onDestroy");
        isServiceDestroyed.set(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        getPackageManager().getPackagesForUid(Binder.getCallingUid());
        LogTool.e(TAG, "onBind " + intent.toString());
        return mBookManager;
    }

    private class ServiceWorker implements Runnable {

        @Override
        public void run() {
            while (!isServiceDestroyed.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //每过5s添加一本图书
                int bookSize = bookList.size() + 1;
                Book book = new Book("new Book " + bookSize, new Random().nextInt());
                onNewBookArrived(book);
            }
        }
    }

    private void onNewBookArrived(Book book) {
        //加入列表
        bookList.add(book);
        int size = listenerList.beginBroadcast();
        //通知注册用户
        for (int i = 0; i < size; i++) {
            try {
                listenerList.getBroadcastItem(i).onNewBookArrived(book);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        listenerList.finishBroadcast();
    }
}
