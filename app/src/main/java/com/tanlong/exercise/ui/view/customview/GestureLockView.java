package com.tanlong.exercise.ui.view.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.view.View;

/**
 * 手势锁中的单个锁View
 * Created by 龙 on 2016/8/5.
 */
public class GestureLockView extends View {
    private static final String TAG = "GestureLockView";

    /**
     * GestureLockView的三种状态
     */
    public enum Mode {
        STATUS_NO_FINGER,// 没有手指触碰
        STATUS_FINGER_ON,// 手指触碰
        STATUS_FINGER_UP // 手指触碰后抬起
    }

    /**
     * GestureLockView的当前状态
     */
    private Mode mCurrentStatus = Mode.STATUS_NO_FINGER;

    /**
     * 宽度
     */
    private int mWidth;
    /**
     * 高度
     */
    private int mHeight;
    /**
     * 外圆半径
     */
    private int mRadius;
    /**
     * 画笔的宽度
     */
    private int mStrokeWidth = 2;

    /**
     * 圆心坐标
     */
    private int mCenterX;
    private int mCenterY;
    private Paint mPaint;

    /**
     * 箭头（小三角）最长边与mWidth的比值（小三角最长边的一半长度 = mArrowRate * mWidth / 2 ）
     */
    private float mArrowRate = 0.333f;
    /**
     * 箭头角度
     */
    private int mArrowDegree = -1;
    /**
     * 箭头绘制路径
     */
    private Path mArrowPath;
    /**
     * 内圆的半径与外圆半径的比值，内圆的半径 = mInnerCircleRadiusRate * mRadius
     */
    private float mInnerCircleRadiusRate = 0.3F;

    /**
     * 四个颜色，可由用户自定义，初始化时由GestureLockViewGroup传入
     */
    private int mColorNoFingerInner;
    private int mColorNoFingerOuter;
    private int mColorFingerOn;
    private int mColorFingerUp;

    public GestureLockView(Context context, int colorNoFingerInner, int colorNoFingerOutter, int colorFingerOn, int colorFingerUp) {
        super(context);
        this.mColorNoFingerInner = colorNoFingerInner;
        this.mColorNoFingerOuter = colorNoFingerOutter;
        this.mColorFingerOn = colorFingerOn;
        this.mColorFingerUp = colorFingerUp;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 抗锯齿
        mArrowPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 取长和宽中的小值
        mWidth = mWidth < mHeight ? mWidth : mHeight;
        mRadius = mCenterX = mCenterY = mWidth / 2;
        mRadius -= mStrokeWidth / 2;// 外圆半径应减去画笔宽度的一半

        // 绘制三角形，初始时是个默认箭头朝上的一个等腰三角形，用户绘制结束后，根据由两个GestureLockView决定需要旋转多少度
        float mArrowLength = mWidth / 2 * mArrowRate;
        mArrowPath.moveTo(mWidth / 2, mStrokeWidth + 2);
        mArrowPath.lineTo(mWidth / 2 - mArrowLength, mStrokeWidth + 2
                + mArrowLength);
        mArrowPath.lineTo(mWidth / 2 + mArrowLength, mStrokeWidth + 2
                + mArrowLength);
        mArrowPath.close();// 关闭轮廓，如果当前点不等于起点，会自动添加一条当前点到起点的直线
        mArrowPath.setFillType(Path.FillType.WINDING);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (mCurrentStatus) {
            case STATUS_FINGER_ON:// 手指触碰
                // 绘制外圆
                mPaint.setStyle(Style.STROKE);
                mPaint.setColor(mColorFingerOn);
                mPaint.setStrokeWidth(2);
                canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
                // 绘制内圆
                mPaint.setStyle(Style.FILL);
                canvas.drawCircle(mCenterX, mCenterY, mRadius
                        * mInnerCircleRadiusRate, mPaint);
                break;
            case STATUS_FINGER_UP:// 手指触碰后抬起
                // 绘制外圆
                mPaint.setColor(mColorFingerUp);
                mPaint.setStyle(Style.STROKE);
                mPaint.setStrokeWidth(2);
                canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
                // 绘制内圆
                mPaint.setStyle(Style.FILL);
                canvas.drawCircle(mCenterX, mCenterY, mRadius
                        * mInnerCircleRadiusRate, mPaint);
                // 绘制箭头
                drawArrow(canvas);

                break;

            case STATUS_NO_FINGER:// 没有手指触碰
                // 绘制外圆
                mPaint.setStyle(Style.FILL);
                mPaint.setColor(mColorNoFingerOuter);
                canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
                // 绘制内圆
                mPaint.setColor(mColorNoFingerInner);
                canvas.drawCircle(mCenterX, mCenterY, mRadius
                        * mInnerCircleRadiusRate, mPaint);
                break;

        }

    }

    /**
     * 绘制箭头
     *
     * @param canvas
     */
    private void drawArrow(Canvas canvas) {
        if (mArrowDegree != -1) {
            mPaint.setStyle(Paint.Style.FILL);// 设置画笔为填充模式

            canvas.save();// 先存储画布当前绘制内容
            canvas.rotate(mArrowDegree, mCenterX, mCenterY);// 以圆心为中心，旋转画布
            canvas.drawPath(mArrowPath, mPaint);// 绘制箭头

            canvas.restore();// 恢复画布内容
        }

    }

    /**
     * 设置当前模式并重绘界面
     *
     * @param mode
     */
    public void setMode(Mode mode) {
        this.mCurrentStatus = mode;
        invalidate();
    }

    public void setArrowDegree(int degree) {
        this.mArrowDegree = degree;
    }

    public int getArrowDegree() {
        return this.mArrowDegree;
    }
}
