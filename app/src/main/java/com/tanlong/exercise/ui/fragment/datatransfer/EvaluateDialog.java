package com.tanlong.exercise.ui.fragment.datatransfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.tanlong.exercise.R;
import com.tanlong.exercise.util.ToastHelp;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 评价对话框
 * Created by 龙 on 2016/11/4.
 */

public class EvaluateDialog extends DialogFragment {

    @BindView(R.id.et_evaluate)
    EditText etEvaluate;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    Unbinder unbinder;
    public static final String EVALUATE_RESPONSE = "evaluate_response";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_MinWidth);
        View rootView = inflater.inflate(R.layout.fragment_evaluate, null);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_commit)
    public void onClick() {

        if (getTargetFragment() == null) {//没有设置启动Fragment
            dismiss();
            return;
        }

        if (getActivity() != null) {
//            ToastHelp.showShortMsg(getActivity(), "通过getTargetFragment().onActivityResult()直接" +
//                    "调用启动Fragment的onActivityResult()方法");
        }

        String response = etEvaluate.getText().toString();
        if (TextUtils.isEmpty(response)) {
            response = "暂无评价";
        }
        Intent intent = new Intent();
        intent.putExtra(EVALUATE_RESPONSE, response);
        getTargetFragment().onActivityResult(ArticleContentFragment.REQUEST_EVALUATE,
                Activity.RESULT_OK, intent);
    }
}
