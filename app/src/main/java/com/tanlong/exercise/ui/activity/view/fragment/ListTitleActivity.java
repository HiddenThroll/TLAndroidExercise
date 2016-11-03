package com.tanlong.exercise.ui.activity.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tanlong.exercise.ui.activity.base.SingleFragmentActivity;
import com.tanlong.exercise.ui.fragment.ListTitleFragment;

/**
 * 文章列表Activity
 * Created by 龙 on 2016/11/3.
 */

public class ListTitleActivity extends SingleFragmentActivity {
    ListTitleFragment fragment;

    @Override
    protected Fragment createFragment() {
        fragment = ListTitleFragment.newInstance();
        return fragment;
    }
}
