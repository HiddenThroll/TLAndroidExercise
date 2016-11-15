package com.tanlong.exercise.ui.activity.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.fragment.base.BaseFragment;
import com.tanlong.exercise.ui.fragment.tabcontent.ContentFiveFragment;
import com.tanlong.exercise.ui.fragment.tabcontent.ContentFourFragment;
import com.tanlong.exercise.ui.fragment.tabcontent.ContentOneFragment;
import com.tanlong.exercise.ui.fragment.tabcontent.ContentThreeFragment;
import com.tanlong.exercise.ui.fragment.tabcontent.ContentTwoFragment;
import com.tanlong.exercise.util.LogTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fragment + ViewPager 练习
 * Created by 龙 on 2016/11/14.
 */

public class TabContentActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.ll_tab_select)
    LinearLayout llTabSelect;
    @Bind(R.id.vp_tab_content)
    ViewPager vpTabContent;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_message)
    TextView tvMessage;
    @Bind(R.id.tv_phonebook)
    TextView tvPhonebook;
    @Bind(R.id.tv_discovery)
    TextView tvDiscovery;
    @Bind(R.id.tv_personal)
    TextView tvPersonal;
    @Bind(R.id.tv_setting)
    TextView tvSetting;

    List<TextView> itemViews;
    List<BaseFragment> itemFragments;
    FragmentPagerAdapter mAdapter;
    int curPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tab_content);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        tvTitle.setText(R.string.fragment_viewpager);

        itemViews = new ArrayList<>();
        itemViews.add(tvMessage);
        itemViews.add(tvPhonebook);
        itemViews.add(tvDiscovery);
        itemViews.add(tvPersonal);
        itemViews.add(tvSetting);

        itemFragments = new ArrayList<>();
        itemFragments.add(ContentOneFragment.newInstance());
        itemFragments.add(ContentTwoFragment.newInstance());
        itemFragments.add(ContentThreeFragment.newInstance());
        itemFragments.add(ContentFourFragment.newInstance());
        itemFragments.add(ContentFiveFragment.newInstance());

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                //生成 新的 Fragment 对象时调用，可用于设置静态数据
                return itemFragments.get(position);
            }

            @Override
            public int getCount() {
                return itemFragments.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // 判断要生成的Fragment是否已经生成过
                // 如果生成过了，通过FragmentTransaction.attach()使用旧的Fragment
                // 如果没有生成，调用getItem()方法生成新的Fragment，并调用FragmentTransaction.add()添加Fragment
                // 可以在这里设置更新数据
                BaseFragment fragment = (BaseFragment) super.instantiateItem(container, position);
                LogTool.e(TAG, "instantiateItem position is " + position + " update time is " + fragment.getUpdateContent());
                return fragment;
            }

            @Override
            public int getItemPosition(Object object) {
                // 返回POSITION_UNCHANGED代表位置没有改变，不会做任何事
                // 返回POSITION_NONE代表Adapter中没有Item，这会导致 PagerAdapter.destroyItem() 来去掉该对象，
                // 并设置为需要刷新 (needPopulate = true) 以便触发 PagerAdapter.instantiateItem() 来生成新的对象
                return PagerAdapter.POSITION_NONE;
            }
        };

        vpTabContent.setAdapter(mAdapter);
        vpTabContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetItemSelect();
                setItemSelect(position);
                // 刷新Fragment UI
                int times = Integer.valueOf(itemFragments.get(position).getUpdateContent());
                times++;
                itemFragments.get(position).setUpdateContent(String.valueOf(times));
                LogTool.e(TAG, "onPageSelected position is " + position + " times is " + times);
                mAdapter.notifyDataSetChanged();// 通过观察者模式，触发ViewPager.dataSetChanged()方法,
                                                // dataSetChanged()方法中会判断PagerAdapter.getItemPosition()的返回值

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setItemSelect(0);
    }

    private void resetItemSelect() {
        for (TextView textView : itemViews) {
            textView.setTextColor(ContextCompat.getColor(this, R.color.color_282c76));
        }
    }

    private void setItemSelect(int position) {
        // 改变文字颜色
        itemViews.get(position).setTextColor(ContextCompat.getColor(this, R.color.color_white));
    }

    @OnClick({R.id.iv_back, R.id.tv_message, R.id.tv_phonebook, R.id.tv_discovery, R.id.tv_personal, R.id.tv_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_message:
                if (curPosition != 0) {
                    resetItemSelect();
                    setItemSelect(0);
                    vpTabContent.setCurrentItem(0);//也会触发ViewPager的onPageSelected()回调
                    curPosition = 0;
                }
                break;
            case R.id.tv_phonebook:
                if (curPosition != 1) {
                    resetItemSelect();
                    setItemSelect(1);
                    vpTabContent.setCurrentItem(1);
                    curPosition = 1;
                }
                break;
            case R.id.tv_discovery:
                if (curPosition != 2) {
                    resetItemSelect();
                    setItemSelect(2);
                    vpTabContent.setCurrentItem(2);
                    curPosition = 2;
                }
                break;
            case R.id.tv_personal:
                if (curPosition != 3) {
                    resetItemSelect();
                    setItemSelect(3);
                    vpTabContent.setCurrentItem(3);
                    curPosition = 3;
                }
                break;
            case R.id.tv_setting:
                if (curPosition != 4) {
                    resetItemSelect();
                    setItemSelect(4);
                    vpTabContent.setCurrentItem(4);
                    curPosition = 4;
                }
                break;
        }
    }
}
