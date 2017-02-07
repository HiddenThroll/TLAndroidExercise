package com.tanlong.exercise.ui.activity.view.listview;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.tanlong.exercise.R;
import com.tanlong.exercise.model.entity.IMenuItem;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.adapter.MenuAdapter;
import com.tanlong.exercise.ui.fragment.ShowTipsFragment;
import com.tanlong.exercise.ui.view.swiperefresh.CustomSwipeRefreshLayout;
import com.tanlong.exercise.util.DisplayUtil;
import com.tanlong.exercise.util.LogTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 实现下拉刷新、上拉加载、侧滑菜单的ListView
 * Created by 龙 on 2016/6/28.
 */
public class SwipeRefreshLoadMoreListViewActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.slv_swipe_menu)
    SwipeMenuListView mSwipeMenuListView;
    @Bind(R.id.srl_custom_swipe_refresh)
    CustomSwipeRefreshLayout mCustomSwipeRefresh;
    @Bind(R.id.btn_help)
    Button btnHelp;

    private List<IMenuItem> mListItems;
    private MenuAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_swipe_refresh_load_more_list_view);
        ButterKnife.bind(this);

        initVariable();
        initView();
        initListener();
        bindData();
    }

    private void initVariable() {
        mListItems = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            IMenuItem menuItem = new IMenuItem("item " + i, i % 2);
            mListItems.add(menuItem);
        }
    }

    private void initView() {
        mTvTitle.setText(R.string.list_view_swipe_refresh_load_more);
        btnHelp.setVisibility(View.VISIBLE);
        //TODO 设置CustomSwipeRefresh
        mCustomSwipeRefresh.initColor();

        //TODO 设置SwipeMenuListView
        setMenuItem();
    }

    private void initListener() {
        mCustomSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeMenuListView.smoothCloseMenu();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mCustomSwipeRefresh.setRefreshing(false);
                        showShortMessage("刷新完毕");
                    }
                }, 2000);
            }
        });
        mCustomSwipeRefresh.setmLoadMoreListener(new CustomSwipeRefreshLayout.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mSwipeMenuListView.smoothCloseMenu();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int size = mListItems.size();
                        for (int i = size; i < size + 10; i++) {
                            IMenuItem menuItem = new IMenuItem("item " + i, i % 2);
                            mListItems.add(menuItem);
                        }

                        if (size >= 40) {
                            mCustomSwipeRefresh.setmCanLoadMore(false);
                            showShortMessage("已加载完所有数据");
                        } else {
                            mCustomSwipeRefresh.setmCanLoadMore(true);
                        }

                        mCustomSwipeRefresh.setLoading(false);
                        mAdapter.notifyDataSetChanged();
                    }
                }, 2000);
            }
        });

        mSwipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                LogTool.e(TAG, "view type is " + menu.getViewType());
                switch (index) {
                    case 0:
                        showShortMessage("菜单 0");
                        break;
                    case 1:
                        showShortMessage("菜单 1");
                        break;
                }
                return false;
            }
        });
        mSwipeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showShortMessage("click item " + position);
            }
        });

    }

    private void bindData() {
        mAdapter = new MenuAdapter(this, mListItems, R.layout.item_menu);
        mSwipeMenuListView.setAdapter(mAdapter);
    }

    private void setMenuItem() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // 文字菜单项
                SwipeMenuItem textItem = new SwipeMenuItem(SwipeRefreshLoadMoreListViewActivity.this);
                int bgColor = ContextCompat.getColor(SwipeRefreshLoadMoreListViewActivity.this,
                        R.color.color_86d0ab);
                int textColor = ContextCompat.getColor(SwipeRefreshLoadMoreListViewActivity.this,
                        R.color.color_white);
                textItem.setBackground(new ColorDrawable(bgColor));//设置背景色
                textItem.setWidth(DisplayUtil.dip2px(SwipeRefreshLoadMoreListViewActivity.this, 64));
                textItem.setTitle("Item 0");
                textItem.setTitleColor(textColor);
                textItem.setTitleSize(16);

                // 图标菜单项
                SwipeMenuItem iconItem = new SwipeMenuItem(SwipeRefreshLoadMoreListViewActivity.this);
                bgColor = ContextCompat.getColor(SwipeRefreshLoadMoreListViewActivity.this,
                        R.color.colorPrimary);
                textColor = ContextCompat.getColor(SwipeRefreshLoadMoreListViewActivity.this,
                        R.color.color_white);
                iconItem.setBackground(new ColorDrawable(bgColor));
                iconItem.setWidth(DisplayUtil.dip2px(SwipeRefreshLoadMoreListViewActivity.this, 64));
                iconItem.setTitle("Item 1");
                iconItem.setTitleColor(textColor);
                iconItem.setTitleSize(16);

                switch (menu.getViewType()) {
                    case 0:
                        menu.addMenuItem(textItem);
                        break;
                    case 1:
                        menu.addMenuItem(textItem);
                        menu.addMenuItem(iconItem);
                        break;
                }
            }
        };
        mSwipeMenuListView.setMenuCreator(creator);
        mSwipeMenuListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
    }

    @OnClick(R.id.iv_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.btn_help)
    public void onClick() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("1. ListView.setOnScrollListener()设置滚动监听实现上滑加载更多，详见上一小节\n")
                .append("2. 使用SwipeRefreshLayout实现下拉刷新\n")
                .append("3. 使用SwipeMenuListView实现侧滑菜单\n");
        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }
}
