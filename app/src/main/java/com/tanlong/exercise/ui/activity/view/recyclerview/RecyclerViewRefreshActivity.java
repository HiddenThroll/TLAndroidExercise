package com.tanlong.exercise.ui.activity.view.recyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.model.entity.NewsItem;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.activity.view.recyclerview.adapter.NewsSingleAdapter;
import com.tanlong.exercise.ui.activity.view.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.tanlong.exercise.util.LogTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * RecyclerView实现下拉刷新、上拉加载更多
 * Created by 龙 on 2017/3/3.
 */

public class RecyclerViewRefreshActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;
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
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.srl_recycler)
    SwipeRefreshLayout srlRecycler;

    List<NewsItem> mDatas;
    NewsSingleAdapter mContentAdapter;
    OnRefreshListener mOnRefreshListener;
    OnLoadMoreListener mLoadMoreListener;

    HeaderAndFooterWrapper<NewsItem> mWrapperAdapter;
    View mFooterView;
    boolean isLoadingMore = false;

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

        setContentView(R.layout.activity_recycler_refresh);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    private void initData() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mDatas.add(new NewsItem("标题" + i, "内容" + i, ""));
        }
    }

    private void initView() {
        btnHelp.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.recycler_view_refresh);

        mContentAdapter = new NewsSingleAdapter(this, mDatas, R.layout.item_news_type_2);
        mWrapperAdapter = new HeaderAndFooterWrapper<>(mContentAdapter);
        recyclerView.setLayoutManager(initLayoutManager(mCurMode, mCurOrientation));
        recyclerView.setAdapter(mWrapperAdapter);
        mFooterView = LayoutInflater.from(this).inflate(R.layout.layout_refresh_item, null);

        mLoadMoreListener = new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isLoadingMore = false;
                        mWrapperAdapter.deleteFooterView(mFooterView);
                    }
                }, 2000);
            }
        };
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int totalCount = layoutManager.getItemCount();
                int lastVisiableItemPosition = 0;
                if (layoutManager instanceof LinearLayoutManager) {
                    lastVisiableItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                } else if (layoutManager instanceof GridLayoutManager) {
                    lastVisiableItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    int[] positions = null;
                    positions = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
                    lastVisiableItemPosition = positions[0];
                }
                if (totalCount - lastVisiableItemPosition <= 5 && !isLoadingMore) {
                    isLoadingMore = true;
                    mWrapperAdapter.addFootView(mFooterView);
                    mLoadMoreListener.onLoadMore();
                }
            }
        });

        mOnRefreshListener = new OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDatas.clear();
                        for (int i = 0; i < 20; i++) {
                            mDatas.add(new NewsItem("标题" + i, "内容" + i, ""));
                        }
                        mContentAdapter.notifyDataSetChanged();
                        srlRecycler.setRefreshing(false);
                    }
                }, 2000);
            }
        };
        srlRecycler.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mOnRefreshListener.onRefresh();
            }
        });
    }


    @OnClick({R.id.iv_back, R.id.btn_help, R.id.btn_list, R.id.btn_grid, R.id.btn_stagger, R.id.btn_vertical, R.id.btn_horizontal})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
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

        recyclerView.setLayoutManager(initLayoutManager(mCurMode, mCurOrientation));
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

    public interface OnRefreshListener {
        void onRefresh();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
