package com.woasis.taxi.maplibrary.model;

import java.io.Serializable;

/**
 * 地图覆盖物携带业务数据的基类
 * Created by 龙 on 2016/7/21.
 */
public class MarkDataBase<T> implements Serializable {
    public static final String MARKER_DATA = "marker_data";
    /** 携带的数据*/
    T data;

    public MarkDataBase() {
    }

    public T getData() {

        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
