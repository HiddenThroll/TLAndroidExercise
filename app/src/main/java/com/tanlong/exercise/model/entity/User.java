package com.tanlong.exercise.model.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.tanlong.exercise.BR;

/**
 * Created by 龙 on 2018/1/24.
 */

public class User extends BaseObservable{

    private String name;
    private Integer age;

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
        notifyPropertyChanged(BR.age);
    }
}
