package com.tanlong.exercise.ui.activity.view.recyclerview;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.tanlong.exercise.ui.activity.view.recyclerview.divider.GridDividerItemDecoration;
import com.tanlong.exercise.ui.activity.view.recyclerview.divider.LinearDividerItemDecoration;
import com.tanlong.exercise.ui.activity.view.recyclerview.divider.StaggeredDividerItemDecoration;
import com.tanlong.exercise.ui.fragment.ShowTipsFragment;
import com.tanlong.exercise.util.ToastHelp;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 龙 on 2017/2/20.
 */

public class RecyclerViewItemDecorationActivity extends BaseActivity {

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
    RecyclerView.ItemDecoration mItemDecoration;//本例中只使用一个ItemDecoration

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
        tvTitle.setText(R.string.recycler_view_item_decoration);
        btnHelp.setVisibility(View.VISIBLE);

        mAdapter = new SimpleRecyclerViewAdapter(this, mDatas);
        RecyclerView.LayoutManager layoutManager = initLayoutManager(MODE_LIST, MODE_VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);//设置布局管理器
        mItemDecoration = initItemDecoration(MODE_LIST, MODE_VERTICAL);
        mRecyclerView.addItemDecoration(mItemDecoration);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setmOnItemClickListener(new SimpleRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                ToastHelp.showShortMsg(RecyclerViewItemDecorationActivity.this, "onClick " + mDatas.get(position));
            }

            @Override
            public void onLongClick(View view, int position) {
                ToastHelp.showShortMsg(RecyclerViewItemDecorationActivity.this, "onLongClick " + mDatas.get(position));
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

    private RecyclerView.ItemDecoration initItemDecoration(int layoutMode, int orientation) {
        RecyclerView.ItemDecoration itemDecoration = null;
        int color = ContextCompat.getColor(this, R.color.color_black);
        switch (layoutMode) {
            case MODE_LIST:
                if (orientation == MODE_VERTICAL) {
                    itemDecoration = new LinearDividerItemDecoration(0, 8, color);
                } else {
                    itemDecoration = new LinearDividerItemDecoration(8, 0, color);
                }
                break;
            case MODE_GRID:
                itemDecoration = new GridDividerItemDecoration(8, 8, color);
                break;
            case MODE_STAGGER:
                itemDecoration = new StaggeredDividerItemDecoration(0, 8);
                break;
        }
        return itemDecoration;
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
            layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        } else if (orientation == MODE_HORIZONTAL) {
            layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.HORIZONTAL, false);
        }
        return layoutManager;
    }

    private RecyclerView.LayoutManager initStaggerLayoutManager(int orientation) {
        RecyclerView.LayoutManager layoutManager = null;
        if (orientation == MODE_VERTICAL) {
            layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        } else if (orientation == MODE_HORIZONTAL) {
            layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
        }
        return layoutManager;
    }

    @OnClick({R.id.iv_back, R.id.btn_help, R.id.btn_list, R.id.btn_grid, R.id.btn_stagger, R.id.btn_vertical, R.id.btn_horizontal})
    public void onClick(View view) {
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
        RecyclerView.LayoutManager layoutManager = initLayoutManager(mCurMode, mCurOrientation);
        mRecyclerView.setLayoutManager(layoutManager);
        if (mItemDecoration != null) {//
            mRecyclerView.removeItemDecoration(mItemDecoration);
        }
        mItemDecoration = initItemDecoration(mCurMode, mCurOrientation);
        mRecyclerView.addItemDecoration(mItemDecoration);
    }

    private void showTips() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("RecyclerView的基本用法: \n")
                .append("1. RecyclerView.setLayoutManager(GridLayoutManager)设置网格布局管理器\n")
                .append("1.1 可以通过GridLayoutManager的构造方法设置网格总列数(spanCount)、垂直(GridLayoutManager.VERTICAL)或水平(GridLayoutManager.HORIZONTAL)布局、是否倒序显示数据(reverseLayout)\n")
                .append("2. RecyclerView.addItemDecoration()设置Item间的Divider，实现方法详见代码\n");

        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }

}
