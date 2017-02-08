package com.tanlong.exercise.ui.fragment.datatransfer;

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
    // 显示内容
    public static final String ARGUMENT_CONTENT = "argument_content";
    // 内容回复
    public static final String RESPONSE_EVALUATE = "argument_response";

    public static final int REQUEST_EVALUATE = 1;

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
        // 显示评价对话框
        EvaluateDialog evaluateDialog = new EvaluateDialog();
        evaluateDialog.setTargetFragment(this, REQUEST_EVALUATE);
//        ToastHelp.showShortMsg(mFragmentContext, "通过setTargetFragment()设置启动的Fragment");
        evaluateDialog.show(getFragmentManager(), "Evaluate");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EVALUATE) {
            String response = data.getStringExtra(EvaluateDialog.EVALUATE_RESPONSE);
            if (mFragmentContext != null) {
//                ToastHelp.showShortMsg(mFragmentContext, "Fragment需要通过getActivity().setResult()设置返回结果");
            }
            Intent intent = new Intent();
            intent.putExtra(RESPONSE_EVALUATE, response);
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        }
    }
}
