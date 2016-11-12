package com.tanlong.exercise.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tanlong.exercise.ui.fragment.base.BaseFragment;

/**
 * 无UI的Fragment, 用于Activity重建时保存数据
 * Created by Administrator on 2016/11/12.
 */

public class DataRetainFragment<T> extends BaseFragment {
    public static final String TAG = "DataRetainFragment";
    private T data;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);//Activity重建时保存实例
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
