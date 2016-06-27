package com.tanlong.exercise.ui.activity.view.customview;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.view.customview.CustomVolumeBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/17.
 */
public class CustomVolumeBarActivity extends BaseActivity {

    @Bind(R.id.cvb_custom_volume_bar)
    CustomVolumeBar mCustomVolumeBar;
    @Bind(R.id.btn_Reduce_Bar)
    Button mReduce;
    @Bind(R.id.btn_Add_Bar)
    Button mAdd;
    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_custom_volume_bar);
        ButterKnife.bind(this);

        mTvTitle.setText(R.string.custom_volume_bar);
    }

    @OnClick(R.id.btn_Add_Bar)
    public void addBar() {
        mCustomVolumeBar.addBlock();
    }

    @OnClick(R.id.btn_Reduce_Bar)
    public void reduceBar() {
        mCustomVolumeBar.reduceBlock();
    }

    @OnClick(R.id.iv_back)
    public void onBack() {
        finish();
    }

}
