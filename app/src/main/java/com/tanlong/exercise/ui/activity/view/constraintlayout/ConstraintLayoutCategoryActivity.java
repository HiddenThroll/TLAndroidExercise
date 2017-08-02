package com.tanlong.exercise.ui.activity.view.constraintlayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.activity.view.animator.AnimatorCategoryActivity;
import com.tanlong.exercise.ui.activity.view.cardview.SimpleCardViewActivity;
import com.tanlong.exercise.ui.activity.view.customview.CustomViewCategoryActivity;
import com.tanlong.exercise.ui.activity.view.customviewgroup.CustomViewGroupCategoryActivity;
import com.tanlong.exercise.ui.activity.view.fragment.FragmentCategoryActivity;
import com.tanlong.exercise.ui.activity.view.listview.ListViewCategoryActivity;
import com.tanlong.exercise.ui.activity.view.notification.NotificationCategoryActivity;
import com.tanlong.exercise.ui.activity.view.recyclerview.RecyclerViewCategoryActivity;
import com.tanlong.exercise.ui.activity.view.surfaceview.SurfaceViewCategoryActivity;
import com.tanlong.exercise.ui.activity.view.vieweffect.ViewEffectCategory;
import com.tanlong.exercise.ui.activity.view.viewpager.ViewPagerCategoryActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by 龙 on 2017/8/2.
 */

public class ConstraintLayoutCategoryActivity extends BaseActivity {

    @Bind(R.id.lv_activity_category)
    ListView mLvCategory;
    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);

        initView();
        String[] items = getResources().getStringArray(R.array.constraint_layout_category);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_category, items);
        mLvCategory.setAdapter(adapter);
    }

    public void initView() {
        mTvTitle.setText(R.string.view_exercise);
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
            intent.setClass(this, SimpleConstraintActivity.class);
        }

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            showShortMessage(R.string.no_available_activity);
        }
    }

}
