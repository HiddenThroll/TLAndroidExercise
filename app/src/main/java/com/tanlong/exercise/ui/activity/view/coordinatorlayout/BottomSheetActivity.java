package com.tanlong.exercise.ui.activity.view.coordinatorlayout;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityBottomSheetBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;

/**
 * @author 龙
 */
public class BottomSheetActivity extends BaseActivity implements View.OnClickListener {
    ActivityBottomSheetBinding binding;
    BottomSheetBehavior sheetBehavior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_bottom_sheet);
        binding.setActivity(this);
        TextView tvTitle = binding.toolbar.findViewById(R.id.tv_title);
        tvTitle.setText("BottomSheet");
        binding.toolbar.findViewById(R.id.iv_back).setOnClickListener(this);
        Button help = binding.toolbar.findViewById(R.id.btn_help);
        help.setVisibility(View.VISIBLE);
        help.setOnClickListener(this);
        sheetBehavior = BottomSheetBehavior.from(binding.flShareView);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Log.e(TAG, "newState is " + newState);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        sheetBehavior.setHideable(true);
    }

    public void showHideBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
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
        stringBuilder.append("BottomSheet\n")
                .append("1.BottomSheet用于实现底部弹出布局效果\n")
                .append("2.布局结构\n")
                .append("2.1 弹出的布局必须是CoordinatorLayout的直接子布局\n")
                .append("3. 设置相应属性\n")
                .append("3.1 弹出布局设置app:layout_behavior=\"@string/bottom_sheet_behavior\"\n")
                .append("3.2 设置app:behavior_peekHeight=\"32dp\"指定折叠状态时弹出布局高度\n")
                .append("4. 代码设置\n")
                .append("4.1 通过BottomSheetBehavior.from(View)获得BottomSheetBehavior,这里传入的View即弹出View\n")
                .append("4.2 通过BottomSheetBehavior.setState()/getState()获取弹出View当前状态,一共有5种状态:\n")
                .append("4.2.1 STATE_EXPANDED 展开状态，显示完整布局\n")
                .append("4.2.2 STATE_COLLAPSED 折叠状态，显示peekHeigth的高度\n")
                .append("4.2.3 STATE_DRAGGING 拖拽时的状态\n")
                .append("4.2.4 STATE_HIDDEN 隐藏时的状态\n")
                .append("4.2.5 STATE_SETTLING 释放时的状态\n")
                .append("4.3 通过BottomSheetBehavior.setBottomSheetCallback()设置状态监听回调\n")
                .append("5. 可以拖动弹出View的原因在于BottomSheetBehavior内置了ViewDragHelper属性,通过ViewDragHelper实现对弹出View的拖动控制")
                .append("");
        ShowTipsFragment.newInstance(stringBuilder.toString()).show(getSupportFragmentManager(), "");
    }
}
