package com.tanlong.exercise.ui.activity.view.fragment.dataload;

import android.os.AsyncTask;

import com.tanlong.exercise.ui.fragment.dialog.LoadingDialogFragment;
import com.tanlong.exercise.util.LogTool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 加载数据异步任务,与LoadDataActivity配合使用
 * Created by Administrator on 2016/11/12.
 */

public class LoadDataTask extends AsyncTask<Void, Void, Void> {
    private final String TAG = getClass().getSimpleName();
    private LoadDataActivity curActivity;

    private boolean isCompleted;

    private LoadingDialogFragment loadDialog;

    private List<String> items;

    public LoadDataTask(LoadDataActivity curActivity) {
        this.curActivity = curActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadDialog = new LoadingDialogFragment();
        loadDialog.show(curActivity.getSupportFragmentManager(), LoadingDialogFragment.TAG);
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        items = new ArrayList<>(Arrays.asList("Fragment保存数据", "加载数据时旋转屏幕"));
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        isCompleted = true;

        if (curActivity != null)
            curActivity.onTaskComplete();

        if (loadDialog != null)
            loadDialog.dismiss();
    }

    /**
     * 设置Activity，因为Activity会一直变化
     *
     * @param activity
     */
    public void setActivity(LoadDataActivity activity) {
        LogTool.e(TAG, "setActivity");
        // 如果上一个Activity销毁，将与上一个Activity绑定的DialogFragment销毁
        if (activity == null) {
            LogTool.e(TAG, "curActivity销毁, 取消loadDialog");
            loadDialog.dismiss();
        }

        // 设置为当前的Activity
        this.curActivity = activity;
        // 开启一个与当前Activity绑定的等待框
        if (activity != null && !isCompleted) {
            LogTool.e(TAG, "开启一个与当前Activity绑定的等待框");
            loadDialog = new LoadingDialogFragment();
            loadDialog.show(curActivity.getSupportFragmentManager(), "LOADING");
        }

        // 如果完成，通知Activity
        if (isCompleted && curActivity != null) {
            curActivity.onTaskComplete();
        }
    }

    public List<String> getItems() {
        return items;
    }
}
