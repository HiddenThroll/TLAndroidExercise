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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dialog_fragment);
        ButterKnife.bind(this);
        tvTitle.setText(R.string.fragment_dialog);
    }

    @OnClick({R.id.iv_back, R.id.btn_normal_dialog, R.id.btn_fragment_dialog})
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
}
