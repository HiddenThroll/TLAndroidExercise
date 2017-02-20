package com.tanlong.exercise.ui.activity.view.viewpager.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * ViewPager实现 伪 无限循环滚动使用的Adapter
 * Created by 龙 on 2017/2/9.
 */

public class FakeUnlimitedScrollPagerAdapter extends PagerAdapter {

    private List<View> viewList;

    public FakeUnlimitedScrollPagerAdapter(List<View> viewList) {
        this.viewList = viewList;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = viewList.get(position % viewList.size());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = viewList.get(position % viewList.size());
        container.removeView(view);
    }
}
