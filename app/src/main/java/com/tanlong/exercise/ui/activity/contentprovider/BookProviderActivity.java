package com.tanlong.exercise.ui.activity.contentprovider;

import android.content.ContentValues;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.tanlong.exercise.R;
import com.tanlong.exercise.aidl.Book;
import com.tanlong.exercise.contentprovider.BookProvider;
import com.tanlong.exercise.databinding.ActivityBookProviderBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.wxlz.woasislog.provider.LogProvider;

import java.util.Random;

/**
 * @author 龙
 */
public class BookProviderActivity extends BaseActivity {
    ActivityBookProviderBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_provider);
        binding.setActivity(this);
    }

    public void addBook() {
        ContentValues contentValues = new ContentValues();
        int id = new Random().nextInt(900000);
        contentValues.put("_id", id);
        contentValues.put("name", "Android开发艺术探索" + id);
        getContentResolver().insert(BookProvider.BOOK_CONTENT_URI, contentValues);


        addInfo();
    }

    public void addInfo() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("time", "1111");
        contentValues.put("userid", "2");
        contentValues.put("name", "addInfo");
        contentValues.put("value", "addValue");
        contentValues.put("type", 1);
        contentValues.put("level", 1);

        getContentResolver().insert(LogProvider.LOG_CONTENT_URI, contentValues);
    }

    public void queryBooks() {
        Cursor cursor = getContentResolver().query(BookProvider.BOOK_CONTENT_URI, new String[]{"_id", "name"},
                null, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            Book book = new Book(name, 1);
            Logger.e("book is " + new Gson().toJson(book));
        }
        cursor.close();

        queryLog();
    }

    private void queryLog() {
        Cursor cursor = getContentResolver().query(LogProvider.LOG_CONTENT_URI, null,
                null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            Logger.e("id is " + id);
        }
        cursor.close();
    }
}
