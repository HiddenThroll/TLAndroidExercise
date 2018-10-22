package com.tanlong.exercise.util;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;

/**
 * 移动手势检测
 * @author 龙
 */
public class MoveGestureDetector extends BaseGestureDetector {
    /**
     * 当前手势事件 对应的 PointF
     */
    private PointF mCurrentPointer;
    /**
     * 之前手势事件 对应的 PointF
     */
    private PointF mPrePointer;
    /**
     * 用于记录最终结果，并返回
     */
    private PointF mExtenalPointer = new PointF();

    private OnMoveGestureListener mListener;

    public MoveGestureDetector(Context context, OnMoveGestureListener listener) {
        super(context);
        mListener = listener;
    }

    @Override
    protected void handleInProgressEvent(MotionEvent event) {
        int actionCode = event.getAction() & MotionEvent.ACTION_MASK;
        switch (actionCode) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mListener.onMoveEnd(this);
                resetState();
                break;
            case MotionEvent.ACTION_MOVE:
                updateStateByEvent(event);
                boolean update = mListener.onMove(this);
                //是否更新前一个事件
                if (update) {
                    mPreMotionEvent.recycle();
                    mPreMotionEvent = MotionEvent.obtain(event);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void handleStartProgressEvent(MotionEvent event) {
        int actionCode = event.getAction() & MotionEvent.ACTION_MASK;
        switch (actionCode) {
            case MotionEvent.ACTION_DOWN:
                resetState();//防止没有接收到CANCEL or UP ,保险起见
                mPreMotionEvent = MotionEvent.obtain(event);
                updateStateByEvent(event);
                break;
            case MotionEvent.ACTION_MOVE:
                mGestureInProgress = mListener.onMoveBegin(this);
                break;
            default:
                break;
        }

    }

    @Override
    protected void updateStateByEvent(MotionEvent event) {
        final MotionEvent prev = mPreMotionEvent;

        mPrePointer = caculateFocalPointer(prev);
        mCurrentPointer = caculateFocalPointer(event);
        //是否忽略 该事件
        boolean mSkipThisMoveEvent = prev.getPointerCount() != event.getPointerCount();
        //计算前后事件的偏移量
        mExtenalPointer.x = mSkipThisMoveEvent ? 0 : mCurrentPointer.x - mPrePointer.x;
        mExtenalPointer.y = mSkipThisMoveEvent ? 0 : mCurrentPointer.y - mPrePointer.y;
    }

    /**
     * 根据event计算多指中心点
     *
     * @param event
     * @return
     */
    private PointF caculateFocalPointer(MotionEvent event) {
        final int count = event.getPointerCount();
        float x = 0, y = 0;
        for (int i = 0; i < count; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }

        x /= count;
        y /= count;

        return new PointF(x, y);
    }


    public float getMoveX() {
        return mExtenalPointer.x;

    }

    public float getMoveY() {
        return mExtenalPointer.y;
    }


    public interface OnMoveGestureListener {
        /**
         * 开始移动
         * @param detector
         * @return
         */
        boolean onMoveBegin(MoveGestureDetector detector);

        /**
         * 移动中
         * @param detector
         * @return
         */
        boolean onMove(MoveGestureDetector detector);

        /**
         * 移动结束
         * @param detector
         */
        void onMoveEnd(MoveGestureDetector detector);
    }

    public static class SimpleMoveGestureDetector implements OnMoveGestureListener {

        @Override
        public boolean onMoveBegin(MoveGestureDetector detector) {
            return true;
        }

        @Override
        public boolean onMove(MoveGestureDetector detector) {
            return false;
        }

        @Override
        public void onMoveEnd(MoveGestureDetector detector) {
        }
    }

}
