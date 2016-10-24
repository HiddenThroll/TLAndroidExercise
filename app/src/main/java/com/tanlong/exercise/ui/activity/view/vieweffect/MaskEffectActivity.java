package com.tanlong.exercise.ui.activity.view.vieweffect;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mask_effect);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        tvTitle.setText(R.string.view_effect_mask);
        showMask();
    }

    private void showMask() {
        try {
            // 动态初始化图层
            ImageView ivMask = new ImageView(this);
            ivMask.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            ivMask.setScaleType(ImageView.ScaleType.FIT_XY);
            ivMask.setImageResource(R.mipmap.ic_mask);

            // 设置LayoutParams参数
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            // 设置显示的类型，TYPE_PHONE指的是来电话的时候会被覆盖，其他时候会在最前端，显示位置在stateBar下面，其他更多的值请查阅文档
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
            // 设置显示格式
            params.format = PixelFormat.RGBA_8888;
            // 设置对齐方式
            params.gravity = Gravity.LEFT | Gravity.TOP;
            // 设置宽高
            params.width = DisplayUtil.getDisplay(this).x;
            params.height = DisplayUtil.getDisplay(this).y;

            getWindowManager().addView(ivMask, params);
        } catch (Exception e) {
            e.printStackTrace();
            LogTool.e(TAG, e.getMessage());
        }
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}
