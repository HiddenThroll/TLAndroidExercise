package com.tanlong.exercise.service;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import com.orhanobut.logger.Logger;

/**
 * Binder代理类
 * @author 龙
 */
public class GameBinderProxy implements IGameInterface {

    private IBinder mRemote;

    public GameBinderProxy(IBinder mRemote) {
        this.mRemote = mRemote;
    }

    @Override
    public int getPrice(String gameName) throws RemoteException {
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
            mRemote.transact(INDEX_QUERY_GAME_PRICE, data, reply, 0);
            //接收GameService相应方法返回值
            Logger.e("client 读取GameService指定方法的返回值");
            result = reply.readInt();
        } finally {
            data.recycle();
            reply.recycle();
        }
        return result;
    }
}
