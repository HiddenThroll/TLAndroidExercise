package com.tanlong.exercise.ui.activity.view.customviewgroup;

import android.os.Bundle;


import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.view.customviewgroup.VerticalLinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 * Created by Administrator on 2016/5/22.
 */
public class VerticalLinearLayoutActivity extends BaseActivity {

    @Bind(R.id.vll_activity_vertical_linear_layout)
    VerticalLinearLayout mVerticalLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vertical_linear_layout);
        ButterKnife.bind(this);

        mVerticalLinearLayout.setOnPageChangeListener(new VerticalLinearLayout.OnPageChangeListener() {
            @Override
            public void onPageChange(int currentPage) {
                showShortMessage("currentPage is " + (currentPage + 1));
            }
        });
    }
}
