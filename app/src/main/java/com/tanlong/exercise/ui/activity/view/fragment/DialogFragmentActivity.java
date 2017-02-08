package com.tanlong.exercise.ui.activity.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.fragment.LoginDialogFragment;
import com.tanlong.exercise.ui.fragment.ShowTipsFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 对话框Fragment
 * Created by Administrator on 2016/10/26.
 */

public class DialogFragmentActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_normal_dialog)
    Button btnNormalDialog;
    @Bind(R.id.btn_fragment_dialog)
    Button btnFragmentDialog;
    @Bind(R.id.btn_help)
    Button btnHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dialog_fragment);
        ButterKnife.bind(this);
        tvTitle.setText(R.string.fragment_dialog);
        btnHelp.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.iv_back, R.id.btn_normal_dialog, R.id.btn_fragment_dialog, R.id.btn_help})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_normal_dialog:
                showNormalDialog();
                break;
            case R.id.btn_fragment_dialog:
                showFragmentDialog();
                break;
            case R.id.btn_help:
                showTips();
                break;
        }
    }

    private void showNormalDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(LayoutInflater.from(this).inflate(R.layout.dialog_login, null))
                .setPositiveButton("登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void showFragmentDialog() {
        LoginDialogFragment dialogFragment = new LoginDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "LoginDialogFragment");
    }

    private void showTips() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("1. 官方推荐使用DialogFragment实现对话框\n")
                .append("2. 当发生屏幕旋转等操作时，DialogFragment可以保存状态，AlertDialog不能\n")
                .append("3. 在onCreateView()方法中，调用getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE)方法去除对话框标题\n")
                .append("4. 在onStart()方法中，调用dialog.getWindow().setLayout(int width, int height)方法设置对话框宽高\n");

        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }
}
