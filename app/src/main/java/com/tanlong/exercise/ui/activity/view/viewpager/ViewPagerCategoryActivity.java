package com.tanlong.exercise.ui.activity.view.viewpager;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by 龙 on 2017/2/8.
 */

public class ViewPagerCategoryActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.lv_activity_category)
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

    }
}
