package com.tanlong.exercise.ui.activity.view.optimization;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.activity.view.image.CustomImageLoaderActivity;
import com.tanlong.exercise.ui.activity.view.image.LoadSuperBigImageActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class ViewOptimizationCategoryActivity extends BaseActivity {
    @BindView(R.id.lv_activity_category)
    ListView mLvCategory;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);

        initView();
        String[] items = getResources().getStringArray(R.array.view_optimization_category);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_category, items);
        mLvCategory.setAdapter(adapter);
    }

    public void initView() {
        mTvTitle.setText("View性能优化");
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
                intent.setClass(this, MergeExampleActivity.class);
                break;
            case 1:
                intent.setClass(this, ViewStubExampleActivity.class);
                break;
            case 2:
                intent.setClass(this, OverdrawBackgroundActivity.class);
                break;
            default:
                break;
        }

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            overridePendingTransition(R.anim.anim_activity_in_from_right, R.anim.anim_activity_out_to_left);
        } else {
            showShortMessage(R.string.no_available_activity);
        }
    }

}
