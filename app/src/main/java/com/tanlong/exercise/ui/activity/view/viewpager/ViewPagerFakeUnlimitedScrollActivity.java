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
import com.tanlong.exercise.ui.adapter.pageradapter.FakeUnlimitedScrollPagerAdapter;
import com.tanlong.exercise.ui.fragment.ShowTipsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 龙 on 2017/2/9.
 */

public class ViewPagerFakeUnlimitedScrollActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;
    @Bind(R.id.vp_welcome)
    ViewPager vpWelcome;

    List<View> viewList;
    FakeUnlimitedScrollPagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_viewpager_welcome);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        tvTitle.setText(R.string.viewpager_fake_unlimited_scroll);
        btnHelp.setVisibility(View.VISIBLE);

        LayoutInflater inflater = LayoutInflater.from(this);
        viewList = new ArrayList<>();
        viewList.add(inflater.inflate(R.layout.layout_index_1, null));
        viewList.add(inflater.inflate(R.layout.layout_index_2, null));
        viewList.add(inflater.inflate(R.layout.layout_index_3, null));
        viewList.add(inflater.inflate(R.layout.layout_index_4, null));

        pagerAdapter = new FakeUnlimitedScrollPagerAdapter(viewList);
        vpWelcome.setAdapter(pagerAdapter);

        vpWelcome.setCurrentItem(Integer.MAX_VALUE / 2);//从中间开始
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
        stringBuilder.append("ViewPager实现伪无限循环滚动原理：")
                .append("1. 覆写getCount()方法，返回一个足够大的值，如Integer.MAX_VALUE\n")
                .append("2. instantiateItem()方法中添加View时，position对viewList.size()取余，即viewList.get(position % viewList.size())\n")
                .append("2. destroyItem()方法中删除View时，position对viewList.size()取余，即viewList.get(position % viewList.size())\n");

        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }
}
