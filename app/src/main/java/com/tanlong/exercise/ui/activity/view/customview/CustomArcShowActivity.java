package com.tanlong.exercise.ui.activity.view.customview;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.fragment.ShowTipsFragment;
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
    @Bind(R.id.btn_help)
    Button btnHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_custom_arc_show);
        ButterKnife.bind(this);

        mTvTitle.setText(R.string.custom_arc_show_view);
        btnHelp.setVisibility(View.VISIBLE);
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

    @OnClick(R.id.btn_help)
    public void onClick() {
        StringBuilder sb = new StringBuilder();
        sb.append("1. 使用属性动画来实现动态改变效果\n")
                .append("2. 对当前数据（currentCount）设置ObjectAnimator, 即ObjectAnimator objectAnimator = ObjectAnimator.ofInt(Object, \"currentCount\", oldCount, currentCount);\n")
                .append("3. 调用setCurrentCount(int currentCount)方法时，该方法内部调用invalidate()方法触发View重绘，实现动态改变效果\n")
                .append("4. 要使用属性动画,属性前不要加m");
        ShowTipsFragment showTipsFragment = ShowTipsFragment.newInstance(sb.toString());
        showTipsFragment.show(getSupportFragmentManager(), "");
    }
}
