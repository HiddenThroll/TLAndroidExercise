package com.tanlong.exercise.ui.activity.view.optimization;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityViewstubExampleBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;

/**
 * @author 龙
 */
public class ViewStubExampleActivity extends BaseActivity {
    ActivityViewstubExampleBinding binding;

    boolean isShowing = false;
    ConstraintLayout clNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_viewstub_example);
        binding.setActivity(this);
    }

    public void showHideNetworkError() {
        if (isShowing) {
            isShowing = false;
            hideNetworkError();
        } else {
            isShowing = true;
            showNetworkError();
        }
    }

    private void showNetworkError() {
        if (binding.stubImport.getViewStub() != null) {
            binding.stubImport.getViewStub().setVisibility(View.VISIBLE);
            clNetwork = findViewById(R.id.cl_network_error);
        } else {
            clNetwork.setVisibility(View.VISIBLE);
        }
    }

    private void hideNetworkError() {
        if (clNetwork != null) {
            clNetwork.setVisibility(View.GONE);
        }
    }
}
