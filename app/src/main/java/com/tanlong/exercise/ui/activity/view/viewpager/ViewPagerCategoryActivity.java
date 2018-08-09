package com.tanlong.exercise.ui.activity.view.viewpager;

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
 * Created by 龙 on 2017/2/8.
 */

public class ViewPagerCategoryActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lv_activity_category)
    ListView lvActivityCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        tvTitle.setText(R.string.viewpager_exercise);
        String[] items = getResources().getStringArray(R.array.viewpager_category);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_category, items);
        lvActivityCategory.setAdapter(adapter);
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    @OnItemClick(R.id.lv_activity_category)
    public void onItemClick(int position) {
        Intent intent = new Intent();
        switch (position) {
            case 0:
                intent.setClass(this, ViewPagerWelcomeActivity.class);
                break;
            case 1:
                intent.setClass(this, ViewPagerFakeUnlimitedScrollActivity.class);
                break;
            case 2:
                intent.setClass(this, ViewPagerRealUnlimitedScrollActivity.class);
                break;
            case 3:
                intent.setClass(this, ViewPagerTabLayoutActivity.class);
                break;
            case 4:
                intent.setClass(this, ViewPagerFragmentPagerAdapterActivity.class);
                break;
            case 5:
                intent.setClass(this, ViewPagerFragmentPagerNotifyAdapterActivity.class);
                break;
            case 6:
                intent.setClass(this, ViewPagerFragmentStatePagerAdapterActivity.class);
                break;
            case 7:
                intent.setClass(this, ViewPagerTransformActivity.class);
                break;
            default:
                break;
        }

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
