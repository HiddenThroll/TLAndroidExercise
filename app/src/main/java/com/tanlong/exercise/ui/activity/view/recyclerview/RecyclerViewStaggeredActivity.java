package com.tanlong.exercise.ui.activity.view.recyclerview;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.activity.view.recyclerview.adapter.SimpleRecyclerViewAdapter;
import com.tanlong.exercise.ui.activity.view.recyclerview.divider.StaggeredDividerItemDecoration;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;
import com.tanlong.exercise.util.DisplayUtil;
import com.tanlong.exercise.util.ToastHelp;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 龙 on 2017/2/20.
 */

public class RecyclerViewStaggeredActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_help)
    Button btnHelp;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    List<String> mDatas;
    @BindView(R.id.btn_list)
    Button btnAddItem;
    @BindView(R.id.btn_grid)
    Button btnRemoveItem;

    SimpleRecyclerViewAdapter adapter;
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
        int random = 0;
        for (int i = 0; i < 20; i++) {
            random = (int) (Math.random() * 100);
            if (random % 2 == 0) {
                mDatas.add("item " + i + " 一段随机添加的数据，用于展示瀑布流效果");
            } else {
                mDatas.add("item " + i);
            }
        }
    }

    private void initView() {
        tvTitle.setText(R.string.recycler_view_staggered);
        btnHelp.setVisibility(View.VISIBLE);


        adapter = new SimpleRecyclerViewAdapter(this, mDatas);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);//设置布局管理器

        int spac = DisplayUtil.dip2px(this, 8);
        RecyclerView.ItemDecoration itemDecoration = new StaggeredDividerItemDecoration(spac, spac,
                ContextCompat.getColor(this, R.color.color_fb9b10));
        mRecyclerView.addItemDecoration(itemDecoration);//设置Divider
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置添加/移除Item时动画
        mRecyclerView.setAdapter(adapter);
        adapter.setmOnItemClickListener(new SimpleRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                ToastHelp.showShortMsg(RecyclerViewStaggeredActivity.this, "onClick " + mDatas.get(position));
            }

            @Override
            public void onLongClick(View view, int position) {
                ToastHelp.showShortMsg(RecyclerViewStaggeredActivity.this, "onLongClick " + mDatas.get(position));
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.btn_help, R.id.btn_list, R.id.btn_grid})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                showTips();
                break;
            case R.id.btn_list:
                if (mDatas.size() > 1) {
                    mDatas.add(1, "Item");
                    adapter.notifyDataSetChanged();
                } else {
                    mDatas.add(0, "Item");
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.btn_grid:
                if (mDatas.size() > 0) {
                    mDatas.remove(0);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private void showTips() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("RecyclerView的基本用法: \n")
                .append("1. RecyclerView.setLayoutManager(StaggeredGridLayoutManager)设置网格布局管理器\n")
                .append("1.1 可以通过StaggeredGridLayoutManager的构造方法设置spanCount和方向，当方向为水平时,spanCount是总行数，当方向为垂直时，spanCount是总列数\n")
                .append("2. RecyclerView.addItemDecoration()设置Item间的Divider，实现方法详见代码\n");

        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }

}
