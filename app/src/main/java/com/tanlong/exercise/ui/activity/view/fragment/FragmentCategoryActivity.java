package com.tanlong.exercise.ui.activity.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.activity.view.fragment.dataload.LoadDataActivity;
import com.tanlong.exercise.ui.activity.view.fragment.datatransfer.ListArticleActivity;
import com.tanlong.exercise.ui.activity.view.fragment.lazyload.FragmentLazyLoadActivity;
import com.tanlong.exercise.ui.activity.view.fragment.overlap.OverlapFragmentActivity;
import com.tanlong.exercise.util.ToastHelp;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/26.
 */

public class FragmentCategoryActivity extends BaseActivity {

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

        tvTitle.setText(R.string.fragment_exercise);

        String[] items = getResources().getStringArray(R.array.fragment_category);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_category, items);
        lvActivityCategory.setAdapter(adapter);
        lvActivityCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                switch (position) {
                    case 0:
                        intent.setClass(FragmentCategoryActivity.this, DialogFragmentActivity.class);
                        break;
                    case 1:
                        intent.setClass(FragmentCategoryActivity.this, ListArticleActivity.class);
                        break;
                    case 2:
                        intent.setClass(FragmentCategoryActivity.this, LoadDataActivity.class);
                        break;
                    case 3:
                        intent.setClass(FragmentCategoryActivity.this, OverlapFragmentActivity.class);
                        break;
                    case 4:
                        intent.setClass(FragmentCategoryActivity.this, FragmentLazyLoadActivity.class);
                        break;
                    default:
                        break;
                }

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    ToastHelp.showShortMsg(FragmentCategoryActivity.this, R.string.no_available_activity);
                }
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
