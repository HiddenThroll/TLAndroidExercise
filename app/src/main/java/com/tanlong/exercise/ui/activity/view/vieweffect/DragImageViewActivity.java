package com.tanlong.exercise.ui.activity.view.vieweffect;

import android.databinding.DataBindingUtil;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityDragImageviewBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.util.DisplayUtil;

/**
 * @author 龙
 */
public class DragImageViewActivity extends BaseActivity implements View.OnClickListener {

    ActivityDragImageviewBinding binding;

    ImageView ivBack;
    TextView tvTitle;

    ImageView ivDrag;
    WindowManager.LayoutParams params;

    WindowManager windowManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_drag_imageview);
        binding.setActivity(this);

        ivBack = binding.toolbar.findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        tvTitle = binding.toolbar.findViewById(R.id.tv_title);
        tvTitle.setText("可拖动ImageView");

        windowManager = getWindowManager();
        ivDrag = new ImageView(this);
        ivDrag.setImageResource(R.mipmap.ic_launcher);
        ivDrag.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        ivDrag.setOnTouchListener(new View.OnTouchListener() {
            float lastX, lastY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = x;
                        lastY = y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Logger.e("x is " + params.x + " y is " + params.y);
                        params.x += (x - lastX) / 2;
                        params.y += (y - lastY) / 2;
                        windowManager.updateViewLayout(ivDrag, params);

                        lastX = x;
                        lastY = y;
                        break;
                    default:
                        break;
                }

                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    public void addDragView() {
        if (params != null) {
            Logger.e("已添加ImageView");
            return;
        }

        params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.format = PixelFormat.TRANSPARENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        windowManager.addView(ivDrag, params);
    }

    public void removeDragView() {

    }
}
