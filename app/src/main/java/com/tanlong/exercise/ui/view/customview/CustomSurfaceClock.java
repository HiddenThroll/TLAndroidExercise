package com.tanlong.exercise.ui.view.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.orhanobut.logger.Logger;
import com.tanlong.exercise.R;
import com.tanlong.exercise.util.DisplayUtil;

import java.util.Calendar;


/**
 * 继承SurfaceView的Clock，可跟随时间变化
 * Created by 龙 on 2017/4/27.
 */

public class CustomSurfaceClock extends SurfaceView implements SurfaceHolder.Callback, Runnable{
    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private boolean isDrawing;
    private Paint mPaint;

    private float mRadius;
    private float mDialScale;//表盘刻度长度, 长, 短的为长刻度1/2
    private float mInteval;// 刻度与文字间隔
    private float mTextSizeBig;//大写字号
    private float mTextSizeNormal;//小写字号
    private float mMinuteHand;// 分针长度

    public CustomSurfaceClock(Context context) {
        super(context);
        init();
    }

    public CustomSurfaceClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomSurfaceClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        isDrawing = false;

        mPaint = new Paint();
        mRadius = DisplayUtil.dip2px(getContext(), 80f);
        mDialScale = DisplayUtil.dip2px(getContext(), 8f);
        mInteval = DisplayUtil.dip2px(getContext(), 12f);
        mTextSizeBig = DisplayUtil.sp2px(getContext(), 12f);
        mTextSizeNormal = DisplayUtil.sp2px(getContext(), 8f);
        mMinuteHand = DisplayUtil.dip2px(getContext(), 48f);

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isDrawing = true;
        Logger.e("surfaceCreated");
        new Thread(this).start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawing = false;
        Logger.e("surfaceDestroyed");
    }

    @Override
    public void run() {
        while (isDrawing) {
            startDraw();
        }
    }

    private void startDraw() {
        try {
            mCanvas = mHolder.lockCanvas();
            mCanvas.drawColor(Color.WHITE);

            drawDial(mCanvas);
            drawDialScale(mCanvas);
            drawDialText(mCanvas);

            int hour = Calendar.getInstance().get(Calendar.HOUR);
            int minute = Calendar.getInstance().get(Calendar.MINUTE);
            int second = Calendar.getInstance().get(Calendar.SECOND);


            drawSecond(mCanvas, second);
            drawMinute(mCanvas, minute);
            drawHour(mCanvas, hour, minute);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
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

    private void drawSecond(Canvas canvas, int second) {
        canvas.save();//保存画布
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(DisplayUtil.dip2px(getContext(), 1f));//秒针比较细
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_text_black));

        int degree = 360 / 60 * (second) + 180;

        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);//移动画布
        canvas.rotate(degree, 0, 0);//旋转画布
        canvas.drawLine(0, 0, 0, mMinuteHand, mPaint);
        canvas.rotate(-degree, 0, 0);//恢复画布
        canvas.translate(-getMeasuredWidth() / 2, -getMeasuredHeight() / 2);//
        canvas.restore();//恢复
    }

    private void drawMinute(Canvas canvas, int minute) {
        canvas.save();//保存画布
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(DisplayUtil.dip2px(getContext(), 2f));//分针粗一点
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_text_black));

        int degree = 360 / 60 * (minute) + 180;

        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);//移动画布
        canvas.rotate(degree, 0, 0);//旋转画布
        canvas.drawLine(0, 0, 0, mMinuteHand, mPaint);
        canvas.rotate(-degree, 0, 0);//恢复画布
        canvas.translate(-getMeasuredWidth() / 2, -getMeasuredHeight() / 2);//
        canvas.restore();//恢复画布
    }

    int oldHour;

    private void drawHour(Canvas canvas, int hour, int minute) {
        canvas.save();//保存画布
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(DisplayUtil.dip2px(getContext(), 3f));//时针再粗一点
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_text_black));

        int degree = 360 / 12 * (hour) + 180;//返回的hour是 0 - 11

        if (oldHour != hour) {
            oldHour = hour;
            Logger.e("oldHour is %d, degree is %d", oldHour, degree );
        }

        // 补偿分针的角度
        degree = degree + 360 / 12 * minute / 60 ;

        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);//移动画布
        canvas.rotate(degree, 0, 0);//旋转画布
        canvas.drawLine(0, 0, 0, mMinuteHand / 2, mPaint);//时针短一半
        canvas.rotate(-degree, 0, 0);//恢复画布
        canvas.translate(-getMeasuredWidth() / 2, -getMeasuredHeight() / 2);//
        canvas.restore();//恢复画布
    }
}
