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
    /**
     * 缓存Bitmap
     */
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private float startX;
    private float startY;
    private final int BACKGROUND_COLOR = Color.GRAY;

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
        //设置抗锯齿
        mPaint.setAntiAlias(true);
        //让Path的连接变得圆滑
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //画笔颜色
        mPaint.setColor(Color.BLACK);

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
            default:
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
        // 重置路径
        mPath.reset();
        //清空已绘制内容
        mCanvas.drawColor(BACKGROUND_COLOR);
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
