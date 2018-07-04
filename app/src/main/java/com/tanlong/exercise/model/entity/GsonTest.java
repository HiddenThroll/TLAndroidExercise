package com.tanlong.exercise.model.entity;

import com.google.gson.annotations.SerializedName;

public class GsonTest {
    @SerializedName(value = "nameA")
    private String a;
    @SerializedName(value = "nameB", alternate = {"nameb"})
    private String b;
    private String c;

    public GsonTest(String a, String b, String c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }
}
