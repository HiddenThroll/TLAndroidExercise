package com.tanlong.exercise.ui.activity.view.constraintlayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 龙 on 2017/8/2.
 */

public class SimpleConstraintActivity extends BaseActivity {

    @Bind(R.id.button12)
    Button button12;
    @Bind(R.id.button13)
    Button button13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_simple_constraint);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button13)
    public void onViewClicked() {
        button12.setVisibility(View.GONE);
    }

}
