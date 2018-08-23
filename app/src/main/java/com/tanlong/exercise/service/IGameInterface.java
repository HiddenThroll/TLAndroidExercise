package com.tanlong.exercise.service;

import android.os.RemoteException;

public interface IGameInterface {
    int INDEX_QUERY_GAME_PRICE = 1;

    String GAME_1 = "荒野求生";
    String GAME_2 = "塞达尔传说";

    int getPrice(String gameName) throws RemoteException;
}
