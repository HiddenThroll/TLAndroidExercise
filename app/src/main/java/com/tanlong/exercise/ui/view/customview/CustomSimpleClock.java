package com.tanlong.exercise.ui.view.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.tanlong.exercise.R;
import com.tanlong.exercise.util.DisplayUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/3/20.
 */

public class CustomSimpleClock extends View {

    private Paint mPaint;
    private float mRadius;
    /**
     * 表盘刻度长度, 长, 短的为长刻度1/2
     */
    private float mDialScale;
    /**
     * 刻度与文字间隔
     */
    private float mInteval;
    private float mTextSizeBig;
    private float mTextSizeNormal;
    /**
     * 分针长度
     */
    private float mMinuteHand;
    /**
     * 当前时间
     */
    private Date curDate;

    public CustomSimpleClock(Context context) {
        this(context, null);
    }

    public CustomSimpleClock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSimpleClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mRadius = DisplayUtil.dip2px(context, 80f);
        mDialScale = DisplayUtil.dip2px(context, 8f);
        mInteval = DisplayUtil.dip2px(context, 12f);
        mTextSizeBig = DisplayUtil.sp2px(context, 12f);
        mTextSizeNormal = DisplayUtil.sp2px(context, 8f);
        mMinuteHand = DisplayUtil.dip2px(context, 48f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawDial(canvas);
        drawDialScale(canvas);
        drawDialText(canvas);
        drawTime(canvas, curDate);
    }

    /**
     * 绘制表盘(圆)
     */
    private void drawDial(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(DisplayUtil.dip2px(getContext(), 1f));
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_text_black));

        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, mRadius, mPaint);
    }

    /**
     * 绘制表盘刻度
     *
     * @param canvas
     */
    private void drawDialScale(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_text_black));

        for (int i = 0; i < 12; i++) {
            if (i == 0 || i == 3 || i == 6 || i == 9) {
                mPaint.setStrokeWidth(DisplayUtil.dip2px(getContext(), 2f));
                canvas.drawLine(mRadius, 0, mRadius, mDialScale, mPaint);
            } else {
                mPaint.setStrokeWidth(DisplayUtil.dip2px(getContext(), 1f));
                canvas.drawLine(mRadius, 0, mRadius, mDialScale / 2, mPaint);
            }
            int degree = 360 / 12;
            canvas.rotate(degree, getMeasuredWidth() / 2, getMeasuredHeight() / 2);//旋转画布
        }
    }

    /**
     * 绘制表盘文字
     *
     * @param canvas
     */
    private void drawDialText(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_text_black));

        String text;
        float textX, textY;
        for (int i = 0; i < 12; i++) {

            if (i == 0 || i == 3 || i == 6 || i == 9) {
                if (i == 0) {
                    text = "12";
                } else {
                    text = String.valueOf(i);
                }
                mPaint.setTextSize(mTextSizeBig);
                textX = mRadius - mPaint.measureText(text) / 2;
                textY = mDialScale + mInteval;
                canvas.drawText(text, textX, textY, mPaint);
            } else {
                mPaint.setTextSize(mTextSizeNormal);
                text = String.valueOf(i);
                textX = mRadius - mPaint.measureText(text) / 2;
                textY = mDialScale / 2 + mInteval;
                canvas.drawText(text, textX, textY, mPaint);
            }
            int degree = 360 / 12;
            canvas.rotate(degree, getMeasuredWidth() / 2, getMeasuredHeight() / 2);//旋转画布
        }
    }

    private void drawTime(Canvas canvas, Date date) {
        //2.绘制时针
        drawHour(canvas, date);
        //3.绘制分针
        drawMinute(canvas, date);
        //4.绘制秒针
        drawSecond(canvas, date);
    }

    private void drawHour(Canvas canvas, Date date) {
        //初始化画笔
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_text_black));
        mPaint.setStrokeWidth(DisplayUtil.dip2px(getContext(), 4f));
        //获取时分
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR);
        if (hour == 0) {
            hour = 12;
        }
        int minute = calendar.get(Calendar.MINUTE);

        //计算旋转角度
        float baseHourRotate = (float) hour / 12 * 360;
        float minHourRotate = ((float) minute / 60) * (360 / 12);
        float hourRotate = baseHourRotate + minHourRotate;

        //开始绘制
        canvas.save();//保存画布
        //移动画布
        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        canvas.rotate(hourRotate);
        canvas.drawLine(0, 0, 0, -mMinuteHand / 2, mPaint);

        canvas.restore();
    }

    private void drawMinute(Canvas canvas, Date date) {
        //初始化画笔
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_text_black));
        mPaint.setStrokeWidth(DisplayUtil.dip2px(getContext(), 4f));
        //获取分
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int minute = calendar.get(Calendar.MINUTE);
        //计算旋转角度
        float minuteRotate = (float) minute / 60 * 360;
        //开始绘制
        canvas.save();//保存画布
        //移动画布
        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        canvas.rotate(minuteRotate);
        canvas.drawLine(0, 0, 0, -mMinuteHand, mPaint);

        canvas.restore();
    }

    private void drawSecond(Canvas canvas, Date date) {
        //初始化画笔
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_text_black));
        mPaint.setStrokeWidth(DisplayUtil.dip2px(getContext(), 2f));
        //获取秒
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int second = calendar.get(Calendar.SECOND);
        //计算旋转角度
        float secondRotate = (float) second / 60 * 360;
        //开始绘制
        canvas.save();//保存画布
        //移动画布
        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        canvas.rotate(secondRotate);
        canvas.drawLine(0, 0, 0, -mMinuteHand, mPaint);

        canvas.restore();
    }

    public Date getCurDate() {
        return curDate;
    }

    public void setCurDate(Date curDate) {
        this.curDate = curDate;
        postInvalidate();
    }
}
