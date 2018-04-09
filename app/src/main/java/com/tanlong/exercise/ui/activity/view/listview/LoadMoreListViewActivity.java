package com.tanlong.exercise.ui.activity.view.listview;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;
import com.tanlong.exercise.util.LogTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 龙 on 2016/6/28.
 */
public class LoadMoreListViewActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.lv_activity_category)
    ListView mLvCategory;
    @BindView(R.id.btn_help)
    Button btnHelp;

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
        btnHelp.setVisibility(View.VISIBLE);

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
                int selectItemHeight = getSelectItemHeight(mLvCategory, firstVisibleItem, visibleItemCount, totalItemCount);
                LogTool.e(TAG, "ListView height is " + view.getHeight());
                LogTool.e(TAG, "getSelectItemHeight is " + selectItemHeight);
                if (firstVisibleItem + visibleItemCount == totalItemCount && // 到达底部
                        selectItemHeight > view.getHeight()) {// 并且已加载Item高度大于ListView规定高度
                    if (!mIsLoadingMore) {// 没有加载更多
                        mIsLoadingMore = true;
                        // 显示加载更多
                        mLvCategory.addFooterView(mViewBottom);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int size = mItems.size();
                                for (int i = size; i < size + 5; i++) {
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

    /**
     * 获取ListV指定Item之间的高度
     * @param listView -- 获取的ListView
     * @param firstVisibleItem -- 当前ListView的第一个可见Item的index(包含未完全显示的Item)
     * @param visibleItemCount -- 当前ListView可见Item的个数(包含未完全显示的Item)
     * @param totalItemCount -- 当前ListView的Item总个数
     * @return
     */
    public int getSelectItemHeight(ListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Adapter adapter = listView.getAdapter();
        if (adapter == null || adapter.getCount() == 0) {
            return 0;
        }
        int allItemHeight = 0;
        for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
            if (i >= totalItemCount) {
                break;
            }
            View itemView = adapter.getView(i, null, listView);
            itemView.measure(0, 0);
            allItemHeight += itemView.getMeasuredHeight();
        }

        int allDividerHeight = listView.getDividerHeight() * (visibleItemCount - 1);
        if (allDividerHeight < 0) {
            allDividerHeight = 0;
        }
        return allItemHeight + allDividerHeight;
    }

    @OnClick(R.id.btn_help)
    public void onClick() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("1. ListView.setOnScrollListener()设置滚动监听实现上滑加载更多\n")
                .append("2. 到达底部的判断条件为\n")
                .append("2.1 firstVisibleItem + visibleItemCount == totalItemCount\n")
                .append("2.2 第firstVisibleItem个Item 到第firstVisibleItem + visibleItemCount个Item的高度大于ListView的规定高度\n")
                .append("2.3 以上两个条件同时满足，才可以进行加载更多操作");
        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }
}
