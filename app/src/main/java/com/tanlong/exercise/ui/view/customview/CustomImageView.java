package com.tanlong.exercise.ui.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.tanlong.exercise.R;
import com.tanlong.exercise.util.DisplayUtil;

/**
 * 自定义ImageView, 练习使用
 * Created by Administrator on 2016/4/18.
 */
public class CustomImageView extends View {
    private final String TAG = "CustomImageView";
    private String mText;
    private int mTextColor;
    private int mTextSize;

    private Bitmap mImage;
    private int mImageScale;

    private Paint mPaint;
    private TextPaint mTextPaint;
    /**
     * 内容区
     */
    private Rect mRect;
    /**
     * 文本区
     */
    private Rect mTextRect;

    private int mWidth;
    private int mHeight;
    private Context mContext;

    private static final int IMAGE_SCALE_FITXY = 0;
    private static final int IMAGE_SCALE_CENTER = 1;

    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        // TODO 获得自定义属性
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomImageView,
                defStyleAttr, 0);
        for (int i = 0, size = array.length(); i < size; i++) {
            int attr = array.getIndex(i);

            switch (attr) {
                case R.styleable.CustomImageView_custom_image_title:
                    mText = array.getString(attr);
                    break;
                case R.styleable.CustomImageView_custom_image_color:
                    mTextColor = array.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomImageView_custom_image_size:
                    mTextSize = array.getDimensionPixelSize(attr, DisplayUtil.dip2px(context, 16f));
                    break;
                case R.styleable.CustomImageView_custom_image_src:
                    //对于Launcher图标,在8.0及以上系统,通过BitmapFactory.decodeResource方法获取
                    //应传入 R.drawable.launcher 而 不是R.mipmap.launcher
                    //R.mipmap.launcher会查找mipmap-anydpi-v26中的launcher(xml文件),而不是不同分辨率下的launcher图标
                    int src = array.getResourceId(attr, 0);
                    mImage = BitmapFactory.decodeResource(context.getResources(), src);
                    break;
                case R.styleable.CustomImageView_custom_image_scale:
                    mImageScale = array.getInt(attr, 0);
                    break;
                default:
                    break;
            }
        }
        array.recycle();

        mPaint = new Paint();
        mTextPaint = new TextPaint();
        mPaint.setTextSize(mTextSize);
        mRect = new Rect();
        mTextRect = new Rect();
        // 获取能包裹绘制文字的最小矩形
        mPaint.getTextBounds(mText, 0, mText.length(), mTextRect);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 设置宽度
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // 对应精确值(dp) 或者 match_parent
            mWidth = specSize;
        } else {
            // 宽度是文字与图片宽度中的最大值
            int desireText = getPaddingLeft() + getPaddingRight() + mTextRect.width();
            int desireImg = getPaddingLeft() + getPaddingRight() + mImage.getWidth();
            if (specMode == MeasureSpec.AT_MOST) {// wrap content
                int desire = Math.max(desireImg, desireText);
                mWidth = Math.min(desire, specSize);
            }
        }

        // 设置高度
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            mHeight = specSize;
        } else {
            // 高度是文字与图片高度之和
            int desire = getPaddingTop() + getPaddingBottom() + mTextRect.height() + mImage.getHeight();
            if (specMode == MeasureSpec.AT_MOST) {
                mHeight = Math.min(specSize, desire);
            }
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        // 画边框
        mPaint.setStrokeWidth(DisplayUtil.dip2px(mContext, 1f));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor("#41ccef"));
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        mRect.left = getPaddingLeft();
        mRect.right = mWidth - getPaddingRight();
        mRect.top = getPaddingTop();
        mRect.bottom = mHeight - getPaddingBottom();

        // 绘制文字
        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        /**
         * 当前设置的宽度小于字体需要的宽度，将字体改为xxx...
         */
        if (mWidth < mTextRect.width()) {
            mTextPaint.set(mPaint);
            String msg = TextUtils.ellipsize(mText, mTextPaint,
                    (float) mWidth - getPaddingLeft() - getPaddingRight(),
                    TextUtils.TruncateAt.END).toString();
            // 文字绘制起点在左下角
            canvas.drawText(msg, getPaddingLeft(), mHeight - getPaddingBottom(), mPaint);
        } else {
            canvas.drawText(mText, mWidth / 2 - mTextRect.width() / 2, mHeight - getPaddingBottom(), mPaint);
        }

        // 绘制图片

        //取消使用掉的文字块
        mRect.bottom -= mTextRect.height();

        if (mImageScale == IMAGE_SCALE_FITXY) {
            canvas.drawBitmap(mImage, null, mRect, mPaint);
        } else {
            //计算居中的矩形范围
            mRect.left = mWidth / 2 - mImage.getWidth() / 2;
            mRect.right = mWidth / 2 + mImage.getWidth() / 2;
            mRect.top = (mHeight - mTextRect.height()) / 2 - mImage.getHeight() / 2;
            mRect.bottom = (mHeight - mTextRect.height()) / 2 + mImage.getHeight() / 2;

            canvas.drawBitmap(mImage, null, mRect, mPaint);
        }

    }

}
