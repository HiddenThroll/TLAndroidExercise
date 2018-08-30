package com.tanlong.exercise.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.print.PrinterId;

import com.tanlong.exercise.IBinderPool;

import java.util.concurrent.CountDownLatch;

public class BinderPool {
    public static final int BINDER_SECURITY_CENTER = 1;
    public static final int BINDER_GAME = 2;

    private Context mContext;
    private IBinderPool iBinderPool;
    private static volatile BinderPool instance;

    private CountDownLatch mCountDownLatch;

    private BinderPool(Context context) {
        mContext = context.getApplicationContext();
        connectBinderPoolService();
    }

    private synchronized void connectBinderPoolService() {
        mCountDownLatch = new CountDownLatch(1);
        Intent intent = new Intent(mContext, BinderPoolService.class);
        mContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        try {
            mCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iBinderPool = IBinderPool.Stub.asInterface(service);
            mCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public static BinderPool getInstance(Context context) {
        if (instance == null) {
            synchronized (BinderPool.class) {
                if (instance == null) {
                    instance = new BinderPool(context);
                }
            }
        }
        return instance;
    }

    public IBinder queryBinder(int binderCode) {
        IBinder iBinder = null;
        try {
            if (iBinderPool != null) {
                iBinder = iBinderPool.queryBinder(binderCode);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return iBinder;
    }

    public static class BinderPoolImpl extends IBinderPool.Stub {
        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder iBinder = null;
            switch (binderCode) {
                case BINDER_SECURITY_CENTER:
                    iBinder = new SecurityCenterImpl();
                    break;
                case BINDER_GAME:
                    iBinder = new GameServiceImpl();
                    break;
                default:
                    break;
            }
            return iBinder;
        }
    }
}
