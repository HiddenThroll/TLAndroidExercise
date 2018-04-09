package com.tanlong.exercise.ui.activity.view.recyclerview;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;
import com.tanlong.exercise.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * RecyclerView实现多Item布局
 * Created by 龙 on 2017/3/2.
 */

public class RecyclerViewMultiItemActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_help)
    Button btnHelp;
    @BindView(R.id.ll_operation)
    LinearLayout llOperation;
    @BindView(R.id.ll_operation2)
    LinearLayout llOperation2;
    @BindView(R.id.recycler_view)
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
        llOperation2.setVisibility(View.GONE);
        btnHelp.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.recycler_view_multi_item);
        mAdapter = new NewsMultiAdapter(this, mDatas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new LinearDividerItemDecoration(0, DisplayUtil.dip2px(this, 8),
                ContextCompat.getColor(this, R.color.colorPrimary)));
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
                showTips();
                break;
        }
    }

    private void showTips() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("1. RecyclerView实现多Item布局的原理为：\n")
                .append("1.1 Adapter中getItemViewType(int position)方法根据Item的位置和内容，返回对应的布局文件ID\n")
                .append("1.2 Adapter中onCreateViewHolder(ViewGroup parent, int viewType)方法根据viewType生成对应的ViewHolder\n")
                .append("1.3 Adapter中onBindViewHolder(ViewHolder holder, int position)方法根据不同的位置，进行对应ViewHolder的View绑定和赋值\n")
                .append("2. 本例已对ViewHolder和Adapter进行封装，详见代码\n");

        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }
}
