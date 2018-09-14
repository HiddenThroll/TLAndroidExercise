package com.tanlong.exercise.ui.activity.download;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityDownloadAppBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;

/**
 * @author 龙
 */
public class DownloadAppActivity extends BaseActivity {
    ActivityDownloadAppBinding binding;

    AppDownloadManager mDownloadManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_download_app);
        binding.setActivity(this);
        mDownloadManager = new AppDownloadManager(this, "test.apk");
        
    }

    public void startDownload() {
        mDownloadManager.downloadApk("http://qiwangguanjia.cn//attachment//appupgrade//5b9916a19d559.apk", "下载标题", "下载描述");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDownloadManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDownloadManager.onPause();
    }
}
