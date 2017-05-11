package com.tanlong.exercise.model.entity;

/**
 * 坐标 实体类
 * Created by 龙 on 2017/5/10.
 */

public class PointItem {

    private float x;
    private float y;

    public PointItem() {
    }

    public PointItem(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
