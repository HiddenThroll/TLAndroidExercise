package com.tanlong.exercise.ui.activity.view.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.fragment.DataRetainFragment;
import com.tanlong.exercise.util.LogTool;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 数据加载Activity, 加载数据显示对话框时不停旋转屏幕
 * Created by Administrator on 2016/11/12.
 */

public class LoadDataActivity extends BaseActivity {

    @Bind(R.id.lv_data)
    ListView lvData;

    private ArrayAdapter mAdapter;
    private List<String> mDatas;
    private DataRetainFragment<LoadDataTask> dataRetainFragment;
    private FragmentManager fragmentManager;
    private LoadDataTask loadDataTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogTool.e(TAG, "onCreate");
        setContentView(R.layout.activity_load_data);
        ButterKnife.bind(this);

        fragmentManager = getSupportFragmentManager();
        dataRetainFragment = (DataRetainFragment<LoadDataTask>)fragmentManager.findFragmentByTag(DataRetainFragment.TAG);
        if (dataRetainFragment == null) {//第一次创建, 添加到Activity
            dataRetainFragment = new DataRetainFragment<>();
            fragmentManager.beginTransaction().add(dataRetainFragment, DataRetainFragment.TAG).commit();
        }

        loadDataTask = dataRetainFragment.getData();
        if (loadDataTask != null) {//是否有缓存的加载任务
            LogTool.e(TAG, "有缓存的加载任务");
            loadDataTask.setActivity(this);// 重新创建对话框并显示
        } else {
            loadDataTask = new LoadDataTask(this);
            dataRetainFragment.setData(loadDataTask);
            loadDataTask.execute();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Activity销毁时设置Task中Activity为null, 取消对话框显示
        loadDataTask.setActivity(null);
        super.onSaveInstanceState(outState);
        LogTool.e(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogTool.e(TAG, "onRestoreInstanceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogTool.e(TAG, "OnDestroy");
    }

    public void onTaskComplete() {
        try {
            mDatas = loadDataTask.getItems();
            mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mDatas);
            lvData.setAdapter(mAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
