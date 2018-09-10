package com.tanlong.exercise.ui.activity.ipc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.activity.contentprovider.BookProviderActivity;
import com.tanlong.exercise.ui.activity.contentprovider.LogProviderActivity;
import com.tanlong.exercise.ui.activity.ipc.aidl.AIDLIPCActivity;
import com.tanlong.exercise.ui.activity.ipc.messenger.MessengerIPCActivity;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 *
 * Created by 龙 on 2017/4/5.
 */

public class IPCCategoryActivity extends BaseActivity {
    @BindView(R.id.lv_activity_category)
    ListView mLvCategory;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);

        initView();
        String[] items = getResources().getStringArray(R.array.ipc_category);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_category, items);
        mLvCategory.setAdapter(adapter);
    }

    public void initView() {
        mTvTitle.setText(R.string.ipc_exercise);
    }

    @OnClick(R.id.iv_back)
    public void onBack() {
        finish();
    }

    @OnItemClick(R.id.lv_activity_category)
    public void onItemClick(int position) {
        Intent intent = new Intent();
        switch (position) {
            case 0:
                intent.setClass(this, MessengerIPCActivity.class);
                break;
            case 1:
                intent.setClass(this, AIDLIPCActivity.class);
                break;
            case 2:
                intent.setClass(this, BookProviderActivity.class);
                break;
            case 3:
                intent.setClass(this, LogProviderActivity.class);
                break;
            default:
                break;
        }

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            showShortMessage(R.string.no_available_activity);
        }
    }
}
