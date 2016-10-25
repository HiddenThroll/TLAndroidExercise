package com.tanlong.exercise.ui.activity.view.vieweffect;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.util.DisplayUtil;
import com.tanlong.exercise.util.LogTool;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 龙 on 2016/10/24.
 */

public class MaskEffectActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;

    ImageView ivMask;
    WindowManager windowManager;

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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    showMask();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1000);//延迟一段时间显示，避免要显示弹框时，父View还没创建完毕，
                 // 抛出android.view.WindowManager$BadTokenException异常
    }

    private void initView() {
        tvTitle.setText(R.string.view_effect_mask);

        ivMask = new ImageView(MaskEffectActivity.this);
        ivMask.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        ivMask.setScaleType(ImageView.ScaleType.FIT_XY);
        ivMask.setImageResource(R.mipmap.ic_mask);

        ivMask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windowManager.removeView(ivMask);
            }
        });
    }

    private void showMask() {
        LogTool.e(TAG, "showMask");
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        // 设置显示的类型，
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        // 设置显示格式
        params.format = PixelFormat.RGBA_8888;
        // 设置对齐方式
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.width = DisplayUtil.getDisplay(this).x;
        params.height = DisplayUtil.getDisplay(this).y;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        windowManager.addView(ivMask, params);
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}
