package com.wxlz.woasislog.entity;

public class LogInfo {

    private long id;
    private long time;
    private long userid;
    private String name;
    private String value;
    private int type;
    private int level;

//    public LogInfo() {
//    }

    public LogInfo(long time, long userid, String name, String value, int type, int level) {
        this.time = time;
        this.userid = userid;
        this.name = name;
        this.value = value;
        this.type = type;
        this.level = level;
    }

    public LogInfo(long id, long time, long userid, String name, String value, int type, int level) {
        this.id = id;
        this.time = time;
        this.userid = userid;
        this.name = name;
        this.value = value;
        this.type = type;
        this.level = level;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
