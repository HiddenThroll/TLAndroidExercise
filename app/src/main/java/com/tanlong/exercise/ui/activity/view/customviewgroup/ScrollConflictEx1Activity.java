package com.tanlong.exercise.ui.activity.view.customviewgroup;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityScrollConflict1Binding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.view.customviewgroup.HorizontalScrollViewEx;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 龙
 */
public class ScrollConflictEx1Activity extends BaseActivity {
    ActivityScrollConflict1Binding binding;
    HorizontalScrollViewEx mListContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_scroll_conflict_1);
        mListContainer = binding.horizontalScrollView;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < 3; i++) {
            ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.layout_scroll_conflict_content,
                    mListContainer, false);
            TextView tvTitle = viewGroup.findViewById(R.id.tv_title);
            tvTitle.setText("page " + (i + 1));
            createList(viewGroup);
            mListContainer.addView(viewGroup);
        }
    }

    private void createList(ViewGroup viewGroup) {
        ListView listView = viewGroup.findViewById(R.id.lv_content);
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            stringList.add("item " + i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_category, stringList);
        listView.setAdapter(adapter);
    }
}
