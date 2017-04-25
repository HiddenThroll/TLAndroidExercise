package com.tanlong.exercise.ui.activity.view.customview;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.fragment.dialog.SignDialogFragment;
import com.tanlong.exercise.ui.view.customview.CustomSignView;
import com.tanlong.exercise.util.ImageTool;

import java.io.File;
import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 龙 on 2017/4/24.
 */

public class CustomSignViewActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;
    @Bind(R.id.cv_sign)
    CustomSignView mCvSign;
    @Bind(R.id.btn_reset)
    Button btnReset;
    @Bind(R.id.btn_save)
    Button btnSave;

    private String mSignFilePath = "pic_sign";
    private File mSignFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_custom_sign_view);
        ButterKnife.bind(this);

        tvTitle.setText(R.string.custom_sign_view);
    }

    @OnClick({R.id.iv_back, R.id.btn_help, R.id.btn_reset, R.id.btn_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                break;
            case R.id.btn_reset:
                mCvSign.resetSign();
                break;
            case R.id.btn_save:
                saveSign();
                break;
        }
    }

    MyHandler handler = new MyHandler(this);

    private void saveSign() {
        final Bitmap bitmap = mCvSign.getSignBitmap();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mSignFile = ImageTool.saveBitmapToFile(CustomSignViewActivity.this, mSignFilePath,
                        bitmap);
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    private static class MyHandler extends Handler {
        WeakReference<CustomSignViewActivity> reference;

        private MyHandler(CustomSignViewActivity activity) {
            reference = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            CustomSignViewActivity activity = reference.get();
            switch (msg.what) {
                case 1:
                    activity.showSignDialog();
                    break;
            }
        }
    }

    private void showSignDialog() {
        SignDialogFragment fragment = SignDialogFragment.newInstance(mSignFile.getAbsolutePath());
        fragment.show(getSupportFragmentManager(), "");
    }
}
