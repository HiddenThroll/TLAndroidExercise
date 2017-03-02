package com.tanlong.exercise.ui.activity.view.recyclerview;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.model.entity.NewsItem;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.activity.view.recyclerview.adapter.NewsMultiAdapter;
import com.tanlong.exercise.ui.activity.view.recyclerview.divider.LinearDividerItemDecoration;
import com.tanlong.exercise.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 龙 on 2017/3/2.
 */

public class RecyclerViewMultiItemActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;
    @Bind(R.id.ll_operation)
    LinearLayout llOperation;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    NewsMultiAdapter mAdapter;
    List<NewsItem> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recycler_view);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    private void initView() {
        llOperation.setVisibility(View.GONE);
        btnHelp.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.recycler_view_multi_item);
        mAdapter = new NewsMultiAdapter(this, mDatas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new LinearDividerItemDecoration(0, DisplayUtil.dip2px(this, 8)));
        recyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int random = (int) (Math.random() * 100);
            if (random % 3 == 0) {
                mDatas.add(new NewsItem("标题 + 大图形式 ", ""));
            } else if (random % 3 == 1) {
                mDatas.add(new NewsItem("左侧图片 + 右侧标题 + 描述字段 ", "一段内容", ""));
            } else if (random % 3 == 2) {
                mDatas.add(new NewsItem("标题 + 3副图片 ", "", "", ""));
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.btn_help})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                break;
        }
    }
}
