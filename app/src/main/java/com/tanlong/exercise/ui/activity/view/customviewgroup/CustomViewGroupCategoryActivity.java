package com.tanlong.exercise.ui.activity.view.customviewgroup;

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
 * Created by Administrator on 2016/6/27.
 */
public class CustomViewGroupCategoryActivity extends BaseActivity {

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

        mTvTitle.setText(R.string.custom_view_group_exercise);

        String[] items = getResources().getStringArray(R.array.custom_view_group_category);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_category, items);
        mLvCategory.setAdapter(adapter);
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
                intent.setClass(this, VerticalLinearLayoutActivity.class);
                break;
            case 1:
                intent.setClass(this, ViewDragHelperActivity.class);
                break;
            case 2:
                intent.setClass(this, LeftDrawerLayoutActivity.class);
                break;
            case 3:
                intent.setClass(this, GestureLockActivity.class);
                break;
            case 4:
                intent.setClass(this, ScrollConflictEx1Activity.class);
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
