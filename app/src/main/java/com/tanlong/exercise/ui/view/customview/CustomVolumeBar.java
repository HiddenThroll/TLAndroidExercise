package com.tanlong.exercise.ui.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.tanlong.exercise.R;


/**
 * 音频控制Bar,有一定实用价值
 * Created by Administrator on 2016/4/25.
 */
public class CustomVolumeBar extends View {

    private final String TAG = "CustomVolumeBar";

    private int mFirColor;// 块背景色
    private int mSecColor;// 块当前色
    private float mCircleWidth;// 圆环宽度
    private int mTotalBlock;// 总块数
    private int mCurrentBlock;// 当前块数
    private Bitmap mBitmap;// 中间图片
    private int mSplitSize; // 块与块之间距离（角度）

    private Paint mPaint;
    private RectF mCircleRect;// 圆环外接矩形
    private RectF mImageRect; // 中间图片外接矩形

    public CustomVolumeBar(Context context) {
        this(context, null);
    }

    public CustomVolumeBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomVolumeBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomVolumeBar,
                defStyleAttr, 0);
        for (int i = 0, size = array.length(); i < size; i++) {
            int index = array.getIndex(i);

            switch (index) {
                case R.styleable.CustomVolumeBar_custom_volume_bar_fir_color:
                    mFirColor = array.getColor(index, Color.GRAY);
                    break;
                case R.styleable.CustomVolumeBar_custom_volume_bar_sec_color:
                    mSecColor = array.getColor(index, Color.BLACK);
                    break;
                case R.styleable.CustomVolumeBar_custom_volume_bar_bg:
                    mBitmap = BitmapFactory.decodeResource(getResources(), array.getResourceId(index, 0));
                    break;
                case R.styleable.CustomVolumeBar_custom_volume_bar_circle_width:
                    mCircleWidth = array.getDimension(index, 20);
                    break;
                case R.styleable.CustomVolumeBar_custom_volume_bar_count:
                    mTotalBlock = array.getInteger(index, 12);
                    break;
                case R.styleable.CustomVolumeBar_custom_volume_bar_split_size:
                    mSplitSize = array.getInteger(index, 20);
                    break;
                default:
                    break;
            }
        }

        array.recycle();
        mPaint = new Paint();
        mCurrentBlock = mTotalBlock / 3;
        mImageRect = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStrokeWidth(mCircleWidth); // 设置圆环的宽度
        mPaint.setStrokeCap(Paint.Cap.ROUND); // 定义线段断点形状为圆头
        mPaint.setStyle(Paint.Style.STROKE); // 设置空心
        int center = getWidth() / 2; // 获取圆心的x坐标
        int radius = (int) (center - mCircleWidth / 2);// 半径

        // 画块
        drawOval(canvas, center, radius);
        // 画中间图片
        int innerRadius = (int) (center - mCircleWidth);// 内接半径
        mImageRect.left = (float) (center - Math.sqrt(2)/2 * innerRadius);
        mImageRect.top =  (float) (center - Math.sqrt(2)/2 * innerRadius);
        mImageRect.right = (float) (center + Math.sqrt(2)/2 * innerRadius);
        mImageRect.bottom = (float) (center + Math.sqrt(2)/2 * innerRadius);

        /**
         * 如果图片宽度小于内切矩形宽度，那么根据图片的尺寸放置到正中心
         */
        if (mBitmap.getWidth() < Math.sqrt(2) * innerRadius) {
            mImageRect.left = center - mBitmap.getWidth() / 2;
            mImageRect.right = center + mBitmap.getWidth() / 2;
            mImageRect.top = center - mBitmap.getHeight() / 2;
            mImageRect.bottom = center + mBitmap.getHeight() / 2;
        }
        canvas.drawBitmap(mBitmap, null, mImageRect, mPaint);
    }

    /**
     * 画块(其实是有宽度的弧形)
     * @param canvas -- 画布
     * @param centre -- 中心
     * @param radius -- 半径
     */
    private void drawOval(Canvas canvas, int centre, int radius) {

        /**
         * 根据需要画的块个数以及间隙计算每个块块所占的比例*360
         */
        float itemSize = (360 * 1.0f - mTotalBlock * mSplitSize) / mTotalBlock;

        if (mCircleRect == null) {
            // 用于定义圆弧的形状和大小的界限
            mCircleRect = new RectF(centre - radius, centre - radius, centre + radius, centre + radius);
        }
        // 画背景块
        mPaint.setColor(mFirColor); // 设置圆环的颜色
        for (int i = 0; i < mTotalBlock; i++) {
            canvas.drawArc(mCircleRect, i * (itemSize + mSplitSize), itemSize, false, mPaint); // 根据进度画圆弧
        }
        // 画填充块
        mPaint.setColor(mSecColor); // 设置圆环的颜色
        for (int i = 0; i < mCurrentBlock; i++) {
            canvas.drawArc(mCircleRect, i * (itemSize + mSplitSize), itemSize, false, mPaint); // 根据进度画圆弧
        }
    }

    /**
     * 添加音频块
     */
    public void addBlock() {
        if (mCurrentBlock < mTotalBlock) {
            mCurrentBlock++;
            postInvalidate();
        }
    }

    /**
     * 减少音频块
     */
    public void reduceBlock() {
        if (mCurrentBlock > 0) {
            mCurrentBlock--;
            postInvalidate();
        }
    }
}
