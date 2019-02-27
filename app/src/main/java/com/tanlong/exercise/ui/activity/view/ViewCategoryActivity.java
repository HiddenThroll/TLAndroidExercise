package com.tanlong.exercise.ui.activity.view;

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
import com.tanlong.exercise.ui.activity.view.constraintlayout.ConstraintLayoutCategoryActivity;
import com.tanlong.exercise.ui.activity.view.coordinatorlayout.CoordinatorLayoutCategoryActivity;
import com.tanlong.exercise.ui.activity.view.customview.CustomViewCategoryActivity;
import com.tanlong.exercise.ui.activity.view.customviewgroup.CustomViewGroupCategoryActivity;
import com.tanlong.exercise.ui.activity.view.fragment.FragmentCategoryActivity;
import com.tanlong.exercise.ui.activity.view.image.ImageLoadCategoryActivity;
import com.tanlong.exercise.ui.activity.view.listview.ListViewCategoryActivity;
import com.tanlong.exercise.ui.activity.view.notification.NotificationCategoryActivity;
import com.tanlong.exercise.ui.activity.view.optimization.ViewOptimizationCategoryActivity;
import com.tanlong.exercise.ui.activity.view.recyclerview.RecyclerViewCategoryActivity;
import com.tanlong.exercise.ui.activity.view.surfaceview.SurfaceViewCategoryActivity;
import com.tanlong.exercise.ui.activity.view.vieweffect.ViewEffectCategory;
import com.tanlong.exercise.ui.activity.view.viewpager.ViewPagerCategoryActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by 龙 on 2016/6/24.
 */
public class ViewCategoryActivity extends BaseActivity {

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
        String[] items = getResources().getStringArray(R.array.view_category);
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
                intent.setClass(this, CustomViewCategoryActivity.class);
                break;
            case 1:
                intent.setClass(this, CustomViewGroupCategoryActivity.class);
                break;
            case 2:
                intent.setClass(this, ListViewCategoryActivity.class);
                break;
            case 3:
                intent.setClass(this, ViewEffectCategory.class);
                break;
            case 4:
                intent.setClass(this, FragmentCategoryActivity.class);
                break;
            case 5:
                intent.setClass(this, ViewPagerCategoryActivity.class);
                break;
            case 6:
                intent.setClass(this, RecyclerViewCategoryActivity.class);
                break;
            case 7:
                intent.setClass(this, SurfaceViewCategoryActivity.class);
                break;
            case 8:
                intent.setClass(this, AnimatorCategoryActivity.class);
                break;
            case 9:
                intent.setClass(this, SimpleCardViewActivity.class);
                break;
            case 10:
                intent.setClass(this, NotificationCategoryActivity.class);
                break;
            case 11:
                intent.setClass(this, ConstraintLayoutCategoryActivity.class);
                break;
            case 12:
                intent.setClass(this, CoordinatorLayoutCategoryActivity.class);
                break;
            case 13:
                intent.setClass(this, ImageLoadCategoryActivity.class);
                break;
            case 14:
                intent.setClass(this, ViewOptimizationCategoryActivity.class);
                break;
            default:
                break;
        }

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            overridePendingTransition(R.anim.anim_activity_in_from_right, R.anim.anim_activity_out_to_left);
        } else {
            showShortMessage(R.string.no_available_activity);
        }
    }
}
