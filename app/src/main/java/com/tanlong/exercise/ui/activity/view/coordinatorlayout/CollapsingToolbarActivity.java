package com.tanlong.exercise.ui.activity.view.coordinatorlayout;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityCollapsingToolbarBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.activity.view.recyclerview.adapter.SimpleRecyclerViewAdapter;
import com.tanlong.exercise.ui.activity.view.recyclerview.layoutmanager.MyLinearLayoutManager;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 可折叠状态栏
 * @author 龙
 */
public class CollapsingToolbarActivity extends BaseActivity {
    ActivityCollapsingToolbarBinding binding;
    private SimpleRecyclerViewAdapter adapter;
    private List<String> mDatas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_collapsing_toolbar);
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
        stringBuilder.append("CoordinatorLayout实现可折叠Toolbar\n")
                .append("1. 布局结构\n")
                .append("1.1 根布局使用CoordinatorLayout包裹AppBarLayout\n")
                .append("1.2 AppBarLayout包裹CollapsingToolbarLayout\n")
                .append("1.3 CollapsingToolbarLayout包裹Toolbar\n")
                .append("1.3 RecycleView也是CoordinatorLayout的直接子类,与APPBarLayout平级\n")
                .append("2. 设置相应属性\n")
                .append("2.1 RecyclerView设置app:layout_behavior=\"@string/appbar_scrolling_view_behavior\"\n")
                .append("2.2 CollapsingToolbarLayout设置app:layout_scrollFlags=\"scroll|exitUntilCollapsed\"\n")
                .append("2.3 ImageView设置app:layout_collapseMode=\"parallax\"\n")
                .append("2.4 Toolbar设置app:layout_collapseMode=\"pin\"\n")
                .append("3. app:layout_scrollFlags属性解释\n")
                .append("3.1 scroll代表本View(Toolbar)与滚动View(RecyclerView)是一体的,可以将本View视为RecyclerView的第一个Item\n")
                .append("3.3 exitUntilCollapsed代表当本View离开屏幕时,会被“折叠”直到达到其最小高度;当本View折叠后才RecyclerView才开始滚动;当RecyclerView向下滚动时,当其顶部内容全部展示后,本View才开始向下滚动\n")
                .append("4. app:layout_collapseMode属性用于指定折叠过程中的折叠模式\n")
                .append("4.1 app:layout_collapseMode=\"parallax\"指定的View会在折叠过程中产生一定的视差\n")
                .append("4.2 app:layout_collapseMode=\"pin\"指定的View在折叠过程中位置始终保持不变\n");
        ShowTipsFragment.newInstance(stringBuilder.toString()).show(getSupportFragmentManager(), "");
    }
}
