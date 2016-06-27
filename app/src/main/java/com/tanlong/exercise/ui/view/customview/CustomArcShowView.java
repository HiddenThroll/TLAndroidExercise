package com.tanlong.exercise.ui.view.customview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.tanlong.exercise.R;
import com.tanlong.exercise.util.DisplayUtil;
import com.tanlong.exercise.util.LogTool;
import com.tanlong.exercise.util.NumberUtil;

/**
 * 扇形百分比数据展示View, 有一定实用价值
 * Created by Administrator on 2016/5/20.
 */
public class CustomArcShowView extends View {

    private int mTotalCount; // 总数据
    private int currentCount;// 当前数据，要使用属性动画,属性前不要加m
    private float mCircleWidth;// 扇形宽度
    private int mArcColor;// 扇形颜色
    private int mTextColor;// 文字颜色
    private float mTextSize;// 文字大小

    private Paint mPaint;
    private RectF mCircleRect;// 圆环外接矩形
    private Rect mTextRect;// 文字矩形

    public CustomArcShowView(Context context) {
        this(context, null);
    }

    public CustomArcShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomArcShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CustomArcShow, defStyleAttr, 0);
        for (int i = 0, size = typedArray.length(); i < size; i++) {
            int index = typedArray.getIndex(i);

            switch (index) {
                case R.styleable.CustomArcShow_custom_arc_show_total_count:
                    mTotalCount = typedArray.getInteger(index, 100);
                    break;
                case R.styleable.CustomArcShow_custom_arc_show_current_count:
                    currentCount = typedArray.getInteger(index, 50);
                    break;
                case R.styleable.CustomArcShow_custom_arc_show_circle_width:
                    mCircleWidth = typedArray.getDimension(index, DisplayUtil.dip2px(context, 20));
                    break;
                case R.styleable.CustomArcShow_custom_arc_show_arc_color:
                    mArcColor = typedArray.getColor(index, Color.GREEN);
                    break;
                case R.styleable.CustomArcShow_custom_arc_show_text_color:
                    mTextColor = typedArray.getColor(index, Color.BLACK);
                    break;
                case R.styleable.CustomArcShow_custom_arc_show_text_size:
                    mTextSize = typedArray.getDimension(index, DisplayUtil.sp2px(context, 18));
            }
        }
        typedArray.recycle();

        mPaint = new Paint();
        mTextRect = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 画外围弧形
        int center = getWidth() / 2;
        int arcRadius = (int) (center - mCircleWidth / 2);
        if (mCircleRect == null) {
            mCircleRect = new RectF(center - arcRadius, center - arcRadius, center + arcRadius,
                    center + arcRadius);
        }
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mArcColor);

        int sweepAngle = currentCount * 360 / mTotalCount;// 计算圆弧扫过弧度
        canvas.drawArc(mCircleRect, -90, sweepAngle, false, mPaint);

        // 画中间圆形
        int circleRadius = (int) (center - (center - mCircleWidth) / 2);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(center, center, circleRadius, mPaint);

        // 画中间文字
//        Rect mTextRect = new Rect();
        String text = NumberUtil.keepTwoDecimal((currentCount / (float) mTotalCount) * 100) + "%";
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);
        // 先设置mPaint大小,再计算包裹text的最小矩形大小
        mPaint.getTextBounds(text, 0, text.length(), mTextRect);

        float startWidth = center - mTextRect.width() / 2;
        float startHeight = center + mTextRect.height() / 2;
        canvas.drawText(text, 0, text.length(), startWidth, startHeight, mPaint);
    }

    public void addCount() {
        int item = mTotalCount / 10;
        int oldCount = currentCount;
        if (currentCount + item < mTotalCount) {
            currentCount += item;
        } else {
            currentCount = mTotalCount;
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this, "currentCount", oldCount, currentCount);
        objectAnimator.start();
    }

    public void reduceCount() {
        int item = mTotalCount / 10;
        int oldCount = currentCount;
        if (currentCount < item) {
            currentCount = 0;
        } else {
            currentCount -= item;
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this, "currentCount", oldCount, currentCount);
        objectAnimator.start();
    }

    public int getmTotalCount() {
        return mTotalCount;
    }

    public void setmTotalCount(int mTotalCount) {
        this.mTotalCount = mTotalCount;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
        invalidate();
    }
}
