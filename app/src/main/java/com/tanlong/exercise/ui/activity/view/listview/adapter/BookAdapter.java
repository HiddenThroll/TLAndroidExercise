package com.tanlong.exercise.ui.activity.view.listview.adapter;

import android.content.Context;

import com.tanlong.exercise.R;
import com.tanlong.exercise.aidl.Book;
import com.tanlong.exercise.ui.activity.view.listview.adapter.base.CommonAdapter;
import com.tanlong.exercise.ui.activity.view.listview.adapter.base.ViewHolder;

import java.util.List;

/**
 * Created by 龙 on 2017/4/10.
 */

public class BookAdapter extends CommonAdapter<Book> {

    public BookAdapter(Context context, List<Book> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, Book item, int position) {
        helper.setText(R.id.tv_book_name, item.getName());
        helper.setText(R.id.tv_book_price, String.valueOf(item.getPrice()));
    }
}
