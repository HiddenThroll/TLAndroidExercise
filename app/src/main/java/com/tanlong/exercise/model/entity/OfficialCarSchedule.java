package com.tanlong.exercise.model.entity;

import java.io.Serializable;

/**
 * 公务用车安排
 * Created by 龙 on 2016/8/12.
 */
public class OfficialCarSchedule implements Serializable {

    private long startTime;
    private long endTime;

    public OfficialCarSchedule() {
    }

    public OfficialCarSchedule(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
