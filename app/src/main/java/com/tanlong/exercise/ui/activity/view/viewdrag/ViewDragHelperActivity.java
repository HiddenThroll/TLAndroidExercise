package com.tanlong.exercise.ui.activity.view.viewdrag;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 龙 on 2016/6/29.
 */
public class ViewDragHelperActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_drag_helper);
        ButterKnife.bind(this);

        mTvTitle.setText(R.string.view_drag_helper_exercise);
    }

    @OnClick(R.id.iv_back)
    public void onBack() {
        finish();
    }
}
