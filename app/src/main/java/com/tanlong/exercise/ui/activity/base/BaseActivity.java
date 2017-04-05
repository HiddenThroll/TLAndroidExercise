package com.tanlong.exercise.ui.activity.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.tanlong.exercise.util.DisplayUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Activity基类
 * Created by Administrator on 2016/6/23.
 */
public class BaseActivity extends FragmentActivity {
    protected final String TAG = getClass().getSimpleName();
    private int mScreenWidth;
    private int mScreenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        mScreenWidth = DisplayUtil.getDisplay(this).x;
        mScreenHeight = DisplayUtil.getDisplay(this).y;
    }

    public int getmScreenWidth() {
        return mScreenWidth;
    }

    public int getmScreenHeight() {
        return mScreenHeight;
    }

    public void showShortMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showShortMessage(int msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showLongMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void showLongMessage(int msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
