package com.tanlong.exercise.ui.activity.view.customviewgroup;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityInteractiveChartBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.view.customview.interativechart.InteractiveLineGraphView;

/**
 * @author 龙
 */
public class InteractiveChartActivity extends BaseActivity {

    private InteractiveLineGraphView mGraphView;
    ActivityInteractiveChartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_interactive_chart);
        binding.setActivity(this);
        mGraphView = findViewById(R.id.chart);
    }

    public void zoomIn() {
        mGraphView.zoomIn();
    }

    public void zoomOut() {
        mGraphView.zoomOut();
    }

    public void panLeft() {
        mGraphView.panLeft();
    }

    public void panRight() {
        mGraphView.panRight();
    }

    public void panUp() {
        mGraphView.panUp();
    }

    public void panDown() {
        mGraphView.panDown();
    }
}
