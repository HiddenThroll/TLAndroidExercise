package com.tanlong.exercise.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.view.fragment.ArticleContentActivity;
import com.tanlong.exercise.ui.fragment.base.BaseFragment;
import com.tanlong.exercise.util.LogTool;
import com.tanlong.exercise.util.ToastHelp;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.baidu.location.h.i.m;

/**
 * 文章列表Fragment
 * Created by 龙 on 2016/11/3.
 */

public class ListArticleFragment extends BaseFragment implements AdapterView.OnItemClickListener{

    @Bind(R.id.lv_list_title)
    ListView lvListTitle;
    List<String> titles = Arrays.asList("Hello", "World", "Peace");
    ArrayAdapter<String> adapter;
    private int mCurPosition;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_article, null);
        ButterKnife.bind(this, rootView);


        adapter = new ArrayAdapter<String>(mFragmentContext, R.layout.item_category,
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
        mCurPosition = position;
        Intent intent = new Intent(mFragmentContext, ArticleContentActivity.class);
        intent.putExtra(ArticleContentFragment.ARGUMENT_CONTENT, "内容是" + titles.get(position));

        ToastHelp.showShortMsg(mFragmentContext, "Fragment可以通过startActivityForResult()启动Activity");

        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogTool.e(TAG, "onActivityResult requestCode is " + requestCode + " resultCode is " + resultCode);
        ToastHelp.showShortMsg(mFragmentContext, "Fragment也可以在onActivityResult()中接收返回结果");
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String response = data.getStringExtra(ArticleContentFragment.ARGUMENT_RESPONSE);
                if (!TextUtils.isEmpty(response)){
                    titles.set(mCurPosition, titles.get(mCurPosition) + " -- " + response);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    public static ListArticleFragment newInstance() {
        return new ListArticleFragment();
    }
}
