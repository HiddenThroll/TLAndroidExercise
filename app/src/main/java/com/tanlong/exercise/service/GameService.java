package com.tanlong.exercise.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

/**
 * @author 龙
 */
public class GameService extends Service{

    public static final int INDEX_QUERY_GAME_PRICE = 1;
    public static final String GAME_1 = "荒野求生";
    public static final String GAME_2 = "塞达尔传说";

    private Binder mBinder = new Binder() {
        @Override
        protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
            //code确认是哪一个方法
            if (code == INDEX_QUERY_GAME_PRICE) {
                //data获取输入参数
                String arg0 = data.readString();
                Logger.e("service 输入参数是" + arg0);
                //调用对应方法
                int result = getGamePrice(arg0);
                Logger.e("service 返回值是" + result);
                if (reply != null) {
                    //写返回值
                    reply.writeInt(result);
                }
                return true;
            }

            return super.onTransact(code, data, reply, flags);
        }
    };

    public int getGamePrice(String gameName) {
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
