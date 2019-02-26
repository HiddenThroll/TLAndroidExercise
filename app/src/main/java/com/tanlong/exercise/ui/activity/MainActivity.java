package com.tanlong.exercise.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tanlong.exercise.R;
import com.tanlong.exercise.ndk.NdkHelper;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.activity.databinding.DataBindingCategoryActivity;
import com.tanlong.exercise.ui.activity.decode.DecodeCategoryActivity;
import com.tanlong.exercise.ui.activity.download.DownloadAppActivity;
import com.tanlong.exercise.ui.activity.handler.HandlerExerciseActivity;
import com.tanlong.exercise.ui.activity.hotfix.HotFixSampleActivity;
import com.tanlong.exercise.ui.activity.ipc.IPCCategoryActivity;
import com.tanlong.exercise.ui.activity.map.MapCategoryActivity;
import com.tanlong.exercise.ui.activity.plugin.PluginExerciseActivity;
import com.tanlong.exercise.ui.activity.updateversion.UpdateVersionActivity;
import com.tanlong.exercise.ui.activity.view.ViewCategoryActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.lv_activity_category)
    ListView mLvCategory;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);

        initView();
        String[] items = getResources().getStringArray(R.array.main_category);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_category, items);
        mLvCategory.setAdapter(adapter);

//        test();
    }

    public void initView() {
        mIvBack.setVisibility(View.INVISIBLE);
        mTvTitle.setText(R.string.app_name);

        Logger.e("invoke ndk " + NdkHelper.addFromC(1, 2));
    }

    @OnItemClick(R.id.lv_activity_category)
    public void onItemClick(int position) {
        Intent intent = new Intent();
        switch (position) {
            case 0:
                intent.setClass(this, ViewCategoryActivity.class);
                break;
            case 1:
                intent.setClass(this, MapCategoryActivity.class);
                break;
            case 2:
                intent.setClass(this, IPCCategoryActivity.class);
                break;
            case 3:
                intent.setClass(this, DataBindingCategoryActivity.class);
                break;
            case 4:
                intent.setClass(this, DownloadAppActivity.class);
                break;
            case 5:
                intent.setClass(this, HandlerExerciseActivity.class);
                break;
            case 6:
                intent.setClass(this, DecodeCategoryActivity.class);
                break;
            case 7:
                intent.setClass(this, PluginExerciseActivity.class);
                break;
            case 8:
                intent.setClass(this, HotFixSampleActivity.class);
                break;
            case 9:
                intent.setClass(this, UpdateVersionActivity.class);
                break;
            default:
                break;
        }

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            showShortMessage(R.string.no_available_activity);
        }
    }

    private void test() {
        try {
            Log.e(TAG, "files-path " + getFilesDir().getAbsolutePath());
            Log.e(TAG, "cache-path " + getCacheDir().getAbsolutePath());
            Log.e(TAG, "external-path " + Environment.getExternalStorageDirectory().getAbsolutePath());
            Log.e(TAG, "external-files-path null " + getExternalFilesDir(null).getAbsolutePath());
            Log.e(TAG, "external-files-path Environment.DIRECTORY_DOWNLOADS " + getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
            Log.e(TAG, "external-cache-path " + getExternalCacheDir().getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
