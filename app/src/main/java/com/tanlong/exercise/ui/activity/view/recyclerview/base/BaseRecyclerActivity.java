package com.tanlong.exercise.ui.activity.view.recyclerview.base;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.activity.view.recyclerview.layoutmanager.ILayoutManager;
import com.tanlong.exercise.ui.activity.view.recyclerview.layoutmanager.MyLinearLayoutManager;
import com.tanlong.exercise.ui.activity.view.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.tanlong.exercise.ui.activity.view.recyclerview.wrapper.PtrRecyclerLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 龙 on 2017/3/14.
 */

public class BaseRecyclerActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;
    @Bind(R.id.ptr_recycler)
    PtrRecyclerLayout ptrRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base_recycler_view);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        btnHelp.setVisibility(View.VISIBLE);
        ptrRecycler.setLayoutManager(new MyLinearLayoutManager(this));//默认线性布局
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

    /**
     * 设置Adapter
     * @param adapter
     */
    public void setAdapter(HeaderAndFooterWrapper adapter) {
        ptrRecycler.setAdapter(adapter);
    }

    /**
     * 设置刷新监听，将使能下拉刷新
     * @param listener
     */
    public void setRefreshListener(PtrRecyclerLayout.OnRefreshListener listener) {
        enablePullToRefresh(true);//设置监听，认为打开下拉刷新
        ptrRecycler.setmRefreshListener(listener);
    }

    /**
     * 设置加载更多监听，将使能加载更多
     * @param listener
     */
    public void setLoadMoreListener(PtrRecyclerLayout.OnLoadMoreListener listener) {
        enableLoadMore(true);
        ptrRecycler.setmLoadMoreListener(listener);
    }

    /**
     * 设置布局管理器
     * @param layoutManager
     */
    public void setLayoutManager(ILayoutManager layoutManager) {
        ptrRecycler.setLayoutManager(layoutManager);
    }

    /**
     * 是否使能加载更多，默认否
     * @param enable
     */
    public void enableLoadMore(boolean enable) {
        ptrRecycler.enableLoadMore(enable);
    }

    /**
     * 是否使能下拉刷新，默认是
     * @param enable
     */
    public void enablePullToRefresh(boolean enable) {
        ptrRecycler.enablePullToRefresh(enable);
    }

    /**
     * 停止下拉刷新/加载更多
     */
    public void onRefreshComplete() {
        ptrRecycler.onRefreshCompleted();
    }

    public void setTitle(String content) {
        tvTitle.setText(content);
    }

    public void showTips() {

    }
}
