package com.tanlong.exercise.ui.activity.view.viewpager;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.adapter.pageradapter.IndexPagerAdapter;
import com.tanlong.exercise.ui.fragment.ShowTipsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ViewPager + TabLayout 实现滑动指示器
 * Created by 龙 on 2017/2/10.
 */

public class ViewPagerTabLayoutActivity extends BaseActivity {
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;
    @Bind(R.id.vp_custom_indicator)
    ViewPager vpCustomIndicator;

    List<View> viewList;
    IndexPagerAdapter pagerAdapter;
    @Bind(R.id.tl_title_container)
    TabLayout tabContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_viewpager_custom_indicator);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        tvTitle.setText(R.string.viewpager_custom_indicator);
        btnHelp.setVisibility(View.VISIBLE);

        LayoutInflater inflater = LayoutInflater.from(this);
        viewList = new ArrayList<>();
        viewList.add(inflater.inflate(R.layout.layout_index_1, null));
        viewList.add(inflater.inflate(R.layout.layout_index_2, null));
        viewList.add(inflater.inflate(R.layout.layout_index_3, null));
        viewList.add(inflater.inflate(R.layout.layout_index_4, null));
        viewList.add(inflater.inflate(R.layout.layout_index_1, null));
        viewList.add(inflater.inflate(R.layout.layout_index_2, null));
        viewList.add(inflater.inflate(R.layout.layout_index_3, null));
        viewList.add(inflater.inflate(R.layout.layout_index_4, null));

        pagerAdapter = new IndexPagerAdapter(viewList, this);
        vpCustomIndicator.setAdapter(pagerAdapter);

        tabContainer.setTabMode(TabLayout.MODE_SCROLLABLE);//设置可以滑动
        tabContainer.addTab(tabContainer.newTab().setText("标题 1"));//添加Tab选项卡
        tabContainer.addTab(tabContainer.newTab().setText("标题 2"));//添加Tab选项卡
        tabContainer.addTab(tabContainer.newTab().setText("标题 3"));//添加Tab选项卡
        tabContainer.addTab(tabContainer.newTab().setText("标题 4"));//添加Tab选项卡
        tabContainer.addTab(tabContainer.newTab().setText("标题 5"));//添加Tab选项卡
        tabContainer.addTab(tabContainer.newTab().setText("标题 6"));//添加Tab选项卡
        tabContainer.addTab(tabContainer.newTab().setText("标题 7"));//添加Tab选项卡
        tabContainer.addTab(tabContainer.newTab().setText("标题 8"));//添加Tab选项卡
        tabContainer.setupWithViewPager(vpCustomIndicator);//将ViewPager和TabLayout结合

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

        stringBuilder.append("1. 使用TabLayout实现ViewPager的滑动指示器: \n")
                .append("1.1 TabLayout.setTabMode(int mode)设置Tab模式,FIXED会显示所有Tab,不可滑动;SCROLLABLE可以滑动,可以设置较长的Tab\n")
                .append("1.2 TabLayout.addTab(TabLayout.newTab().setText(String))添加Tab选项卡并初始化标题\n")
                .append("1.3 ViewPager的Adapter中, 覆写getPageTitle()方法设置Tab标题\n")
                .append("1.4 TabLayout.setupWithViewPager(ViewPager)方法将ViewPager和TabLayout结合,必须在ViewPager.setAdapter()方法之后调用\n");

        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }

}
