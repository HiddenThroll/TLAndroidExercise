package com.tanlong.exercise.model.entity;

import android.util.Log;

public class Cat {

    private final String name;
    public int age;

    public Cat(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

//    public void setName(String name) {
//        this.name = name;
//    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean catchMouse(String mouseName, long catchTime) {
        Log.e("test", "catch mouse " + mouseName);
        return true;
    }

    private boolean figthDogs(String[] dogNames, long[] time) {
        Log.e("test", "figthDogs " + dogNames.length);
        return true;
    }
}
