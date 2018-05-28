package com.tanlong.exercise.ui.activity.view.coordinatorlayout;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityHiddenToolbarBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.activity.view.recyclerview.adapter.SimpleRecyclerViewAdapter;
import com.tanlong.exercise.ui.activity.view.recyclerview.layoutmanager.MyLinearLayoutManager;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 可隐藏状态栏
 * @author 龙
 */
public class HiddenToolbarActivity extends BaseActivity {

    private ActivityHiddenToolbarBinding binding;
    private SimpleRecyclerViewAdapter adapter;
    private List<String> mDatas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_hidden_toolbar);
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
        stringBuilder.append("CoordinatorLayout实现可隐藏Toolbar\n")
                .append("1. 布局结构\n")
                .append("1.1 根布局使用CoordinatorLayout包裹AppBarLayout\n")
                .append("1.2 AppBarLayout包裹Toolbar\n")
                .append("1.3 RecycleView也是CoordinatorLayout的直接子类,与APPBarLayout平级\n")
                .append("2. 设置相应属性\n")
                .append("2.1 RecyclerView设置app:layout_behavior=\"@string/appbar_scrolling_view_behavior\"\n")
                .append("2.2 Toolbar设置app:layout_scrollFlags=\"scroll|enterAlways|snap\"\n")
                .append("3. app:layout_scrollFlags属性解释\n")
                .append("3.1 scroll代表本View(Toolbar)与滚动View(RecyclerView)是一体的,可以将本View视为RecyclerView的第一个Item\n")
                .append("3.2 enterAlways代表当向下滑动时,本View会跟着一起滑动,就像滚动View和本View一起向下滑动,需和scroll一起设置\n")
                .append("3.3 snap作用是在一次滚动结束时，本 View 很可能只处于“部分显示”的状态,加上这个标记能够达到“要么完全隐藏，要么完全显示”的效果\n");
        ShowTipsFragment.newInstance(stringBuilder.toString()).show(getSupportFragmentManager(), "");
    }
}
