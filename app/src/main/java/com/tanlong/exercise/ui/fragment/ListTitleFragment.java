package com.tanlong.exercise.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.fragment.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 文章列表Fragment
 * Created by 龙 on 2016/11/3.
 */

public class ListTitleFragment extends BaseFragment implements AdapterView.OnItemClickListener{

    @Bind(R.id.lv_list_title)
    ListView lvListTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_title, container);
        ButterKnife.bind(this, rootView);

        String[] titles = new String[] {
                "Hello", "World", "Peace"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.item_category,
                titles);
        lvListTitle.setAdapter(adapter);

        lvListTitle.setOnItemClickListener(this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public static ListTitleFragment newInstance() {
        return new ListTitleFragment();
    }
}
