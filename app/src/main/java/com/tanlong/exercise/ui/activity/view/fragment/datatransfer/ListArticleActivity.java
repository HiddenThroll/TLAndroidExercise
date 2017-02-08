package com.tanlong.exercise.ui.activity.view.fragment.datatransfer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tanlong.exercise.ui.activity.base.SingleFragmentActivity;
import com.tanlong.exercise.ui.fragment.datatransfer.ListArticleFragment;
import com.tanlong.exercise.util.LogTool;

/**
 * 文章列表Activity
 * Created by 龙 on 2016/11/3.
 */

public class ListArticleActivity extends SingleFragmentActivity {
    ListArticleFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Fragment createFragment() {
        fragment = ListArticleFragment.newInstance();
        return fragment;
    }

    @Override
    protected String getTips() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("1. Fragment可以通过startActivityForResult()启动Activity\n")
                .append("2. Fragment在onActivityResult()中接收返回结果: \n")
                .append("2.1 启动的Activity/Fragment中，调用Activity.setResult(int resultCode, Intent data)方法设置返回结果\n")
                .append("2.2 本Fragment覆写onActivityResult()方法接收返回结果");
        return stringBuilder.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogTool.e(TAG, "onActivityResult requestCode is " + requestCode + " resultCode is " + resultCode);
    }
}
