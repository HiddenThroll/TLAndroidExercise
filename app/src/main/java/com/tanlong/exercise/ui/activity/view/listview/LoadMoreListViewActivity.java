package com.tanlong.exercise.ui.activity.view.listview;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 * Created by 龙 on 2016/6/28.
 */
public class LoadMoreListViewActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.lv_activity_category)
    ListView mLvCategory;

    private List<String> mItems;
    private ArrayAdapter<String> mAdapter;

    private boolean mIsLoadingMore;// 是否正在加载更多
    private View mViewBottom;// 底部加载更多View

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);

        mTvTitle.setText(R.string.list_view_load_more);

        mIsLoadingMore = false;

        mItems = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mItems.add("item " + i);
        }
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mItems);
        mLvCategory.setAdapter(mAdapter);

        mViewBottom = LayoutInflater.from(this).inflate(R.layout.layout_load_more, null);

        mLvCategory.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount) {// 到达底部
                    if (!mIsLoadingMore) {// 没有加载更多
                        mIsLoadingMore = true;
                        // 显示加载更多
                        mLvCategory.addFooterView(mViewBottom);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int size = mItems.size();
                                for (int i = size; i < size + 10; i++) {
                                    mItems.add("item " + i);
                                }
                                mAdapter.notifyDataSetChanged();
                                //隐藏加载更多
                                mLvCategory.removeFooterView(mViewBottom);
                                mIsLoadingMore = false;
                            }
                        }, 2000);
                    }

                }
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void onBack() {
        finish();
    }
}
