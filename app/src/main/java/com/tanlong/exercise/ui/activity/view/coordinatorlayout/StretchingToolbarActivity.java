package com.tanlong.exercise.ui.activity.view.coordinatorlayout;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityStretchingToolbarBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.activity.view.recyclerview.adapter.SimpleRecyclerViewAdapter;
import com.tanlong.exercise.ui.activity.view.recyclerview.layoutmanager.MyLinearLayoutManager;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 可拉伸Toolbar
 * @author 龙
 */
public class StretchingToolbarActivity extends BaseActivity {
    ActivityStretchingToolbarBinding binding;
    private SimpleRecyclerViewAdapter adapter;
    private List<String> mDatas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_stretching_toolbar);
        binding.setActivity(this);
        initView();
    }

    private void initView() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            mDatas.add(String.valueOf(i));
        }
        adapter = new SimpleRecyclerViewAdapter(this, mDatas);
        binding.rvList.setLayoutManager(new MyLinearLayoutManager(this));
        binding.rvList.setAdapter(adapter);
    }

    public void showTips() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CoordinatorLayout实现可拉伸Toolbar\n")
                .append("1. 布局结构\n")
                .append("1.1 根布局使用CoordinatorLayout包裹AppBarLayout\n")
                .append("1.2 AppBarLayout包裹Toolbar\n")
                .append("1.3 RecycleView也是CoordinatorLayout的直接子类,与APPBarLayout平级\n")
                .append("2. 设置相应属性\n")
                .append("2.1 RecyclerView设置app:layout_behavior=\"@string/appbar_scrolling_view_behavior\"\n")
                .append("2.2 Toolbar设置app:layout_scrollFlags=\"scroll|enterAlways|enterAlwaysCollapsed\"\n")
                .append("3. app:layout_scrollFlags属性解释\n")
                .append("3.1 enterAlwaysCollapsed代表滚动View向下滑动时,先将Toolbar滑动至折叠高度(最小高度),然后滑动滚动View,当滚动View到达最上方时,再滑动Toolbar剩余部分,必须和scroll,enterAlways一起使用\n");
        ShowTipsFragment.newInstance(stringBuilder.toString()).show(getSupportFragmentManager(), "");
    }
}
