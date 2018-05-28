package com.tanlong.exercise.ui.activity.view.coordinatorlayout;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityHiddenToolbarBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.activity.view.recyclerview.adapter.SimpleRecyclerViewAdapter;
import com.tanlong.exercise.ui.activity.view.recyclerview.layoutmanager.MyLinearLayoutManager;

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
}
