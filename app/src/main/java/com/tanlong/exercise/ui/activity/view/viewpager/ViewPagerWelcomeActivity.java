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
import com.tanlong.exercise.ui.activity.view.viewpager.adapter.IndexPagerAdapter;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 使用ViewPager实现引导页
 * Created by 龙 on 2017/2/9.
 */

public class ViewPagerWelcomeActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_help)
    Button btnHelp;
    @BindView(R.id.vp_welcome)
    ViewPager vpWelcome;

    List<View> viewList;
    IndexPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_viewpager_welcome);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        tvTitle.setText(R.string.viewpager_welcome);
        btnHelp.setVisibility(View.VISIBLE);

        LayoutInflater inflater = LayoutInflater.from(this);
        viewList = new ArrayList<>();
        viewList.add(inflater.inflate(R.layout.layout_index_1, null));
        viewList.add(inflater.inflate(R.layout.layout_index_2, null));
        viewList.add(inflater.inflate(R.layout.layout_index_3, null));
        viewList.add(inflater.inflate(R.layout.layout_index_4, null));

        pagerAdapter = new IndexPagerAdapter(viewList, this);
        vpWelcome.setAdapter(pagerAdapter);
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
            default:
                break;
        }
    }

    private void showTips() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("1. PagerAdapter常用四方法解析：\n")
                .append("1.1 每个滑动页面对应一个唯一的Key，该Key用于唯一追踪这个页面\n")
                .append("1.2 void destroyItem(ViewGroup container, int position, Object object)方法移除给定位置的页面，参数中的object就是页面对应的Key\n")
                .append("1.3 int getCount()返回有效视图的个数\n")
                .append("1.4 Object instantiateItem (ViewGroup container, int position)创建指定位置的页面视图，其返回值Object就是页面对应的Key!!!\n")
                .append("1.5 boolean isViewFromObject (View view, Object object)判断instantiateItem()方法返回的Key(参数中的object)与一个页面视图(参数中的view)是否是同一个视图(view)\n");

        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }
}
