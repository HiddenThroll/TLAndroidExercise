package com.tanlong.exercise.ui.activity.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.fragment.tabcontent.ContentFiveFragment;
import com.tanlong.exercise.ui.fragment.tabcontent.ContentFourFragment;
import com.tanlong.exercise.ui.fragment.tabcontent.ContentOneFragment;
import com.tanlong.exercise.ui.fragment.tabcontent.ContentThreeFragment;
import com.tanlong.exercise.ui.fragment.tabcontent.ContentTwoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
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
    List<Fragment> itemFragments;
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

        setItemSelect(0);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return itemFragments.get(position);
            }

            @Override
            public int getCount() {
                return itemFragments.size();
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
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void resetItemSelect() {
        for (TextView textView : itemViews) {
            textView.setTextColor(ContextCompat.getColor(this, R.color.color_282c76));
        }
    }

    private void setItemSelect(int position) {
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
                    vpTabContent.setCurrentItem(0);
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
