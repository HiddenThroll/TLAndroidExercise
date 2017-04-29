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

/**
 * 继承SurfaceView的Clock，可跟随时间变化
 * Created by 龙 on 2017/4/27.
 */

public class CustomSurfaceClock extends SurfaceView implements SurfaceHolder.Callback{

    private SurfaceHolder surfaceHolder;
    private MyThread myThread;

    private float mRadius;
    private float mDialScale;//表盘刻度长度, 长, 短的为长刻度1/2
    private float mInteval;// 刻度与文字间隔
    private float mTextSizeBig;
    private float mTextSizeNormal;
    private float mMinuteHand;// 分针长度

    private final int SLEEP_TIME = 100;

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
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        myThread = new MyThread(surfaceHolder, false);

        mRadius = DisplayUtil.dip2px(getContext(), 80f);
        mDialScale = DisplayUtil.dip2px(getContext(), 8f);
        mInteval = DisplayUtil.dip2px(getContext(), 12f);
        mTextSizeBig = DisplayUtil.sp2px(getContext(), 12f);
        mTextSizeNormal = DisplayUtil.sp2px(getContext(), 8f);
        mMinuteHand = DisplayUtil.dip2px(getContext(), 48f);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        myThread.setRun(true);
        myThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        myThread.setRun(false);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private class MyThread extends Thread {
        private SurfaceHolder surfaceHolder;
        private boolean isRun;

        private Canvas canvas;
        private Paint paint;

        public MyThread(SurfaceHolder surfaceHolder, boolean isRun) {
            this.surfaceHolder = surfaceHolder;
            this.isRun = isRun;

        }

        public boolean isRun() {
            return isRun;
        }

        public void setRun(boolean run) {
            isRun = run;
        }

        @Override
        public void run() {
            Logger.e("rung");
            while (isRun) {
                try {
                    canvas = surfaceHolder.lockCanvas();
                    paint = new Paint();
                    canvas.drawColor(Color.WHITE);
                    drawDial(canvas, paint);

//                    Thread.sleep(SLEEP_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }

    /**
     * 绘制表盘(圆)
     */
    private void drawDial(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(DisplayUtil.dip2px(getContext(), 1f));
        paint.setColor(ContextCompat.getColor(getContext(), R.color.color_text_black));

        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, mRadius, paint);
    }
}
