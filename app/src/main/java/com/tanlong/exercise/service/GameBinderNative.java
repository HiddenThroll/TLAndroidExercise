package com.tanlong.exercise.service;

import android.os.Binder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

/**
 * Binder实现类
 * @author 龙
 */
public class GameBinderNative extends Binder implements IGameInterface {

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        //code确认是哪一个方法
        if (code == INDEX_QUERY_GAME_PRICE) {
            //data获取输入参数
            String arg0 = data.readString();
            Logger.e("service 输入参数是" + arg0);
            //调用对应方法
            int result = getPrice(arg0);
            Logger.e("service 返回值是" + result);
            if (reply != null) {
                //写返回值
                reply.writeInt(result);
            }
            return true;
        }

        return super.onTransact(code, data, reply, flags);
    }

    @Override
    public int getPrice(String gameName) throws RemoteException {
        Logger.e("service thread is " + Thread.currentThread().getName());

        int price = -1;
        switch (gameName) {
            case GAME_1:
                price = 123;
                break;
            case GAME_2:
                price = 998;
            default:
                break;
        }
        return price;
    }
}
