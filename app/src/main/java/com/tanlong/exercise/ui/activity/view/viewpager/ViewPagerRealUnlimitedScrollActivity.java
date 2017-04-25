package com.tanlong.exercise.ui.activity.view.viewpager;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.activity.view.viewpager.adapter.RealUnlimitedScrollPagerAdapter;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;
import com.tanlong.exercise.util.LogTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ViewPager实现 真 无限循环滚动
 * Created by 龙 on 2017/2/9.
 */

public class ViewPagerRealUnlimitedScrollActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;
    @Bind(R.id.vp_welcome)
    ViewPager vpWelcome;

    List<View> viewList;
    RealUnlimitedScrollPagerAdapter pagerAdapter;
    private int realViewCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_viewpager_welcome);
        ButterKnife.bind(this);

        realViewCount = 4;
        initView();

    }

    private void initView() {
        tvTitle.setText(R.string.viewpager_real_unlimited_scroll);
        btnHelp.setVisibility(View.VISIBLE);

        LayoutInflater inflater = LayoutInflater.from(this);
        viewList = new ArrayList<>();
        // 第一个位置放最后一个View，
        viewList.add(inflater.inflate(R.layout.layout_index_4, null));
        viewList.add(inflater.inflate(R.layout.layout_index_1, null));
        viewList.add(inflater.inflate(R.layout.layout_index_2, null));
        viewList.add(inflater.inflate(R.layout.layout_index_3, null));
        viewList.add(inflater.inflate(R.layout.layout_index_4, null));
        // 最后一个位置放第一个View，
        viewList.add(inflater.inflate(R.layout.layout_index_1, null));

        pagerAdapter = new RealUnlimitedScrollPagerAdapter(viewList);
        vpWelcome.setAdapter(pagerAdapter);

        vpWelcome.setCurrentItem(1);//展示第二个View，即index_1
        vpWelcome.addOnPageChangeListener(this);
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
        stringBuilder.append("ViewPager实现真无限循环滚动原理：\n")
                .append("1. 若需显示4个View，则ViewPager共显示6个View，排列顺序为“4,1,2,3,4,1”\n")
                .append("2. 初始化时，调用ViewPager.setCurrentItem(1)方法显示View_1\n")
                .append("3. ViewPager添加PageChangeListener: \n")
                .append("3.1 当移动到第一个位置(position = 0)时，显示第二个View_4,即ViewPager.setCurrentItem(4)\n")
                .append("3.2 当移动到最后一个位置(position = 5)时，显示第一个View_1,即ViewPager.setCurrentItem(1)\n");
        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        LogTool.e(TAG, "position is " + position);
        int pageIndex = position;
        if (position == 0) {//移动到第一个位置，应该显示最后一个View（这里是index_4）
            pageIndex = realViewCount;
        } else if (position == realViewCount + 1) {//移动到最后一个位置，应该显示第一个View(这里是这里是index_1)
            pageIndex = 1;
        }
        LogTool.e(TAG, "pageIndex is " + pageIndex);
        if (position != pageIndex) {
            vpWelcome.setCurrentItem(pageIndex, false);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
