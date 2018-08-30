package com.tanlong.exercise.service;

import android.os.RemoteException;

import com.tanlong.exercise.IGameService;

/**
 * IGameService.Stub的实现类
 * @author 龙
 */
public class GameServiceImpl extends IGameService.Stub {
    @Override
    public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
        //do nothing
    }

    @Override
    public int getPrice(String gameName) throws RemoteException {
        int price = -1;
        switch (gameName) {
            case IGameInterface.GAME_1:
                price = 123;
                break;
            case IGameInterface.GAME_2:
                price = 998;
            default:
                break;
        }
        return price;
    }
}
