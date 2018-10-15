package com.tanlong.exercise.ui.activity.view.vieweffect;

import android.databinding.DataBindingUtil;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityDragImageviewBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.util.ToastHelp;

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
            int xOffset, yOffset, x, y;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //滑动前 相对于屏幕的距离
                        xOffset = (int) event.getRawX();
                        yOffset = (int) event.getRawY();
                        x = params.x;
                        y = params.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        params.x = x + (int)event.getRawX() - xOffset;
                        params.y = y + (int)event.getRawY() - yOffset;
                        windowManager.updateViewLayout(ivDrag, params);
                        break;
                    default:
                        break;
                }

                return false;
            }
        });

        ivDrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastHelp.showShortMsg(DragImageViewActivity.this, "点击Drag ImageView");
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
        if (params != null) {
            windowManager.removeView(ivDrag);
            params = null;
        }
    }
}
