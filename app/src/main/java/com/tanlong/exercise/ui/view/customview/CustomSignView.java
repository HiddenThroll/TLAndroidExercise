package com.tanlong.exercise.ui.view.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tanlong.exercise.util.DisplayUtil;

/**
 * 手写签名
 * Created by 龙 on 2017/4/24.
 */

public class CustomSignView extends View {

    private Paint mPaint;
    private Bitmap mBitmap;//缓存Bitmap
    private Canvas mCanvas;
    private Path mPath;
    private float startX;
    private float startY;

    private final int PIC_WIDTH = 400;
    private final int PIC_HEIGHT = 800;

    private final int PANIT_WIDTH = 32;

    public CustomSignView(Context context) {
        super(context);
        init();
    }

    public CustomSignView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomSignView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        int width = DisplayUtil.getDisplay(getContext()).x;
        int height = DisplayUtil.getDisplay(getContext()).y;
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(PANIT_WIDTH);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(Color.BLACK);

        mPath = new Path();

        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(Color.GRAY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, null);
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

        startX = x;
        startY = y;
    }

    private void canvasMove(float x, float y) {
        float endX = (startX + x) / 2;
        float endY = (startY + y) / 2;

        mPath.quadTo(startX, startY, endX, endY);
        mCanvas.drawPath(mPath, mPaint);
        //更新起始点
        startX = x;
        startY = y;
    }

    private void canvasEnd() {

    }
}
