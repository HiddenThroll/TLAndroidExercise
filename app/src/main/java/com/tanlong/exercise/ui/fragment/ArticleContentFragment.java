package com.tanlong.exercise.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.fragment.base.BaseFragment;
import com.tanlong.exercise.util.ToastHelp;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文章内容Fragment
 * Created by 龙 on 2016/11/4.
 */

public class ArticleContentFragment extends BaseFragment {

    @Bind(R.id.tv_article_content)
    TextView tvArticleContent;

    public static final String ARGUMENT_CONTENT = "argument_content";
    public static final String ARGUMENT_RESPONSE = "argument_response";

    public static ArticleContentFragment newInstance(String content) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_CONTENT, content);
        ArticleContentFragment fragment = new ArticleContentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_article_content, null);
        ButterKnife.bind(this, rootView);

        if (getArguments() != null) {
            String content = getArguments().getString(ARGUMENT_CONTENT, "");
            if (!TextUtils.isEmpty(content)) {
                tvArticleContent.setText(content);
            }
        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.tv_article_content)
    public void onClick() {
        String response = "good";

        if (mFragmentContext != null) {
            ToastHelp.showShortMsg(mFragmentContext, "Fragment需要通过getActivity().setResult()设置返回结果");
        }
        Intent intent = new Intent();
        intent.putExtra(ARGUMENT_RESPONSE, response);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }
}
