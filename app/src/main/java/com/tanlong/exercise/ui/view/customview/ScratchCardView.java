package com.tanlong.exercise.ui.view.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tanlong.exercise.R;

/**
 * 刮刮卡
 * Created by Administrator on 2017/4/16.
 */

public class ScratchCardView extends View {

    private Bitmap mBgBitmap;//背景图片
    private Bitmap mFgBitmap;//遮挡图片
    private Canvas mCanvas;
    private Paint mPaint;
    private Path mPath;

    private float startX;
    private float startY;

    public ScratchCardView(Context context) {
        super(context);
        init();
    }

    public ScratchCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScratchCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAlpha(0);// 设置画笔为透明
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(50);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);

        mPath = new Path();

        mBgBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_guide_1);
        mFgBitmap = Bitmap.createBitmap(mBgBitmap.getWidth(), mBgBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        mCanvas = new Canvas(mFgBitmap);
        mCanvas.drawColor(Color.GRAY);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                canvasStart(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                canvasMove(x, y);
                break;
            case MotionEvent.ACTION_UP:
                canvasEnd();
                break;
        }

        invalidate();
        return true;
    }

    private void canvasStart(float x, float y) {
        mPath.moveTo(x, y);
        // 记录起始点
        startX = x;
        startY = y;
    }

    private void canvasMove(float x, float y) {
        float endX = (x + startX) / 2;
        float endY = (y + startY) / 2;

        mPath.quadTo(startX, startY, endX, endY);//获得一条二阶贝塞尔曲线
        mCanvas.drawPath(mPath, mPaint);//绘制曲线

        //更新起始点
        startX = x;
        startY = y;
    }

    private void canvasEnd() {
        mPath.lineTo(startX, startY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBgBitmap, 0, 0, null);//先画的是DST
        canvas.drawBitmap(mFgBitmap, 0,0,null);//后画的是SRC
    }
}
