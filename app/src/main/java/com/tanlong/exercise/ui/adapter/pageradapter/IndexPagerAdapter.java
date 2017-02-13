package com.tanlong.exercise.ui.adapter.pageradapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * ViewPager实现引导页使用的PagerAdapter, 最简PagerAdapter应用
 * Created by 龙 on 2017/2/9.
 */

public class IndexPagerAdapter extends PagerAdapter {

    private List<View> viewList;
    private Context mContext;

    public IndexPagerAdapter(List<View> viewList, Context mContext) {
        this.viewList = viewList;
        this.mContext = mContext;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));

//        return viewList.get(position);
        return position;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        int position = Integer.parseInt(object.toString());
        return view == viewList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "标题 " + String.valueOf(position + 1);
    }
}
