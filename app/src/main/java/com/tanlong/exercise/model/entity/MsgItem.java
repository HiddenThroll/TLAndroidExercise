package com.tanlong.exercise.model.entity;

/**
 * Created by 龙 on 2017/4/5.
 */

public class MsgItem {

    private static int curCount = 0;

    private int id;
    private String msgSend;
    private String msgReceive;

    public MsgItem() {
        curCount++;
        id = curCount;
    }

    public MsgItem(String msgSend) {
        this();
        this.msgSend = msgSend;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsgSend() {
        return msgSend;
    }

    public void setMsgSend(String msgSend) {
        this.msgSend = msgSend;
    }

    public String getMsgReceive() {
        return msgReceive;
    }

    public void setMsgReceive(String msgReceive) {
        this.msgReceive = msgReceive;
    }
}
