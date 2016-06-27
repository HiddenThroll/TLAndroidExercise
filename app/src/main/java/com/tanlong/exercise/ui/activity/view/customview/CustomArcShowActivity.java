package com.tanlong.exercise.ui.activity.view.customview;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.view.customview.CustomArcShowView;
import com.tanlong.exercise.util.LogTool;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/20.
 */
public class CustomArcShowActivity extends BaseActivity {

    @Bind(R.id.cas_activity_custom_arc_show)
    CustomArcShowView mCustomArcShowView;
    @Bind(R.id.btn_activity_custom_arc_show_Add)
    Button mBtnAdd;
    @Bind(R.id.btn_activity_custom_arc_show_Reduce)
    Button mBtnReduce;
    @Bind(R.id.et_activity_custom_arc_show_Count)
    EditText mEtCount;
    @Bind(R.id.btn_activity_custom_arc_show_Set)
    Button mBtnSet;
    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_custom_arc_show);
        ButterKnife.bind(this);

        mTvTitle.setText(R.string.custom_arc_show_view);
    }

    @OnClick(R.id.btn_activity_custom_arc_show_Add)
    public void addCount() {
        mCustomArcShowView.addCount();
    }

    @OnClick(R.id.btn_activity_custom_arc_show_Reduce)
    public void reduceCount() {
        mCustomArcShowView.reduceCount();
    }

    @OnClick(R.id.btn_activity_custom_arc_show_Set)
    public void setCount() {
        if (TextUtils.isEmpty(mEtCount.getText())) {
            return;
        }
        int total = mCustomArcShowView.getmTotalCount();
        int current = Integer.valueOf(mEtCount.getText().toString());
        if (current >= total) {
            showShortMessage("total count is " + total);
            current = total;
            mEtCount.setText(String.valueOf(current));
            String count = mEtCount.getText().toString();
            mEtCount.setSelection(count.length());
            LogTool.e(TAG, "length is " + count.length());
        }

        ObjectAnimator animator = ObjectAnimator.ofInt(mCustomArcShowView, "currentCount", current);
        animator.start();
    }

    @OnClick(R.id.iv_back)
    public void onBack() {
        finish();
    }
}
