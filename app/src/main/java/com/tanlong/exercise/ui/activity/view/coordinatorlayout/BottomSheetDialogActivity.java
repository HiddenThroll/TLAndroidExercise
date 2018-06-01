package com.tanlong.exercise.ui.activity.view.coordinatorlayout;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityBottomSheetDialogBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;

/**
 * BottomSheetDialog
 *
 * @author 龙
 */
public class BottomSheetDialogActivity extends BaseActivity implements View.OnClickListener {
    ActivityBottomSheetDialogBinding binding;
    BottomSheetDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_bottom_sheet_dialog);
        binding.setActivity(this);
        TextView tvTitle = binding.toolbar.findViewById(R.id.tv_title);
        tvTitle.setText("BottomSheetDialog");
        binding.toolbar.findViewById(R.id.iv_back).setOnClickListener(this);
        Button btnHelp = binding.toolbar.findViewById(R.id.btn_help);
        btnHelp.setVisibility(View.VISIBLE);
        btnHelp.setOnClickListener(this);
    }

    public void showHideBottomSheetDialog() {
        if (dialog == null) {
            dialog = new BottomSheetDialog(this);
            dialog.setContentView(R.layout.layout_sheet_share_dialog);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        } else {
            dialog.show();
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
        stringBuilder.append("1.BottomSheetDialog是对BottomSheet的封装,通过对传入View添加BottomSheet Behavior,实现底部弹出传入View的Dialog效果\n")
                .append("2.代码设置\n")
                .append("2.1 通过new BottomSheetDialog(Context)获取BottomSheetDialog实例\n")
                .append("2.2 通过BottomSheetDialog.setContentView()设置展示View\n")
                .append("2.3 按需调用setCancelable(),setCanceledOnTouchOutside()等方法\n")
                .append("2.4 通过BottomSheetDialog.show()展示对话框\n")
                .append("");
        ShowTipsFragment.newInstance(stringBuilder.toString()).show(getSupportFragmentManager(), "");
    }
}
