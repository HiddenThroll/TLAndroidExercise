package com.tanlong.exercise.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.tanlong.exercise.IGameService;

import static com.tanlong.exercise.service.IGameInterface.GAME_1;
import static com.tanlong.exercise.service.IGameInterface.GAME_2;

/**
 * @author 龙
 */
public class GameService extends Service{

//    private Binder mBinder = new GameBinderNative();

    private Binder mBinder = new IGameService.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public int getPrice(String gameName) throws RemoteException {
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
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
