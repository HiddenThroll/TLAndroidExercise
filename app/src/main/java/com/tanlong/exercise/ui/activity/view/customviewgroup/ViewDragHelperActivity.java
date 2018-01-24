package com.tanlong.exercise.ui.activity.view.customviewgroup;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 龙 on 2016/6/29.
 */
public class ViewDragHelperActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.btn_help)
    Button btnHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_drag_helper);
        ButterKnife.bind(this);

        mTvTitle.setText(R.string.view_drag_helper_exercise);
        btnHelp.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.iv_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.btn_help)
    public void onHelp() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("1. 基本使用ViewDragHelper:\n")
                .append("1.1 ViewGroup\n")
                .append("1.1.1 覆写onInterceptTouchEvent()方法, 由ViewDragHelper拦截触摸事件\n")
                .append("1.1.2 覆写onTouchEvent()方法, 由ViewDragHelper处理触摸事件\n")
                .append("1.2 ViewDragHelper\n")
                .append("1.2.1 覆写tryCaptureView()方法, 要对当前触摸的子View进行拖拽移动就返回true，否则返回false\n")
                .append("1.2.2 覆写clampViewPositionHorizontal(View child, int left, int dx)方法,对child移动的水平边界进行控制，left为水平方向期望移动到的位置, 返回值为子View在最终位置时的left值\n")
                .append("1.2.3 覆写clampViewPositionVertical(View child, int top, int dy)方法,对child移动的垂直边界进行控制，与clampViewPositionHorizontal()同理\n")
                .append("1.2.4 覆写getViewHorizontalDragRange(View child)方法, 返回值大于0时, 水平方向可以拖动\n")
                .append("1.2.5 覆写getViewVerticalDragRange(View child)方法, 返回值大于0时, 垂直方向可以拖动\n")
                .append("2. 实现拖动松手后返回效果:\n")
                .append("2.1 ViewGroup\n")
                .append("2.1.1 覆写computeScroll()方法, 实现滚动中重绘View\n")
                .append("2.2 ViewDragHelper\n")
                .append("2.2.1 覆写onViewReleased()方法, 手指释放时回调该方法, 在该方法中设置滚动位置, 启动滚动\n")
                .append("3. 实现边缘操作\n")
                .append("3.1 ViewGroup\n")
                .append("3.1.1 设置ViewDragHelper可以响应左边缘拖动, 即ViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT)\n")
                .append("3.2 ViewDragHelper\n")
                .append("3.2.1 覆写onEdgeDragStarted()方法, 通过ViewDragHelper.captureChildView对View进行捕获\n")
                ;
        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }
}
