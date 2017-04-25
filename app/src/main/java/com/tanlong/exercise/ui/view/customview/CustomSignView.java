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
    private final int BACKGROUND_COLOR = Color.GRAY;

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
        mBitmap = getBitmap(width, height);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(PANIT_WIDTH);
        mPaint.setAntiAlias(true);//设置抗锯齿
        mPaint.setStrokeJoin(Paint.Join.ROUND);//让Path的连接变得圆滑
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(Color.BLACK);//画笔颜色

        mPath = new Path();

        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(BACKGROUND_COLOR);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
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

    private Bitmap getBitmap(int width, int height) {
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    }

    public void resetSign() {
        mPath.reset();// 重置路径
        mCanvas.drawColor(BACKGROUND_COLOR);//清空已绘制内容
        invalidate();
    }

    /**
     * 获得签名图片
     * @return
     */
    public Bitmap getSignBitmap() {
        return mBitmap;
    }
}
