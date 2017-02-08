package com.tanlong.exercise.ui.activity.view.fragment.datatransfer;

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

    @Override
    protected String getTips() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("1. Fragment通过setArguments(Bundle)方法设置参数，通过getArguments()方法获取参数\n")
                .append("2. 实现Fragment之间类似startActivityForResult方式传递数据方法：即AFragment启动BFragment，AFragment接收返回数据\n")
                .append("2.1 BFragment显示前调用Fragment.setTargetFragment(Fragment aFragment, int reqCode)方法设置启动它的Fragment\n")
                .append("2.2 BFragment通过getTargetFragment().onActivityResult(int requestCode, int resultCode, Intent data)设置返回结果并调用AFragment的onActivityResult()方法\n")
                .append("2.3 AFragment覆写onActivityResult()方法接收返回结果\n");
        return stringBuilder.toString();
    }
}
