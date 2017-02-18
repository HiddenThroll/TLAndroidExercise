package com.tanlong.exercise.ui.adapter.pageradapter.fragmentstatepageradapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/2/18.
 */

public class SimpleFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mListFragment;

    public SimpleFragmentStatePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mListFragment = fragments;
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
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
