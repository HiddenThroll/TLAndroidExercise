package com.tanlong.exercise.ui.activity.contentprovider;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;

import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityBookProviderBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;

/**
 * @author 龙
 */
public class BookProviderActivity extends BaseActivity {
    ActivityBookProviderBinding binding;
    private final String AUTHORITIES = "com.tanlong.exercise.book.provider";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_provider);

        Uri uri = Uri.parse("content://" + AUTHORITIES);
        getContentResolver().query(uri, null, null, null,null);
        getContentResolver().query(uri, null, null, null,null);
        getContentResolver().query(uri, null, null, null,null);
    }

}
