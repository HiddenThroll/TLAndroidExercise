package com.tanlong.exercise.ui.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.tanlong.exercise.R;
import com.tanlong.exercise.util.DisplayUtil;
import com.tanlong.exercise.util.LogTool;

import java.util.concurrent.Executors;


/**
 * 自定义CircleView，模拟等待效果
 * Created by Administrator on 2016/4/22.
 */
public class CustomCircleView extends View {

    /**
     * 圆环宽度
     */
    private float mCircleWidth;
    /**
     * 绘制速度
     */
    private float mSpeed;
    private int mFirCircleColor;
    private int mSecCircleColor;

    private Paint mPaint;

    /**
     * 当前进度
     */
    private int mProgress;
    /**
     * 用于定义的圆弧的形状和大小的界限
     */
    private RectF mRectF;
    /**
     * 圆心
     */
    private int center;
    /**
     * 半径
     */
    private int radius;
    /**
     * 是否应该开始下一个
     */
    private boolean isNext = false;

    public CustomCircleView(Context context) {
        this(context, null);
    }

    public CustomCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //TODO 获取自定义属性
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomCircle,
                defStyleAttr, 0);
        for (int i = 0, size = array.length(); i < size; i++) {
            int index = array.getIndex(i);

            switch (index) {
                case R.styleable.CustomCircle_custom_circle_width:
                    mCircleWidth = array.getDimension(index, DisplayUtil.dip2px(context, 20));
                    break;
                case R.styleable.CustomCircle_custom_circle_speed:
                    mSpeed = array.getInt(index, 16);
                    break;
                case R.styleable.CustomCircle_custom_circle_fir_color:
                    mFirCircleColor = array.getColor(index, Color.BLACK);
                    break;
                case R.styleable.CustomCircle_custom_circle_sec_color:
                    mSecCircleColor = array.getColor(index, Color.RED);
                    break;
                default:
                    break;
            }
        }
        array.recycle();

        mPaint = new Paint();
        // 绘图线程
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    mProgress++;
                    if (mProgress == 360) {
                        mProgress = 0;
                        isNext = !isNext;
                    }
                    postInvalidate();//非UI线程, 使用postInvalidate更新View
                    try {
                        Thread.sleep((int) mSpeed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 设置圆环宽度
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        center = getWidth() / 2;
        radius = (int) (center - mCircleWidth / 2);
        if (mRectF == null) {
            mRectF = new RectF(center - radius, center - radius, center + radius, center + radius);
        }

        if (!isNext) {
            mPaint.setColor(mFirCircleColor);
            canvas.drawCircle(center, center, radius, mPaint);
            mPaint.setColor(mSecCircleColor);
            canvas.drawArc(mRectF, -90, mProgress, false, mPaint);
        } else {
            mPaint.setColor(mSecCircleColor);
            canvas.drawCircle(center, center, radius, mPaint);
            mPaint.setColor(mFirCircleColor);
            canvas.drawArc(mRectF, -90, mProgress, false, mPaint);
        }
        /**
         * drawArc函数参数解释
         * mRectF -- 绘制图形的外接矩形大小位置
         * startAngle -- 绘制起始角度，X正半轴为0度
         * sweepAngle -- 绘制扫过的角度
         * useCenter -- 是否绘制弧形的椭圆中心
         * paint -- 使用的画笔
         */
    }
}
