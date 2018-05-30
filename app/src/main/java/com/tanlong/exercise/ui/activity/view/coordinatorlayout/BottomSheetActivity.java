package com.tanlong.exercise.ui.activity.view.coordinatorlayout;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.view.View;

import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityBottomSheetBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;

/**
 * @author 龙
 */
public class BottomSheetActivity extends BaseActivity {
    ActivityBottomSheetBinding binding;
    BottomSheetBehavior sheetBehavior;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_bottom_sheet);
        binding.setActivity(this);
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
}
