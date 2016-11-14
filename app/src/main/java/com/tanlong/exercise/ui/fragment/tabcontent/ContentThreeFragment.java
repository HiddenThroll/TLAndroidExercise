package com.tanlong.exercise.ui.fragment.tabcontent;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.fragment.base.BaseFragment;
import com.tanlong.exercise.util.LogTool;

/**
 * Created by 龙 on 2016/11/14.
 */

public class ContentThreeFragment extends BaseFragment {

    public static ContentThreeFragment newInstance() {
        return new ContentThreeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_three, container, false);
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
}
