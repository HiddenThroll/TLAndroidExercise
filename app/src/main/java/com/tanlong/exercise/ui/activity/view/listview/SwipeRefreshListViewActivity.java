package com.tanlong.exercise.ui.activity.view.listview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.fragment.ShowTipsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 使用SwipeRefreshLayout实现ListView的下拉刷新
 * Created by 龙 on 2016/6/28.
 */
public class SwipeRefreshListViewActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.lv_swipe_refresh)
    ListView mLvSwipeRefresh;
    @Bind(R.id.srl_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.btn_help)
    Button btnHelp;

    private List<String> mItems;
    ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_swipe_refresh_list_view);
        ButterKnife.bind(this);

        mTvTitle.setText(R.string.list_view_swipe_refresh);
        btnHelp.setVisibility(View.VISIBLE);

        mItems = new ArrayList<>();
        mItems.add("item 0");
        mAdapter = new ArrayAdapter<>(this, R.layout.item_category, mItems);
        mLvSwipeRefresh.setAdapter(mAdapter);

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int size = mItems.size();
                        for (int i = size; i < size + 10; i++) {
                            mItems.add("item " + i);
                        }
                        mAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.btn_help)
    public void onClick() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("1. 使用SwipeRefreshLayout实现下拉刷新\n")
                .append("2. SwipeRefreshLayout.setColorSchemeResources(int... colors)设置刷新效果颜色\n")
                .append("3. SwipeRefreshLayout.setOnRefreshListener设置刷新监听\n")
                .append("4. SwipeRefreshLayout.setRefreshing(boolean)设置显示/隐藏刷新效果");

        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }
}
