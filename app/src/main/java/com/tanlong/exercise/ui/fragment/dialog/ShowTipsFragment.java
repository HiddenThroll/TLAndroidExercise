package com.tanlong.exercise.ui.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.util.DisplayUtil;
import com.tanlong.exercise.util.LogTool;
import com.tanlong.exercise.util.VersionUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 显示提示对话框
 * Created by 龙 on 2016/11/29.
 */

public class ShowTipsFragment extends DialogFragment {
    private final String TAG = getClass().getSimpleName();
    @BindView(R.id.tv_tips)
    TextView tvTips;
    public static final String SHOW_TIPS = "showtipsfragment";
    Unbinder unbinder;

    public static ShowTipsFragment newInstance(String content) {
        ShowTipsFragment fragment = new ShowTipsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SHOW_TIPS, content);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        final View rootView = inflater.inflate(R.layout.dialog_show_tips, container);
        unbinder = ButterKnife.bind(this, rootView);

        if (getArguments() != null) {
            String content = getArguments().getString(SHOW_TIPS, "暂无内容");
            tvTips.setText(content);
        }

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (VersionUtil.hasJellyBean()) {
                    rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                int rootHeitht = rootView.getHeight();
                int halfScreenHeight = (int) (DisplayUtil.getDisplay(getContext()).y * 0.5);
                Dialog dialog = getDialog();
                LogTool.e(TAG, "rootHeitht is " + rootHeitht + " halfScreenHeight is " + halfScreenHeight);
                if (rootHeitht < halfScreenHeight) {//小于屏幕高度的一半
                    if (dialog != null) {
                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        dialog.getWindow().setLayout((int) (displayMetrics.widthPixels * 0.75),
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                    }
                } else {
                    if (dialog != null) {
                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        dialog.getWindow().setLayout((int) (displayMetrics.widthPixels * 0.75), halfScreenHeight);
                    }
                }
            }
        });
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
