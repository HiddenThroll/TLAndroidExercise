package com.tanlong.exercise.ui.activity.updateversion;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.updateversion.manager.AppDownloadManager;
import com.tanlong.exercise.databinding.ActivityUpdateVersionBinding;

/**
 * @author 龙
 */
public class UpdateVersionActivity extends BaseActivity {

    private final String APK_URL = "https://imtt.dd.qq.com/16891/F518B8BF317A7796337C12B11F9B76B8.apk?fsname=com.cqfrozen.jsh_1.2.10_30.apk";
    /**
     * FileProvider 的 Auth
     */
    private final String FILE_PROVIDER_AUTH = "com.tanlong.exercise.fileProvider";

    ActivityUpdateVersionBinding binding;
    AppDownloadManager appDownloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_version);
        binding.setActivity(this);

        appDownloadManager = new AppDownloadManager(this, "test.apk", FILE_PROVIDER_AUTH);
    }

    public void startDownload() {
        appDownloadManager.downloadApk(APK_URL, "下载标题", "下载链接");
    }

    @Override
    protected void onResume() {
        super.onResume();
        appDownloadManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        appDownloadManager.onPause();
    }
}
