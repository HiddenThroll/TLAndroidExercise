package com.tanlong.exercise.ui.view.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tanlong.exercise.util.MoveGestureDetector;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 显示超大图片的View
 * @author 龙
 */
public class LargeImageView extends View {
    /**
     * 区域加载Bitmap 工具类
     */
    private BitmapRegionDecoder mDecoder;
    /**
     * 图片的宽度和高度
     */
    private int mImageWidth, mImageHeight;
    /**
     * 绘制的区域
     */
    private volatile Rect mRect = new Rect();

    private MoveGestureDetector mDetector;
    /**
     * 展示的Bitmap
     */
    private Bitmap bm;

    private ExecutorService threadPool;


    private static final BitmapFactory.Options options = new BitmapFactory.Options();

    static {
        options.inPreferredConfig = Bitmap.Config.RGB_565;
    }

    public LargeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        threadPool = Executors.newSingleThreadExecutor();
    }

    /**
     * 在子线程中加载图片,提高UI渲染速度
     */
    private void decodeBitmap() {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                bm = mDecoder.decodeRegion(mRect, options);
                post(new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                    }
                });
            }
        });
    }

    private void init() {
        mDetector = new MoveGestureDetector(getContext(), new MoveGestureDetector.SimpleMoveGestureDetector() {
            @Override
            public boolean onMove(MoveGestureDetector detector) {
                int moveX = (int) detector.getMoveX();
                int moveY = (int) detector.getMoveY();

                if (mImageWidth > getWidth()) {
                    mRect.offset(-moveX, 0);
                    checkWidth();
                    decodeBitmap();
                }
                if (mImageHeight > getHeight()) {
                    mRect.offset(0, -moveY);
                    checkHeight();
                    decodeBitmap();
                }
                return true;
            }
        });
    }

    public void setInputStream(InputStream is) {
        try {
            mDecoder = BitmapRegionDecoder.newInstance(is, false);
            BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
            // Grab the bounds for the scene dimensions
            tmpOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, tmpOptions);
            mImageWidth = tmpOptions.outWidth;
            mImageHeight = tmpOptions.outHeight;

            requestLayout();
            invalidate();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 宽度边界检测
     */
    private void checkWidth() {
        Rect rect = mRect;
        int imageWidth = mImageWidth;

        if (rect.right > imageWidth) {
            rect.right = imageWidth;
            rect.left = imageWidth - getWidth();
        }

        if (rect.left < 0) {
            rect.left = 0;
            rect.right = getWidth();
        }
    }

    /**
     * 高度边界检测
     */
    private void checkHeight() {
        Rect rect = mRect;
        int imageHeight = mImageHeight;

        if (rect.bottom > imageHeight) {
            rect.bottom = imageHeight;
            rect.top = imageHeight - getHeight();
        }

        if (rect.top < 0) {
            rect.top = 0;
            rect.bottom = getHeight();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;

        //默认直接显示图片的中心区域，可以自己去调节
        mRect.left = imageWidth / 2 - width / 2;
        mRect.top = imageHeight / 2 - height / 2;
        mRect.right = mRect.left + width;
        mRect.bottom = mRect.top + height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bm == null) {
            //对应第一次绘制
            bm = mDecoder.decodeRegion(mRect, options);
        }
        canvas.drawBitmap(bm, 0, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (bm != null) {
            bm.recycle();
        }
        if (!threadPool.isShutdown()) {
            threadPool.shutdown();
        }
    }
}
