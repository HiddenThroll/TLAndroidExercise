package com.tanlong.exercise.ui.adapter.pageradapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.tanlong.exercise.util.LogTool;

import java.util.List;

/**
 * 真 无限循环滚动ViewPager使用的Adapter
 * Created by 龙 on 2017/2/9.
 */

public class RealUnlimitedScrollPagerAdapter extends PagerAdapter {
    private final String TAG = getClass().getSimpleName();
    private List<View> viewList;

    public RealUnlimitedScrollPagerAdapter(List<View> viewList) {
        this.viewList = viewList;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = viewList.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = viewList.get(position);
        container.removeView(view);
    }
}
