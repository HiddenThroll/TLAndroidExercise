package com.tanlong.exercise.ui.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.tanlong.exercise.R;
import com.tanlong.exercise.model.entity.OfficialCarSchedule;
import com.tanlong.exercise.util.DateUtil;
import com.tanlong.exercise.util.DisplayUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 时间分配表
 * Created by 龙 on 2016/9/5.
 */
public class ScheduleChart extends View {

    private Paint mPaint;
    private float mLineHeight = 8;// 时间块高度，8dp
    private float mLineWidth = 300;// 时间轴长度, 300dp
    private int mScheduleTimeColor;// 已分配时间
    private int mUseTimeColor;  // 当前选择时间
    private int mConflictTimeColor;// 冲突时间
    private int mTotalTime;

    private final int TOTAL_TIME = 60 * 60 * 48;//默认总时间为48小时

    /** 已有用车时间*/
    private List<OfficialCarSchedule> mScheduleTime = new ArrayList<>();
    /** 当前选择用车时间，只有一个元素*/
    private List<OfficialCarSchedule> mCurUseTime = new ArrayList<>();
    /** 冲突时间，可能有多个元素*/
    private List<OfficialCarSchedule> mConflictTime = new ArrayList<>();

    public ScheduleChart(Context context) {
        this(context, null);
    }

    public ScheduleChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScheduleChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.ScheduleChart, defStyleAttr, 0);

        for (int i = 0, size = typedArray.length(); i < size; i++) {
            int index = typedArray.getIndex(i);

            switch (index) {
                case R.styleable.ScheduleChart_color_schedule_time:
                    mScheduleTimeColor = typedArray.getColor(index, Color.GREEN);
                    break;
                case R.styleable.ScheduleChart_color_use_time:
                    mUseTimeColor = typedArray.getColor(index, Color.BLUE);
                    break;
                case R.styleable.ScheduleChart_color_conflict_time:
                    mConflictTimeColor = typedArray.getColor(index, Color.RED);
                    break;
                case R.styleable.ScheduleChart_total_time_in_seconds:
                    mTotalTime = typedArray.getInteger(index, TOTAL_TIME);
                    break;
                case R.styleable.ScheduleChart_line_width:
                    mLineWidth = typedArray.getDimension(index, DisplayUtil.dip2px(context, 8));
                    break;
                case R.styleable.ScheduleChart_line_height:
                    mLineHeight = typedArray.getDimension(index, DisplayUtil.dip2px(context, 300));
                    break;
                default:
                    break;
            }
        }

        typedArray.recycle();

        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制已分配时间
        drawTime(canvas, mScheduleTime, mScheduleTimeColor);
        // 绘制当前选择时间
        drawTime(canvas, mCurUseTime, mUseTimeColor);
        // 绘制冲突时间
        calConflictTime();
        if (mConflictTime.size() > 0) {
            drawTime(canvas, mConflictTime, mConflictTimeColor);
            drawText(canvas, mConflictTime, R.color.text_color_info, 10);
        }
    }

    private void drawTime(Canvas canvas, List<OfficialCarSchedule> list, int color) {
        // 今天开始时间，秒为单位
        long todayTime = 0;
        try {
            todayTime = DateUtil.getTodayBegin(DateUtil.getNowDate()) / 1000;
        } catch (ParseException e) {
            todayTime = System.currentTimeMillis();
            e.printStackTrace();
        }
        for (OfficialCarSchedule schedule : list) {
            float left = getTimeWidth(schedule.getStartTime() / 1000 - todayTime);
            float right = getTimeWidth(schedule.getEndTime() / 1000 - todayTime);
            float top = (float) (getHeight() * 0.5 - DisplayUtil.dip2px(getContext(), mLineHeight) * 0.5);
            float bottom = (float) (getHeight() * 0.5 + DisplayUtil.dip2px(getContext(), mLineHeight) * 0.5);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(color);
            canvas.drawRect(left, top, right, bottom, mPaint);
        }
    }

    private void drawText(Canvas canvas, List<OfficialCarSchedule> list, int color, int textSize) {
        // 今天开始时间，秒为单位
        long todayTime = 0;
        try {
            todayTime = DateUtil.getTodayBegin(DateUtil.getNowDate()) / 1000;
        } catch (ParseException e) {
            todayTime = System.currentTimeMillis();
            e.printStackTrace();
        }

        for (OfficialCarSchedule schedule : list) {
            float time1 = getTimeWidth(schedule.getStartTime() / 1000 - todayTime);
            String startTime = DateUtil.getDateTime(new Date(schedule.getStartTime()), DateUtil.HOUR_MINUTE);
            float time2 = getTimeWidth(schedule.getEndTime() / 1000 - todayTime);
            String endTime = DateUtil.getDateTime(new Date(schedule.getEndTime()), DateUtil.HOUR_MINUTE);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(ContextCompat.getColor(getContext(), color));
            mPaint.setTextSize(DisplayUtil.sp2px(getContext(), textSize));
            int heightRevise = 2;// 垂直修正距离
            Rect textRect = new Rect();
            mPaint.getTextBounds(startTime, 0, startTime.length(), textRect);
            int widthRevise = textRect.width() / 2;// 水平修正距离
            // 画冲突开始时间, 位于时间轴下面
            canvas.drawText(startTime, time1 - widthRevise, getHeight(), mPaint);
            // 画冲突结束时间, 位于时间轴上面
            canvas.drawText(endTime, time2 - widthRevise,
                    (float) (getHeight() * 0.5 - DisplayUtil.dip2px(getContext(), mLineHeight) * 0.5)
                            - heightRevise,
                    mPaint);
        }
    }



    /**
     * 获得传入时间对应的长度（px）
     * @param time -- 传入时间，单位为秒
     * @return 对应的长度，单位为px
     */
    private int getTimeWidth(long time) {
        float width = (mLineWidth * time / TOTAL_TIME);
        return DisplayUtil.dip2px(getContext(), width);
    }

    /**
     * 计算冲突时间
     */
    private void calConflictTime() {
        if (mScheduleTime == null || mScheduleTime.size() == 0) {
            return;
        }

        if (mCurUseTime == null || mCurUseTime.size() == 0) {
            return;
        }
        OfficialCarSchedule useSchedule = mCurUseTime.get(0);

        mConflictTime.clear();// 清空已有时间

        for (OfficialCarSchedule schedule : mScheduleTime) {
            // 用车时间完全处于已分配时间
            if (useSchedule.getStartTime() >= schedule.getStartTime() &&
                    useSchedule.getEndTime() <= schedule.getEndTime()) {
                mConflictTime.add(useSchedule);
            }
            // 用车时间偏结束部分处于已分配时间
            if (useSchedule.getStartTime() < schedule.getStartTime() &&
                    useSchedule.getEndTime() > schedule.getStartTime() &&
                    useSchedule.getEndTime() <= schedule.getEndTime()) {
                mConflictTime.add(new OfficialCarSchedule(schedule.getStartTime(),
                        useSchedule.getEndTime()));
            }
            // 用车时间偏开始部分处于已分配时间
            if (useSchedule.getEndTime() > schedule.getEndTime() &&
                    useSchedule.getStartTime() >= schedule.getStartTime() &&
                    useSchedule.getStartTime() < schedule.getEndTime()) {
                mConflictTime.add(new OfficialCarSchedule(useSchedule.getStartTime(),
                        schedule.getEndTime()));
            }
            // 用车时间完全包含已分配时间
            if (useSchedule.getStartTime() < schedule.getStartTime() &&
                    useSchedule.getEndTime() > schedule.getEndTime()) {
                mConflictTime.add(schedule);
            }
        }
    }

    public List<OfficialCarSchedule> getmScheduleTime() {
        return mScheduleTime;
    }

    public void setmScheduleTime(List<OfficialCarSchedule> mScheduleTime) {
        this.mScheduleTime = mScheduleTime;
    }

    public List<OfficialCarSchedule> getmCurUseTime() {
        return mCurUseTime;
    }

    public void setmCurUseTime(List<OfficialCarSchedule> mCurUseTime) {
        this.mCurUseTime = mCurUseTime;
    }

    public List<OfficialCarSchedule> getmConflictTime() {
        return mConflictTime;
    }

    public void setmConflictTime(List<OfficialCarSchedule> mConflictTime) {
        this.mConflictTime = mConflictTime;
    }
}
