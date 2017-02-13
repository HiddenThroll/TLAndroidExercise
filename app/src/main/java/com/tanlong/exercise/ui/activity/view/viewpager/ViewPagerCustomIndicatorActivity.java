package com.tanlong.exercise.ui.activity.view.viewpager;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.adapter.pageradapter.IndexPagerAdapter;
import com.tanlong.exercise.util.LogTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ViewPager自定义滑动指示器
 * Created by 龙 on 2017/2/10.
 */

public class ViewPagerCustomIndicatorActivity extends BaseActivity {
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;
    @Bind(R.id.tv_title_1)
    TextView tvTitle1;
    @Bind(R.id.tv_title_2)
    TextView tvTitle2;
    @Bind(R.id.tv_title_3)
    TextView tvTitle3;
    @Bind(R.id.tv_title_4)
    TextView tvTitle4;
    @Bind(R.id.iv_indicator)
    ImageView ivIndicator;
    @Bind(R.id.vp_custom_indicator)
    ViewPager vpCustomIndicator;

    List<View> viewList;
    IndexPagerAdapter pagerAdapter;

    private int lastIndex = 0;
    private TextView lastTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_viewpager_custom_indicator);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        tvTitle.setText(R.string.viewpager_custom_indicator);
        btnHelp.setVisibility(View.VISIBLE);
        lastTextView = tvTitle1;

        LayoutInflater inflater = LayoutInflater.from(this);
        viewList = new ArrayList<>();
        viewList.add(inflater.inflate(R.layout.layout_index_1, null));
        viewList.add(inflater.inflate(R.layout.layout_index_2, null));
        viewList.add(inflater.inflate(R.layout.layout_index_3, null));
        viewList.add(inflater.inflate(R.layout.layout_index_4, null));

        pagerAdapter = new IndexPagerAdapter(viewList, this);
        vpCustomIndicator.setAdapter(pagerAdapter);

        vpCustomIndicator.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                LogTool.e(TAG, "position is " + position);
                changeView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        changeView(0);
    }

    @OnClick({R.id.iv_back, R.id.btn_help, R.id.tv_title_1, R.id.tv_title_2, R.id.tv_title_3, R.id.tv_title_4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                showTips();
                break;
            case R.id.tv_title_1:
                selectContent(0);
                break;
            case R.id.tv_title_2:
                selectContent(1);
                break;
            case R.id.tv_title_3:
                selectContent(2);
                break;
            case R.id.tv_title_4:
                selectContent(3);
                break;
        }
    }

    private void showTips() {

    }

    private void selectContent(int index) {
        //移动ViewPager
        vpCustomIndicator.setCurrentItem(index);
    }

    private void changeView(int index) {
        // 改变文字颜色
        lastTextView.setTextColor(ContextCompat.getColor(this, R.color.text_color_title));
        switch (index) {
            case 0:
                tvTitle1.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                lastTextView = tvTitle1;
                break;
            case 1:
                tvTitle2.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                lastTextView = tvTitle2;
                break;
            case 2:
                tvTitle3.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                lastTextView = tvTitle3;
                break;
            case 3:
                tvTitle4.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                lastTextView = tvTitle4;
                break;
        }
        // 移动指示条

    }
}
