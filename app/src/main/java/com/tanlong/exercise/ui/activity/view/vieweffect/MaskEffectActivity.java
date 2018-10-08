package com.tanlong.exercise.ui.activity.view.vieweffect;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;
import com.tanlong.exercise.util.DisplayUtil;
import com.tanlong.exercise.util.LogTool;
import com.tanlong.exercise.util.VersionUtil;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 蒙版效果
 * Created by 龙 on 2016/10/24.
 */

public class MaskEffectActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    ImageView ivMask;
    WindowManager windowManager;
    @BindView(R.id.btn_help)
    Button btnHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mask_effect);
        ButterKnife.bind(this);

        windowManager = getWindowManager();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        tvTitle.setText(R.string.view_effect_mask);
        btnHelp.setVisibility(View.VISIBLE);

        ivMask = new ImageView(MaskEffectActivity.this);
        ivMask.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        ivMask.setScaleType(ImageView.ScaleType.FIT_XY);
        ivMask.setImageResource(R.mipmap.ic_mask);

        ivMask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //要删除一个Window,删除它里面的View即可
                windowManager.removeView(ivMask);
            }
        });

        tvTitle.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (VersionUtil.hasJellyBean()) {
                    tvTitle.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    tvTitle.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                showMask();
            }
        });
    }

    private void showMask() {
        LogTool.e(TAG, "showMask");
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        // 设置显示的Window类型为子Window,它将悬浮在应用Window(如Activity)之上
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        // 设置显示格式
        params.format = PixelFormat.RGBA_8888;
        // 设置对齐方式
        params.gravity = Gravity.START | Gravity.TOP;
        // 设置Window显示的大小
        params.width = DisplayUtil.getDisplay(this).x / 2;
        params.height = DisplayUtil.getDisplay(this).y / 2;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //添加Window
        windowManager.addView(ivMask, params);
    }

    @OnClick({R.id.iv_back, R.id.btn_help})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                showTips();
                break;
            default:
                break;
        }
    }

    private void showTips() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("1. 使用WindowManager.addView(View, WindowManager.LayoutParams)方法在当前窗口之上显示ImageView实现蒙版效果\n")
                .append("2. WindowManager.LayoutParams重点配置为：\n")
                .append("2.1 LayoutParams.type设置为TYPE_APPLICATION_PANEL，表示添加的Window在当前附着Window之上\n")
                .append("2.2 LayoutParams.format设置为RGBA_8888，设置显示格式\n")
                .append("2.3 LayoutParams.gravity设置对齐方式\n")
                .append("2.4 LayoutParams.flags设置为FLAG_NOT_TOUCH_MODAL|FLAG_WATCH_OUTSIDE_TOUCH|FLAG_NOT_FOCUSABLE\n")
                .append("2.4.1 FLAG_NOT_TOUCH_MODAL表示任何该窗口外的指针事件由该窗口之后（下）的窗口处理\n")
                .append("2.4.2 FLAG_WATCH_OUTSIDE_TOUCH表示当设置FLAG_NOT_TOUCH_MODAL后，当窗口外有触摸事件发生时，会接收到一个单独的事件，MotionEvent.ACTION_OUTSIDE\n")
                .append("2.4.3 FLAG_NOT_FOCUSABLE表示该窗口不接收键盘输入焦点，键盘输入焦点由该窗口之后（下）的窗口处理\n");
        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }
}
