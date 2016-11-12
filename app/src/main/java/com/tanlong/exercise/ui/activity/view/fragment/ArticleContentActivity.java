package com.tanlong.exercise.ui.activity.view.fragment;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.tanlong.exercise.ui.activity.base.SingleFragmentActivity;
import com.tanlong.exercise.ui.fragment.datatransfer.ArticleContentFragment;

/**
 * 文章内容Activity
 * Created by 龙 on 2016/11/4.
 */

public class ArticleContentActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        String content = getIntent().getStringExtra(ArticleContentFragment.ARGUMENT_CONTENT);
        if (TextUtils.isEmpty(content)) {
            content = "暂无内容";
        }
        return ArticleContentFragment.newInstance(content);
    }
}
