package com.tanlong.exercise.model.event;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;

/**
 *
 * Created by 龙 on 2017/4/5.
 */

public class BaseEvent<T> implements Serializable{

    private T data;

    public T getData() {
        return data;
    }

    public BaseEvent setData(T data) {
        this.data = data;
        return this;
    }

    public void post() {
        EventBus.getDefault().post(this);
    }
}
