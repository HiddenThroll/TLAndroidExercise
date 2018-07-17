package com.tanlong.exercise.model.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * 订单费用中的附加/减免费用
 *
 * @author 龙
 */
@Entity(primaryKeys = {"price","name"})
public class EntityFee {
    public static final int TYPE_ADDITIONAL = 0;
    public static final int TYPE_REDUCE = 1;

    /**
     * 费用金额
     */
    @NonNull
    String price;
    /**
     * 费用名称
     */
    @NonNull
    String name;
    /**
     * 费用描述
     */
    String desc;
    /**
     * 费用类型,增加/减免费用,客户端本地维护该字段
     */
    int type;

    /**
     * 对应的订单ID,客户端本地维护该字段
     */
    long orderid;

    public long getOrderid() {
        return orderid;
    }

    public void setOrderid(long orderid) {
        this.orderid = orderid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
