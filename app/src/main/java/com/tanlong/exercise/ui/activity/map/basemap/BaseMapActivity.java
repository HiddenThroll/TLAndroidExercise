package com.tanlong.exercise.ui.activity.map.basemap;

import android.os.Bundle;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;

/**
 * 基础地图界面
 * Created by Administrator on 2016/8/31.
 */
public class BaseMapActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base_map);
    }
}
