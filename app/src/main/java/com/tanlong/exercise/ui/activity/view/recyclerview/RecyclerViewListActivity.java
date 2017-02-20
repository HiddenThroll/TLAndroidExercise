package com.tanlong.exercise.ui.activity.view.recyclerview;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 龙 on 2017/2/20.
 */

public class RecyclerViewListActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    List<String> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recycler_view);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    private void initData() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mDatas.add("item " + i);
        }
    }

    private void initView() {
        tvTitle.setText(R.string.recycler_view_list);
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

    }
}
