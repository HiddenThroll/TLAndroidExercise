package com.tanlong.exercise.ui.activity.view.recyclerview;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.activity.view.recyclerview.adapter.SimpleRecyclerViewAdapter;
import com.tanlong.exercise.ui.activity.view.recyclerview.divider.SimpleDividerItemDecoration;
import com.tanlong.exercise.ui.fragment.ShowTipsFragment;
import com.tanlong.exercise.util.ToastHelp;

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
        btnHelp.setVisibility(View.VISIBLE);

        SimpleRecyclerViewAdapter adapter = new SimpleRecyclerViewAdapter(this, mDatas);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);//设置布局管理器
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this,
                SimpleDividerItemDecoration.VERTICAL_LIST));//设置Divider
        mRecyclerView.setAdapter(adapter);
        adapter.setmOnItemClickListener(new SimpleRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                ToastHelp.showShortMsg(RecyclerViewListActivity.this, "onClick " + mDatas.get(position));
            }

            @Override
            public void onLongClick(View view, int position) {
                ToastHelp.showShortMsg(RecyclerViewListActivity.this, "onLongClick " + mDatas.get(position));
            }
        });
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
        stringBuilder.append("RecyclerView的基本用法: \n")
                .append("1. RecyclerView.setLayoutManager()设置布局管理器，控制RecyclerView的显示方式。这里使用线性布局管理器LinearLayoutManager\n")
                .append("1.1 可以通过LinearLayoutManager的构造方法设置垂直(LinearLayoutManager.VERTICAL)或水平(LinearLayoutManager.HORIZONTAL)布局、是否倒序显示数据(reverseLayout)\n")
                .append("2. RecyclerView.addItemDecoration()设置Item间的间隔\n")
                .append("2.1 需继承ItemDecoration抽象类并实现方法：\n")
                .append("2.1.1 onDraw()方法绘制Divider，该方法先于drawChildren()\n")
                .append("2.1.2 onDrawOver()方法绘制Divider，该方法在drawChildren()之后；一般选择二者之一绘制Divider\n")
                .append("2.1.3 getItemOffsets()通过outRect.set()为每个Item设置一定的偏移量，主要用于绘制Divider\n")
                .append("3. 通过接口的方式，在Adapter中设置单击监听和长按监听\n");

        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }
}
