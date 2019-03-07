package com.tanlong.exercise.ui.activity.view.fragment.lazyload;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;

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

        initViewPager();
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
