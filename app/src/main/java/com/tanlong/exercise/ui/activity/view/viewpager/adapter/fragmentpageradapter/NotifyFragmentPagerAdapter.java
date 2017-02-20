package com.tanlong.exercise.ui.activity.view.viewpager.adapter.fragmentpageradapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.tanlong.exercise.ui.activity.view.viewpager.tabcontent.ContentOneFragment;
import com.tanlong.exercise.ui.fragment.base.BaseFragment;
import com.tanlong.exercise.util.LogTool;

import java.util.List;

/**
 * 可通过NotifyDataSetChanged()方法更新Fragment的Adapter
 * Created by 龙 on 2017/2/17.
 */

public class NotifyFragmentPagerAdapter extends FragmentPagerAdapter {
    private final String TAG = getClass().getSimpleName();
    private List<Fragment> mListFragment;
    private ContentOneFragment.OnRefreshFragment mOnRefreshFragment;

    public NotifyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList,
                                      ContentOneFragment.OnRefreshFragment onRefreshFragment) {
        super(fm);
        mListFragment = fragmentList;
        mOnRefreshFragment = onRefreshFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return mListFragment.get(position);
    }

    @Override
    public int getCount() {
        return mListFragment.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LogTool.e(TAG, "instantiateItem " + position);
        BaseFragment fragment = (BaseFragment) super.instantiateItem(container, position);
        return fragment;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
