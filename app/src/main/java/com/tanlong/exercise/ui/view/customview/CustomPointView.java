package com.tanlong.exercise.ui.view.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.tanlong.exercise.R;
import com.tanlong.exercise.model.entity.PointItem;
import com.tanlong.exercise.util.DisplayUtil;

/**
 * Created by 龙 on 2017/5/10.
 */

public class CustomPointView extends View {

    private final float CIRCLE_RADIUS = 50;

    private Paint mPaint;
    private int mFillColor;

    private PointItem pointItem;

    public CustomPointView(Context context) {
        super(context);
        init();
    }

    public CustomPointView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomPointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mFillColor = ContextCompat.getColor(getContext(), R.color.color_282c76);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mFillColor);
        mPaint.setAntiAlias(true);

        pointItem = new PointItem(DisplayUtil.getDisplay(getContext()).x / 2,
                CIRCLE_RADIUS);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(pointItem.getX(), pointItem.getY(), CIRCLE_RADIUS, mPaint);
    }

    public PointItem getPointItem() {
        return pointItem;
    }

    public void setPointItem(PointItem pointItem) {
        this.pointItem = pointItem;
        invalidate();
    }

    public float getCIRCLE_RADIUS() {
        return CIRCLE_RADIUS;
    }
}
