package com.tanlong.exercise.ui.view.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.tanlong.exercise.util.LogTool;

public class TestRecyclerView extends RecyclerView {
    private final String TAG = getClass().getSimpleName();
    /**
     * 缓存的上次移动距离
     */
    private float mLastY = -1;

    private LinearLayoutManager layoutManager;

    private int firstCompleteVisibleItemPosition;

    public TestRecyclerView(Context context) {
        this(context, null);
    }

    public TestRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        layoutManager = (LinearLayoutManager) getLayoutManager();
        if (mLastY == -1) {
            mLastY = e.getRawY();
        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下的时候记录值
                mLastY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = e.getRawY();
                //从 按下 到 当前滑动位置,  手指滑动的差值
                float distanceY = moveY - mLastY;
                LogTool.e(TAG, "getRawY() is " + e.getRawY() + " mLastY is " + mLastY);

                //真实第一个Item 完全可见 并且 Header的可见高度>0

                firstCompleteVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                if (firstCompleteVisibleItemPosition == 0 ) {
                    // 0 对应 HeaderView未显示 1 对应 HeaderView 部分显示/完全显示
                    if (distanceY > 0) {
                        if (iPullListener != null) {
                            iPullListener.onPull(distanceY);
                            return true;
                        }
                    }
                }
                break;
            default:
//                mLastY = -1;
                firstCompleteVisibleItemPosition = layoutManager
                        .findFirstCompletelyVisibleItemPosition();
                if (firstCompleteVisibleItemPosition == 0 ) {
                    // 松手的时候  高度大于  一定值  调用刷新
                    if (iPullListener != null) {
                        iPullListener.reset();
                    }
                } else {
                    if (iPullListener != null) {
                        iPullListener.reset();
                    }
                }
                break;
        }


        return super.onTouchEvent(e);
    }

    private void resetHeaderHeight() {

    }

    private IPullListener iPullListener;

    public void setiPullListener(IPullListener iPullListener) {
        this.iPullListener = iPullListener;
    }

    public interface IPullListener {
        /**
         * 下拉操作
         * @param y -- 当前Y坐标
         */
        void onPull(float y);

        void reset();
    }
}
