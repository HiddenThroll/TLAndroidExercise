package com.tanlong.exercise.ui.activity.view.vieweffect;

import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityTouchDelegateBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.util.ToastHelp;

/**
 * @author 龙
 */
public class TouchDelegateActivity extends BaseActivity {
    ActivityTouchDelegateBinding binding;
    RelativeLayout rlContainer;
    ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_touch_delegate);
        rlContainer = binding.rlContainer;
        imageButton = binding.imageBtn;

        rlContainer.post(new Runnable() {
            @Override
            public void run() {
                imageButton.setEnabled(true);
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastHelp.showShortMsg(TouchDelegateActivity.this, "点击ImageButton");
                    }
                });

                //获取点击区域
                Rect delegateArea = new Rect();
                imageButton.getHitRect(delegateArea);
                //增大区域
                delegateArea.right += 200;
                delegateArea.bottom += 200;
                //新建TouchDelegate
                TouchDelegate touchDelegate = new TouchDelegate(delegateArea, imageButton);
                //关联TouchDelegate
                if (View.class.isInstance(imageButton.getParent())) {
                    ((View)imageButton.getParent()).setTouchDelegate(touchDelegate);
                }
            }
        });
    }
}
