package com.tanlong.exercise.ui.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.tanlong.exercise.R;
import com.tanlong.exercise.util.DisplayUtil;
import com.tanlong.exercise.util.LogTool;

import java.util.Random;

/**
 * 自定义TitleView, 练习使用
 * Created by Administrator on 2016/4/15.
 */
public class CustomTitleView extends View {

    private final String TAG = "CustomTitleView";

    private String mText;
    private int mTextColor;
    private float mTextSize;

    private Paint mPaint;
    private Rect mRect;// 绘制区域

    public CustomTitleView(Context context) {
        this(context, null);
    }

    public CustomTitleView(Context context, AttributeSet arrts) {
        this(context, arrts, 0);
    }

    public CustomTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // TODO 2. 获得我们所定义的自定义样式属性
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTitleView,
                defStyleAttr, 0);
        for (int i = 0, size = array.length(); i < size; i++) {
            int attr = array.getIndex(i);
            LogTool.e(TAG, "attr is " + attr);
            switch (attr) {
                case R.styleable.CustomTitleView_custom_title_text:
                    mText = array.getString(attr);
                    break;
                case R.styleable.CustomTitleView_custom_title_color:
                    mTextColor = array.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomTitleView_custom_title_size:
                    mTextSize = array.getDimensionPixelSize(attr, DisplayUtil.sp2px(context, 16));
                    break;
            }
        }
        array.recycle();// 使用完毕记得释放资源

        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);
        mRect = new Rect();
        // 返回能够包裹绘制内容的最小矩形
        mPaint.getTextBounds(mText, 0, mText.length(), mRect);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mText = getRandomText();
                requestLayout();// 重新布局, 会触发onDraw方法
            }
        });
    }

    //TODO 覆写onMeasure方法
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width, height;

        if (widthMode == MeasureSpec.EXACTLY) {// 使用指定宽度
            width = widthSize;
        } else {// 计算应有宽度
            mPaint.setTextSize(mTextSize);
            mPaint.getTextBounds(mText, 0, mText.length(), mRect);
            width = getPaddingLeft() + mRect.width() + getPaddingRight();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mPaint.setTextSize(mTextSize);
            mPaint.getTextBounds(mText, 0, mText.length(), mRect);
            height = getPaddingTop() + mRect.height() + getPaddingBottom();
        }
        setMeasuredDimension(width, height);
    }

    // TODO 覆写onDraw方法
    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        mPaint.setColor(mTextColor);
        /**
         * 设置文字绘制起点为 getWidth() / 2 - mRect.width() / 2, getHeight() / 2 +
         *  mRect.height() / 2，纵坐标设置为getHeight() / 2 + mRect.height() / 2是因为文字绘制起点在其左下角
         */
        canvas.drawText(mText, getWidth() / 2 - mRect.width() / 2, getHeight() / 2 +
                mRect.height() / 2, mPaint);
    }

    private String getRandomText() {
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            result.append(random.nextInt(10));
        }
        return result.toString();
    }

}
