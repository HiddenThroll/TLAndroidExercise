package com.tanlong.exercise.ui.view.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.tanlong.exercise.R;
import com.tanlong.exercise.util.DisplayUtil;

/**
 *
 * Created by Administrator on 2017/3/20.
 */

public class CustomSimpleClock extends View {

    private Paint mPaint;
    private float mRadius;
    private float mDialScale;//表盘刻度长度, 长, 短的为长刻度1/2
    private float mInteval;// 刻度与文字间隔
    private float mTextSizeBig;
    private float mTextSizeNormal;
    private float mMinuteHand;// 分针长度

    public CustomSimpleClock(Context context) {
        this(context, null);
    }

    public CustomSimpleClock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSimpleClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
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
        drawTime(canvas);
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
     * @param canvas
     */
    private void drawDialScale(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_text_black));

        for (int i = 0; i< 12; i++) {
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
     * @param canvas
     */
    private void drawDialText(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_text_black));

        String text;
        float textX, textY;
        for (int i = 0; i< 12; i++) {

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

    private void drawTime(Canvas canvas) {
        canvas.save();//保存画布
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(DisplayUtil.dip2px(getContext(), 4f));
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_text_black));

        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);//移动画布
        canvas.drawLine(0, 0, 0, -mMinuteHand, mPaint);
        canvas.drawLine(0, 0, -100, -mMinuteHand / 2, mPaint);
        canvas.restore();
    }
}
