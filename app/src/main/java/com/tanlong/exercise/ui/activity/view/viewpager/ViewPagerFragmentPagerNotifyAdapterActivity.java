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
import com.tanlong.exercise.ui.adapter.pageradapter.fragmentpageradapter.NotifyFragmentPagerAdapter;
import com.tanlong.exercise.util.LogTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 龙 on 2017/2/17.
 */

public class ViewPagerFragmentPagerNotifyAdapterActivity extends BaseActivity implements ContentOneFragment.OnRefreshFragment {

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

    List<Fragment> fragmentList;
    NotifyFragmentPagerAdapter mAdapter;

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
        tvTitle.setText(R.string.viewpager_fragment_fragmentpageradapter_notify);
        btnHelp.setVisibility(View.VISIBLE);
        // 设置ViewPager
        initViewPager();
        // 设置TabLayout
        initTabLayout();
    }

    private void initViewPager() {
        fragmentList = new ArrayList<>();
        fragmentList.add(ContentOneFragment.newInstance("标题1", this));
        mAdapter = new NotifyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, this);
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
        stringBuilder.append("调用FragmentPagerAdapter.notifyDataSetChanged()方法更新Fragment: \n")
                .append("1. ViewPager: \n")
                .append("1.1 ViewPager会在setAdapter()中调用PagerAdapter的registerDataSetObserver()方法，" +
                        "注册一个自己生成的PagerObserver对象，从而在PagerAdapter有所需要时(如notifyDataSetChanged())，" +
                        "可以调用Observer的onChanged()或onInvalidated()方法，实现PagerAdapter向ViewPager方向发送信息\n")
                .append("1.2 dataSetChanged()方法在在PagerObserver.onChanged()和PagerObserver.onInvalide()中被调用，" +
                        "该方法将使用getItemPosition()的返回值来进行判断，如果为POSITION_UNCHANGED，则什么都不做；" +
                        "如果为POSITION_NONE，则调用PagerAdapter.destroyItem()来去掉该对象，并设置为需要刷新以便触发PagerAdapter.instantiateItem()来生成新的对象\n")
                .append("2. FragmentPagerAdapter: ")
                .append("2.1 getItemPosition()该方法用于返回给定对象的位置，给定对象是instantiateItem()的返回值。" +
                        "当返回POSITION_UNCHANGED时，什么都不做;当返回POSITION_NONE时，会触发PagerAdapter.instantiateItem()来生成新的对象。默认返回POSITION_UNCHANGED\n")
                .append("2.2 getItem()方法在第一次生成Fragment时调用，适用于向Fragment传递静态数据\n")
                .append("2.3 instantiateItem()方法在ViewPager需要一个显示的Object时被调用，" +
                        "该方法判断要生成的Fragment是否已经生成过，如果生成过了，就使用旧的；如果没有生成过，就调用getItem()方法生成一个新的。" +
                        "适用于向Fragment传递动态数据\n")
                .append("3. notifyDataSetChanged()触发刷新过程：\n")
                .append("3.1 ViewPager通过setAdapter()方法与PagerAdapter建立联系，使用观察者模糊监听PagerAdapter，即PagerObserver\n")
                .append("3.2 调用PagerAdapter.notifyDataSetChanged()方法后，触发PagerObserver的onChanged()方法\n")
                .append("3.3 PagerObserver.onChanged()方法会调用ViewPager.dataSetChanged()\n")
                .append("3.4 dataSetChanged()判断PagerAdapter.getItemPosition()的返回值，如果返回值是POSITION_NONE，" +
                        "则调用PagerAdapter.destroyItem()来去掉该对象，并设置为需要刷新以便触发PagerAdapter.instantiateItem()来生成新的对象\n")
                .append("3.5 PagerAdapter.instantiateItem()中更新数据，实现Fragment的刷新\n");
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
