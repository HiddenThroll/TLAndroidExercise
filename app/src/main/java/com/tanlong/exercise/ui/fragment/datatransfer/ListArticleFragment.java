package com.tanlong.exercise.ui.fragment.datatransfer;

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
import com.tanlong.exercise.ui.activity.view.fragment.datatransfer.ArticleContentActivity;
import com.tanlong.exercise.ui.fragment.base.BaseFragment;
import com.tanlong.exercise.util.LogTool;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 文章列表Fragment
 * Created by 龙 on 2016/11/3.
 */

public class ListArticleFragment extends BaseFragment implements AdapterView.OnItemClickListener{

    @BindView(R.id.lv_list_title)
    ListView lvListTitle;
    List<String> titles = Arrays.asList("Hello", "World", "Peace");
    ArrayAdapter<String> adapter;
    private int mCurPosition;

    Unbinder unbinder;

    public static final int REQUEST_CONTENT = 1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_article, null);
        unbinder = ButterKnife.bind(this, rootView);


        adapter = new ArrayAdapter<>(mFragmentContext, R.layout.item_category,
                titles);
        lvListTitle.setAdapter(adapter);

        lvListTitle.setOnItemClickListener(this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCurPosition = position;
        Intent intent = new Intent(mFragmentContext, ArticleContentActivity.class);
        intent.putExtra(ArticleContentFragment.ARGUMENT_CONTENT, "内容是" + titles.get(position));

//        ToastHelp.showShortMsg(mFragmentContext, "Fragment可以通过startActivityForResult()启动Activity");

        startActivityForResult(intent, REQUEST_CONTENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogTool.e(TAG, "onActivityResult requestCode is " + requestCode + " resultCode is " + resultCode);
//        ToastHelp.showShortMsg(mFragmentContext, "Fragment也可以在onActivityResult()中接收返回结果");
        if (requestCode == REQUEST_CONTENT) {
            if (resultCode == Activity.RESULT_OK) {
                String response = data.getStringExtra(ArticleContentFragment.RESPONSE_EVALUATE);
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
