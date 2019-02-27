package com.tanlong.exercise.ui.view.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.tanlong.exercise.R;
import com.tanlong.exercise.util.NumberUtil;
import com.tanlong.exercise.util.ToastHelp;

/**
 * 简单视频播放控制View,模拟实现 暂停/播放 声音加减 亮度加减 快进快退功能
 *
 * @author 龙
 */
public class SimpleVideoControlView extends View {
    private final String TAG = "SimpleVideoControlView";

    private Paint mPaint;
    private GestureDetector gestureDetector;
    private final int TRIGGER_DISTANCE = ViewConfiguration.getTouchSlop() * 2;
    /**
     * 是否快退/快进
     */
    private boolean skipProgress = false;
    private int fastType;
    private final int TYPE_FAST_FORWARD = 1;
    private final int TYPE_FAST_RETREAT = 2;
    /**
     * 快进快退进度
     */
    private float forwardProgress = 0;

    public SimpleVideoControlView(Context context) {
        this(context, null);
    }

    public SimpleVideoControlView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleVideoControlView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        gestureDetector = new GestureDetector(getContext(), gestureListener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //区分开左右部分
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_86d0ab));
        canvas.drawRect(0, 0, getWidth() / 2, getHeight(), mPaint);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_282c76));
        canvas.drawRect(getWidth() / 2, 0, getWidth(), getHeight(), mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = gestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                handleForwardRetreat();
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 处理快进快退
     */
    private void handleForwardRetreat() {
        if (skipProgress) {
            skipProgress = false;
            switch (fastType) {
                case TYPE_FAST_FORWARD:
                    ToastHelp.showShortMsg(getContext(), "快进" + NumberUtil.keepTwoDecimal(forwardProgress * 100) + "%");
                    break;
                case TYPE_FAST_RETREAT:
                    ToastHelp.showShortMsg(getContext(), "快退" + NumberUtil.keepTwoDecimal(forwardProgress * 100) + "%");
                    break;
                default:
                    break;
            }
        }
    }

    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            //onDown各种手势的起点,一般都返回true
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            ToastHelp.showShortMsg(getContext(), "显示播放/暂停按钮");
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //区分上下左右
            float deltaX = e2.getX() - e1.getX();
            float deltaY = e2.getY() - e1.getY();
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                //水平方向移动 大于限定值才触发
                if (Math.abs(deltaX) >= TRIGGER_DISTANCE) {
                    skipProgress = true;
                    if (deltaX > 0) {
                        fastType = TYPE_FAST_FORWARD;
                    } else {
                        fastType = TYPE_FAST_RETREAT;
                    }
                    forwardProgress = Math.abs(deltaX) / getWidth();
                }
            } else {
                //垂直方向移动 大于限制值才触发
                if (Math.abs(deltaY) >= TRIGGER_DISTANCE) {
                    if (deltaY > 0) {
                        //下滑, 区分在哪个半区
                        if (occurInLeft(e1)) {
                            //左半区 修改亮度
                            ToastHelp.showShortMsg(getContext(), "降低亮度");
                        } else {
                            //右半区 修改声音
                            ToastHelp.showShortMsg(getContext(), "减小声音");
                        }
                    } else {
                        //上滑, 区分在哪个半区
                        if (occurInLeft(e1)) {
                            ToastHelp.showShortMsg(getContext(), "增加亮度");
                        } else {
                            ToastHelp.showShortMsg(getContext(), "增大声音");
                        }
                    }
                }
            }
            return true;
        }

    };

    private boolean occurInLeft(MotionEvent e) {
        return e.getX() >= 0 && e.getX() <= (getWidth() / 2);
    }

}
