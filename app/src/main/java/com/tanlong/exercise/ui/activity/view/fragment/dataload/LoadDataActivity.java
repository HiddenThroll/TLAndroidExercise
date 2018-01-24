package com.tanlong.exercise.ui.activity.view.fragment.dataload;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.fragment.DataRetainFragment;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;
import com.tanlong.exercise.util.LogTool;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 数据加载Activity, 加载数据显示对话框时不停旋转屏幕
 * Created by Administrator on 2016/11/12.
 */

public class LoadDataActivity extends BaseActivity {

    @BindView(R.id.lv_data)
    ListView lvData;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_help)
    Button btnHelp;

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

        tvTitle.setText("屏幕旋转时处理网络加载数据");
        btnHelp.setVisibility(View.VISIBLE);

        fragmentManager = getSupportFragmentManager();
        dataRetainFragment = (DataRetainFragment<LoadDataTask>) fragmentManager.findFragmentByTag(DataRetainFragment.TAG);
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


    @OnClick({R.id.iv_back, R.id.btn_help})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                showTips();
                break;
        }
    }

    private void showTips() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("1. 无UI的Fragment可以用来保存数据: \n")
                .append("1.1 覆写onCreate()方法，调用setRetainInstance(true)方法表示Activity重建时保存Fragment实例\n")
                .append("2. 使用无UI的Fragment保存AsyncTask，实现Activity重建时继续加载网络数据\n");

        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }
}
