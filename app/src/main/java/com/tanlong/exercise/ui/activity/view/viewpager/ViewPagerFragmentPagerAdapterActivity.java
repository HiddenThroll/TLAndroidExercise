package com.tanlong.exercise.ui.activity.view.viewpager;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.activity.view.viewpager.tabcontent.ContentOneFragment;
import com.tanlong.exercise.ui.activity.view.viewpager.adapter.fragmentpageradapter.SimpleFragmentPagerAdapter;
import com.tanlong.exercise.ui.fragment.ShowTipsFragment;
import com.tanlong.exercise.util.LogTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ViewPager+Fragment+FragmentPagerAdapter实现APP首页Tab效果
 * Created by 龙 on 2017/2/15.
 */

public class ViewPagerFragmentPagerAdapterActivity extends BaseActivity implements ContentOneFragment.OnRefreshFragment{

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;
    @Bind(R.id.vp_tab_content)
    ViewPager vpTabContent;
    @Bind(R.id.tl_item_container)
    TabLayout tabContainer;
    @Bind(R.id.et_update_content)
    EditText etUpdateContent;

    SimpleFragmentPagerAdapter mAdapter;
    List<Fragment> fragmentList;

    private final int POSITION_1 = 0;
    private final int POSITION_2 = 1;
    private final int POSITION_3 = 2;
    private final int POSITION_4 = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_viewpager_tab);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        tvTitle.setText(R.string.viewpager_fragment_fragmentpageradapter);
        btnHelp.setVisibility(View.VISIBLE);
        etUpdateContent.setVisibility(View.GONE);
        // 设置ViewPager
        initViewPager();
        // 设置TabLayout
        initTabLayout();
    }

    private void initViewPager() {
        fragmentList = new ArrayList<>();
        fragmentList.add(ContentOneFragment.newInstance("标题1", this));
        fragmentList.add(ContentOneFragment.newInstance("标题2", this));
        fragmentList.add(ContentOneFragment.newInstance("标题3", this));
        fragmentList.add(ContentOneFragment.newInstance("标题4", this));
        mAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        vpTabContent.setAdapter(mAdapter);

        vpTabContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                TabLayout.Tab tab = tabContainer.getTabAt(position);
                if (tab != null) {
                    if (tab.isSelected()) {
//                        LogTool.e(TAG, "onPageSelected " + position + "已设置TabLayout");
                    } else {
                        tab.select();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initTabLayout() {
        tabContainer.setTabMode(TabLayout.MODE_FIXED);
        tabContainer.addTab(tabContainer.newTab().setText("标题1").setIcon(R.mipmap.ic_location).setTag(POSITION_1));
        tabContainer.addTab(tabContainer.newTab().setText("标题2").setIcon(R.mipmap.ic_launcher).setTag(POSITION_2));
        tabContainer.addTab(tabContainer.newTab().setText("标题3").setIcon(R.mipmap.ic_launcher).setTag(POSITION_3));
        tabContainer.addTab(tabContainer.newTab().setText("标题4").setIcon(R.mipmap.ic_launcher).setTag(POSITION_4));

        tabContainer.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                LogTool.e(TAG, "onTabSelected " + tab.getText());
                tab.setIcon(R.mipmap.ic_location);
                if (tab.getTag() == null) {
                    return;
                }
                int position = (Integer) tab.getTag();
                if (position != vpTabContent.getCurrentItem()) {
                    vpTabContent.setCurrentItem(position);
                } else {
//                    LogTool.e(TAG, "onTabSelected " + position + "已切换ViewPager");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                LogTool.e(TAG, "onTabUnselected " + tab.getText());
                tab.setIcon(R.mipmap.ic_launcher);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                LogTool.e(TAG, "onTabReselected " + tab.getText());
            }
        });
//        tabContainer.setupWithViewPager(vpTabContent);//调用该方法后会将把前面所有tablayout添加的view都删掉，然后设置为PagerAdapter返回的title
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
        stringBuilder.append("1. 关于FragmentPagerAdapter:\n")
                .append("1.1 getCount()返回ViewPager页面的数量，getItem()返回要显示的fragment对象\n")
                .append("1.2 使用FragmentPagerAdapter作为ViewPager的PagerAdapter时，对于不再需要的Fragment，调用该Fragment的onDestroyView方法，销毁视图，不会销毁Fragment实例\n")
                .append("1.3 因为每一个Fragment均保存于内存中，故FragmentPagerAdapter适用于Fragment数量固定且较少的情况\n")
                .append("2. 关于TabLayout: \n")
                .append("2.1 TabLayout.setOnTabSelectedListener()可以监听Tab选择变化，其中:\n")
                .append("2.1.1 onTabSelected(Tab)方法是当前选择的Tab\n")
                .append("2.1.2 onTabUnselected(Tab)方法是之前选择的Tab\n")
                .append("2.1.3 onTabReselected(Tab)方法是再次点击当前选择Tab时回调\n")
                .append("2.2 调用TabLayout.setupWithViewPager(ViewPager)方法后会将把前面所有TabLayout添加的View都删掉，然后设置为PagerAdapter返回的title\n");
        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }

    @Override
    public void onRefreshFragment() {
        LogTool.e(TAG, "onRefreshFragment");
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public String getUpdateContent() {
        return etUpdateContent.getText().toString();
    }
}
