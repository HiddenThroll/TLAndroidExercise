package com.tanlong.exercise.ui.activity.view.vieweffect;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by 龙 on 2016/10/24.
 */

public class ViewEffectCategory extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lv_activity_category)
    ListView lvActivityCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);

        initView();

        String[] items = getResources().getStringArray(R.array.view_effect_category);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_category, items);
        lvActivityCategory.setAdapter(adapter);
    }

    private void initView() {
        tvTitle.setText(R.string.view_effect_exercise);
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
                intent.setClass(this, MaskEffectActivity.class);
                break;
            case 1:
                intent.setClass(this, ShadowBackgroundActivity.class);
                break;
            case 2:
                intent.setClass(this, RoundImageByXfermodeActivity.class);
                break;
            case 3:
                intent.setClass(this, ScratchCardActivity.class);
                break;
            case 4:
                intent.setClass(this, PaletteExerciseActivity.class);
                break;
            case 5:
                intent.setClass(this, TintingAndClippingActivity.class);
                break;
        }

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            showShortMessage(R.string.no_available_activity);
        }
    }
}
