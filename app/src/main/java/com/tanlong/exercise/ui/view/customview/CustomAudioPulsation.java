package com.tanlong.exercise.ui.view.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.view.customviewgroup.timepopupwindow.DensityUtil;

import java.util.Random;

public class CustomAudioPulsation extends View {

    private Paint mPaint = new Paint();
    /**
     * 每个小方块宽度
     */
    private float rectWidth;
    /**
     * 控制变量,决定每个方块高度
     */
    private float rectHeight = 8;
    private int pro;
    private Random random = new Random();
    /**
     * 每个方块颜色
     */
    private int color;
    /**
     * 画多少个
     */
    private int rectCount = 3;

    public CustomAudioPulsation(Context context) {
        super(context);
    }

    public CustomAudioPulsation(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomAudioPulsation(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e("test", "onDraw");
        color = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(color);
        rectWidth = DensityUtil.dip2px(getContext(), 8);
        rectHeight = random.nextInt(16) + 8;

        for (int i = 0; i < rectCount; i++) {
            float left = i * rectWidth;
            float height = 0;
            if (i % 3 == 0) {
                if (random.nextBoolean()) {
                    height = DensityUtil.dip2px(getContext(), rectHeight);
                } else {
                    height = DensityUtil.dip2px(getContext(), rectHeight) * 0.5f;
                }
            } else if (i % 3 == 1) {
                if (random.nextBoolean()) {
                    height = DensityUtil.dip2px(getContext(), rectHeight) * 0.6f;
                } else {
                    height = DensityUtil.dip2px(getContext(), rectHeight);
                }
            } else {
                if (random.nextBoolean()) {
                    height = DensityUtil.dip2px(getContext(), rectHeight) * 1.4f;
                } else {
                    height = DensityUtil.dip2px(getContext(), rectHeight);
                }
            }
            float top = getHeight() - height;
            float right = (i + 1) * rectWidth;
            float bottom = getHeight();
            canvas.drawRect(left, top, right, bottom, mPaint);
        }
    }

    public int getPro() {
        return pro;
    }

    public void setPro(int pro) {
        this.pro = pro;
        invalidate();
    }
}
