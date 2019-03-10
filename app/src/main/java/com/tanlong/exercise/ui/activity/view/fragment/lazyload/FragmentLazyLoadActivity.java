package com.tanlong.exercise.ui.activity.view.fragment.lazyload;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityFragmentLazyLoadBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.fragment.LazyLoadFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 龙
 */
public class FragmentLazyLoadActivity extends BaseActivity {
    ActivityFragmentLazyLoadBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fragment_lazy_load);

        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("Fragment懒加载");

        initViewPager();
        initTabLayout();
    }


    private void initViewPager() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(LazyLoadFragment.newInstance(ContextCompat.getColor(this, R.color.colorPrimaryDark)));
        fragmentList.add(LazyLoadFragment.newInstance(ContextCompat.getColor(this, R.color.color_86d0ab)));
        fragmentList.add(LazyLoadFragment.newInstance(ContextCompat.getColor(this, R.color.color_fb9b10)));
        fragmentList.add(LazyLoadFragment.newInstance(ContextCompat.getColor(this, R.color.colorAccent)));
        binding.viewPager.setOffscreenPageLimit(3);
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                TabLayout.Tab tab = binding.tabLayout.getTabAt(position);
                if (tab != null) {
                    if (tab.isSelected()) {
                        Logger.e("onPageSelected " + position + "已设置TabLayout");
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
        binding.tabLayout.setTabMode(TabLayout.MODE_FIXED);
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int position = tab.getPosition();
                if (position != binding.viewPager.getCurrentItem()) {
                    binding.viewPager.setCurrentItem(position);
                } else {
                    Logger.e("onTabSelected " + position + "已切换ViewPager");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mListFragment;

        public SimpleFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            mListFragment = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return mListFragment.get(position);
        }

        @Override
        public int getCount() {
            return mListFragment == null ? 0 : mListFragment.size();
        }
    }
}
