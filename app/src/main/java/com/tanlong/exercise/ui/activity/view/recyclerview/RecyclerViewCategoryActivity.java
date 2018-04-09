package com.tanlong.exercise.ui.activity.view.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
 * RecyclerView练习目录
 * Created by 龙 on 2017/2/20.
 */

public class RecyclerViewCategoryActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_help)
    Button btnHelp;
    @BindView(R.id.lv_activity_category)
    ListView lvActivityCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);

        tvTitle.setText(R.string.recycler_view_exercise);

        String[] items = getResources().getStringArray(R.array.recycler_view_category);
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
                intent.setClass(this, RecyclerViewBaseUseActivity.class);
                break;
            case 1:
                intent.setClass(this, RecyclerViewItemDecorationActivity.class);
                break;
            case 2:
                intent.setClass(this, RecyclerViewMultiItemActivity.class);
                break;
            case 3:
                intent.setClass(this, RecyclerViewRefreshActivity.class);
                break;
            case 4:
                intent.setClass(this, RecyclerViewSectionActivity.class);
                break;
        }

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
