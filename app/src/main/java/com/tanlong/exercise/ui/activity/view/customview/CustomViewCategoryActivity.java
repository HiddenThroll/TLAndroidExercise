package com.tanlong.exercise.ui.activity.view.customview;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 *
 * Created by Administrator on 2016/6/26.
 */
public class CustomViewCategoryActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.lv_activity_category)
    ListView mLvCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);

        initView();
        String[] items = getResources().getStringArray(R.array.custom_view_category);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_category, items);
        mLvCategory.setAdapter(adapter);
    }

    public void initView() {
        mTvTitle.setText(R.string.custom_view_exercise);
    }

    @OnClick(R.id.iv_back)
    public void onBack() {
        finish();
    }

    @OnItemClick(R.id.lv_activity_category)
    public void onItemClick(int position) {
        Intent intent = new Intent();

        switch (position) {
            case 0:
                intent.setClass(this, CustomTitleViewActivity.class);
                break;
            case 1:
                intent.setClass(this, CustomImageViewActivity.class);
                break;
            case 2:
                intent.setClass(this, CustomCircleViewActivity.class);
                break;
            case 3:
                intent.setClass(this, CustomVolumeBarActivity.class);
                break;
            case 4:
                intent.setClass(this, CustomArcShowActivity.class);
                break;
            case 5:
                intent.setClass(this, ScheduleChartActivity.class);
                break;
            case 6:
                intent.setClass(this, CustomSimpleClockActivity.class);
                break;
            case 7:
                intent.setClass(this, CustomSignViewActivity.class);
                break;
            case 8:
                intent.setClass(this, CustomAudioPulsationActivity.class);
                break;
            case 9:
                intent.setClass(this, SimpleCircleActivity.class);
                break;
            case 10:
                intent.setClass(this, SimpleVideoControlActivity.class);
                break;
            default:
                break;
        }

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            showShortMessage(R.string.no_available_activity);
        }
    }
}
