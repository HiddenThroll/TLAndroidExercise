package com.tanlong.exercise.ui.activity.view.recyclerview;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.model.entity.NewsItem;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.activity.view.recyclerview.adapter.NewsSingleAdapter;
import com.tanlong.exercise.ui.activity.view.recyclerview.divider.LinearDividerItemDecoration;
import com.tanlong.exercise.ui.activity.view.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.tanlong.exercise.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 龙 on 2017/3/3.
 */

public class RecyclerViewRefreshActivity extends BaseActivity {

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

    List<NewsItem> mDatas;
    NewsSingleAdapter mRealAdapter;
    HeaderAndFooterWrapper<NewsItem> mAdapter;
    View mHeaderView, mFooterView;
    @Bind(R.id.btn_list)
    Button btnAddItem;
    @Bind(R.id.btn_grid)
    Button btnRemoveItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recycler_view);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    private void initData() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mDatas.add(new NewsItem(String.valueOf(i), "一段内容", ""));
        }
    }

    private void initView() {
        tvTitle.setText(R.string.recycler_view_refresh);
        btnHelp.setVisibility(View.VISIBLE);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
//        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        int space = DisplayUtil.dip2px(this, 8);
        RecyclerView.ItemDecoration itemDecoration = new LinearDividerItemDecoration(0, space,
                ContextCompat.getColor(this, R.color.color_86d0ab));

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(itemDecoration);
        mRealAdapter = new NewsSingleAdapter(this, mDatas, R.layout.item_news_type_2);
        mAdapter = new HeaderAndFooterWrapper<>(mRealAdapter);
        recyclerView.setAdapter(mAdapter);

        initHeaderAndFooter();
    }

    private void initHeaderAndFooter() {
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.layout_refresh_item, null);
        mFooterView = LayoutInflater.from(this).inflate(R.layout.layout_refresh_item, null);
        TextView tvHeader = (TextView) mHeaderView.findViewById(R.id.tv_refresh_tips);
        tvHeader.setText("正在刷新，请稍候...");
        TextView tvFooter = (TextView) mFooterView.findViewById(R.id.tv_refresh_tips);
        tvFooter.setText("正在加载，请稍候...");
    }

    @OnClick({R.id.iv_back, R.id.btn_help, R.id.btn_list, R.id.btn_grid})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                break;
            case R.id.btn_list:
                addHeaderView();
//                addFooterView();
                break;
            case R.id.btn_grid:
                removeHeaderView();
//                removeFooterView();
                break;
        }
    }

    private void addHeaderView() {
        mAdapter.addHeaderView(mHeaderView);
    }

    private void removeHeaderView() {
        mAdapter.deleteHeaderView(mHeaderView);
    }

    private void addFooterView() {
        mAdapter.addFootView(mFooterView);
        recyclerView.smoothScrollToPosition(mAdapter.getItemCount());
    }

    private void removeFooterView() {
        mAdapter.deleteFooterView(mFooterView);
    }
}
