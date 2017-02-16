package com.tanlong.exercise.ui.activity.view.viewpager.tabcontent;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.fragment.base.BaseFragment;
import com.tanlong.exercise.util.LogTool;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 龙 on 2016/11/14.
 */

public class ContentOneFragment extends BaseFragment {

    @Bind(R.id.tv_update_content)
    TextView tvUpdateContent;
    @Bind(R.id.tv_fragment_id)
    TextView tvFragmentId;
    @Bind(R.id.et_content)
    EditText etContent;

    public static final String FRAGMENT_IDTENTIFICATION = "fragment_idtentification";
    @Bind(R.id.btn_refresh)
    Button btnRefresh;
    public interface OnRefreshFragment {
        void onRefreshFragment();
    }
    private static OnRefreshFragment mOnRefreshFragment;

    public static ContentOneFragment newInstance(String idtenfication, OnRefreshFragment onRefreshFragment) {
        ContentOneFragment fragment = new ContentOneFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_IDTENTIFICATION, idtenfication);
        fragment.setArguments(bundle);
        mOnRefreshFragment = onRefreshFragment;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogTool.e(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        ButterKnife.bind(this, view);

        String idContent = getArguments().getString(FRAGMENT_IDTENTIFICATION);
        tvFragmentId.setText(idContent);
        String content = etContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            tvUpdateContent.setText("暂无输入数据");
        } else {
            tvUpdateContent.setText(content);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogTool.e(TAG, "onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogTool.e(TAG, "onCreate");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogTool.e(TAG, "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogTool.e(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogTool.e(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogTool.e(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogTool.e(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogTool.e(TAG, "onDestroyView");
        ButterKnife.unbind(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogTool.e(TAG, "onDetach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogTool.e(TAG, "onDestroy");
    }

    @OnClick(R.id.btn_refresh)
    public void onClick() {
        LogTool.e(TAG, "btn_refresh");
        mOnRefreshFragment.onRefreshFragment();
    }
}
