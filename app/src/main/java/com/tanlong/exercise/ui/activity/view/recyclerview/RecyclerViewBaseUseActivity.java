package com.tanlong.exercise.ui.activity.view.recyclerview;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.activity.view.recyclerview.adapter.SimpleRecyclerViewAdapter;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;
import com.tanlong.exercise.util.ToastHelp;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 演示RecyclerView的基本用法，包括 线性布局、网格布局、瀑布流布局
 * Created by 龙 on 2017/2/20.
 */

public class RecyclerViewBaseUseActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    List<String> mDatas;
    @Bind(R.id.btn_list)
    Button btnList;
    @Bind(R.id.btn_grid)
    Button btnGrid;
    @Bind(R.id.btn_stagger)
    Button btnStagger;
    @Bind(R.id.btn_vertical)
    Button btnVertical;
    @Bind(R.id.btn_horizontal)
    Button btnHorizontal;

    SimpleRecyclerViewAdapter mAdapter;

    private final int MODE_LIST = 1;
    private final int MODE_GRID = 2;
    private final int MODE_STAGGER = 3;

    private final int MODE_HORIZONTAL = 4;
    private final int MODE_VERTICAL = 5;

    private int mCurMode = MODE_LIST;
    private int mCurOrientation = MODE_VERTICAL;

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
            mDatas.add("item " + i);
        }
    }

    private void initView() {
        tvTitle.setText(R.string.recycler_view_base_use);
        btnHelp.setVisibility(View.VISIBLE);

        mAdapter = new SimpleRecyclerViewAdapter(this, mDatas);
        RecyclerView.LayoutManager layoutManager = initLayoutManager(MODE_LIST, MODE_VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);//设置布局管理器
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setmOnItemClickListener(new SimpleRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                ToastHelp.showShortMsg(RecyclerViewBaseUseActivity.this, "onClick " + mDatas.get(position));
            }

            @Override
            public void onLongClick(View view, int position) {
                ToastHelp.showShortMsg(RecyclerViewBaseUseActivity.this, "onLongClick " + mDatas.get(position));
            }
        });
    }

    private RecyclerView.LayoutManager initLayoutManager(int layoutMode, int orientation) {
        RecyclerView.LayoutManager layoutManager = null;
        switch (layoutMode) {
            case MODE_LIST:
                layoutManager = initLinearLayoutManager(orientation);
                break;
            case MODE_GRID:
                layoutManager = initGridLayoutManager(orientation);
                break;
            case MODE_STAGGER:
                layoutManager = initStaggerLayoutManager(orientation);
                break;
        }

        return layoutManager;
    }

    private RecyclerView.LayoutManager initLinearLayoutManager(int orientation) {
        RecyclerView.LayoutManager layoutManager = null;
        if (orientation == MODE_VERTICAL) {
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        } else if (orientation == MODE_HORIZONTAL) {
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        }
        return layoutManager;
    }

    private RecyclerView.LayoutManager initGridLayoutManager(int orientation) {
        RecyclerView.LayoutManager layoutManager = null;
        if (orientation == MODE_VERTICAL) {
            layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        } else if (orientation == MODE_HORIZONTAL) {
            layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);
        }
        return layoutManager;
    }

    private RecyclerView.LayoutManager initStaggerLayoutManager(int orientation) {
        RecyclerView.LayoutManager layoutManager = null;
        if (orientation == MODE_VERTICAL) {
            layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        } else if (orientation == MODE_HORIZONTAL) {
            layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        }
        return layoutManager;
    }

    @OnClick({R.id.iv_back, R.id.btn_help, R.id.btn_list, R.id.btn_grid, R.id.btn_stagger,
            R.id.btn_vertical, R.id.btn_horizontal})
    public void onClick(View view) {
        RecyclerView.LayoutManager layoutManager;
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                showTips();
                break;
            case R.id.btn_list:
                mCurMode = MODE_LIST;
                break;
            case R.id.btn_grid:
                mCurMode = MODE_GRID;
                break;
            case R.id.btn_stagger:
                mCurMode = MODE_STAGGER;
                break;
            case R.id.btn_vertical:
                mCurOrientation = MODE_VERTICAL;
                break;
            case R.id.btn_horizontal:
                mCurOrientation = MODE_HORIZONTAL;
                break;
        }
        layoutManager = initLayoutManager(mCurMode, mCurOrientation);
        mRecyclerView.setLayoutManager(layoutManager);
    }


    private void showTips() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("RecyclerView的基本用法: \n")
                .append("1. RecyclerView.setLayoutManager()设置布局管理器，控制RecyclerView的显示方式。\n")
                .append("1.1 通过LinearLayoutManager的构造方法设置垂直(LinearLayoutManager.VERTICAL)或水平(LinearLayoutManager.HORIZONTAL)布局、是否倒序显示数据(reverseLayout)\n")
                .append("1.2 通过GridLayoutManager的构造方法设置网格总列数(spanCount)、垂直(GridLayoutManager.VERTICAL)或水平(GridLayoutManager.HORIZONTAL)布局、是否倒序显示数据(reverseLayout)\n")
                .append("1.3 通过StaggeredGridLayoutManager的构造方法设置spanCount和方向，当方向为水平时,spanCount是总行数，当方向为垂直时，spanCount是总列数\n")
                .append("2. 通过接口的方式，在Adapter中设置单击监听和长按监听\n");

        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }

}
