package com.tanlong.exercise.util;

import android.content.Context;
import android.view.MotionEvent;

/**
 * 手势检测基类
 */
public abstract class BaseGestureDetector {
    /**
     * 是否是进行中的手势
     */
    protected boolean mGestureInProgress;
    /**
     * 之前的手势事件
     */
    protected MotionEvent mPreMotionEvent;
    /**
     * 当前的手势事件
     */
    protected MotionEvent mCurrentMotionEvent;

    protected Context mContext;

    public BaseGestureDetector(Context context) {
        mContext = context;
    }

    /**
     * 接管View的onTouchEvent
     * @param event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event) {
        if (!mGestureInProgress) {
            handleStartProgressEvent(event);
        } else {
            handleInProgressEvent(event);
        }
        return true;
    }

    /**
     * 处理进行中的手势(ACTION_MOVE 和 ACTION_UP)
     * @param event
     */
    protected abstract void handleInProgressEvent(MotionEvent event);

    /**
     * 处理手势的开始 (ACTION_DOWN)
     * @param event
     */
    protected abstract void handleStartProgressEvent(MotionEvent event);

    /**
     * 更新状态
     * @param event
     */
    protected abstract void updateStateByEvent(MotionEvent event);

    /**
     * 重置状态
     */
    protected void resetState() {
        if (mPreMotionEvent != null) {
            mPreMotionEvent.recycle();
            mPreMotionEvent = null;
        }
        if (mCurrentMotionEvent != null) {
            mCurrentMotionEvent.recycle();
            mCurrentMotionEvent = null;
        }
        mGestureInProgress = false;
    }

}
