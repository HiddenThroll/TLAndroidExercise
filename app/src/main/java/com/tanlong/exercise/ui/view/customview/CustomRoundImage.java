package com.tanlong.exercise.ui.view.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.tanlong.exercise.R;
import com.tanlong.exercise.util.DisplayUtil;

/**
 * 使用PorterDuffXfermode实现圆角图片, 主要目的为练习PorterDuffXfermode
 * Created by Administrator on 2017/3/26.
 */

public class CustomRoundImage extends View {

    private int mImageRes = R.mipmap.ic_guide_1;
    private Bitmap mBitmap;// 绘制的图片
    private Bitmap mOutMask;// 遮罩层

    private Paint mPaint;
//    private Canvas mNewCanvas;
    private RectF mRectF;//圆角矩形, 作为遮罩层
    private int round;//圆角大小
    private PorterDuffXfermode xfermode;

    public CustomRoundImage(Context context) {
        super(context);
        init();
    }

    public CustomRoundImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomRoundImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBitmap = BitmapFactory.decodeResource(getResources(), mImageRes);
        mOutMask = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);

//        mNewCanvas = new Canvas(mOutMask);
        mPaint = new Paint();
        round = DisplayUtil.dip2px(getContext(), 16);
        mRectF = new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(255, 139, 197, 186);//绘制背景

        int layerId = canvas.saveLayer(0, 0, mRectF.right, mRectF.bottom, mPaint, Canvas.ALL_SAVE_FLAG);
            mPaint.setAntiAlias(true);
            canvas.drawRoundRect(mRectF, round, round, mPaint);//先画的是DST,作为遮罩层
            mPaint.setXfermode(xfermode);//设置模式
            canvas.drawBitmap(mBitmap, 0, 0, mPaint);//后画的是SRC, 作为 源
        canvas.restoreToCount(layerId);
    }
}
