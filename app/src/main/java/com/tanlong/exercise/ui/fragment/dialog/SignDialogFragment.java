package com.tanlong.exercise.ui.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.tanlong.exercise.R;

import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 龙 on 2017/4/25.
 */

public class SignDialogFragment extends BaseDialogFragment {


    public static final String SIGN_PIC_URL = "sign_pic_url";

    @Bind(R.id.iv_sign)
    ImageView ivSign;
    private String mPicUrl;

    public static SignDialogFragment newInstance(String picUrl) {
        SignDialogFragment fragment = new SignDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SIGN_PIC_URL, picUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPicUrl = getArguments().getString(SIGN_PIC_URL);
    }


    @Override
    protected View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_show_sign, container);
        ButterKnife.bind(this, rootView);

        Glide.with(getContext())
                .load(mPicUrl)
                .signature(new StringSignature(UUID.randomUUID().toString()))//附加一个数据到缓存key中
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(ivSign);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            dialog.getWindow().setLayout((int) (displayMetrics.widthPixels * 0.75),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
