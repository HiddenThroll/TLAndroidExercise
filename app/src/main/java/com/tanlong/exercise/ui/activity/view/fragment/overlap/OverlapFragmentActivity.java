package com.tanlong.exercise.ui.activity.view.fragment.overlap;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.orhanobut.logger.Logger;
import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityOverlapFragmentBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;

/**
 * Fragment重叠显示
 * @author 龙
 */
public class OverlapFragmentActivity extends BaseActivity {
    ActivityOverlapFragmentBinding binding;

    OverlapOneFragment oneFragment;
    OverlapTwoFragment twoFragment;
    OverlapThreeFragment threeFragment;
    TabLayout tabLayout;

    private final int INDEX_ONE = 0;
    private final int INDEX_TWO = 1;
    private final int INDEX_THREE = 2;
    private int curPosition = INDEX_ONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_overlap_fragment);
        binding.setActivity(this);

        initView();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        Logger.e("fragment is " + fragment.toString());
    }

    private void initView() {
        tabLayout = binding.tabLayout;
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showFragmentByPosition(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        showFragmentByPosition(curPosition);
    }

    private void showFragmentByPosition(int position) {
        hideAllFragment();
        switch (position) {
            case INDEX_ONE:
                showOneFragment();
                break;
            case INDEX_TWO:
                showTwoFragment();
                break;
            case INDEX_THREE:
                showThreeFragment();
                break;
            default:
                break;
        }
    }

    private void showOneFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (oneFragment == null) {
            oneFragment = OverlapOneFragment.newInstance();
            transaction.add(R.id.fl_container, oneFragment);
        } else {
            transaction.show(oneFragment);
        }
        transaction.commitAllowingStateLoss();
    }

    private void showTwoFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (twoFragment == null) {
            twoFragment = OverlapTwoFragment.newInstance();
            transaction.add(R.id.fl_container, twoFragment);
        } else {
            transaction.show(twoFragment);
        }
        transaction.commitAllowingStateLoss();
    }

    private void showThreeFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (threeFragment == null) {
            threeFragment = OverlapThreeFragment.newInstance();
            transaction.add(R.id.fl_container, threeFragment);
        } else {
            transaction.show(threeFragment);
        }
        transaction.commitAllowingStateLoss();
    }

    private void hideAllFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (oneFragment != null) {
            transaction.hide(oneFragment);
        }

        if (twoFragment != null) {
            transaction.hide(twoFragment);
        }

        if (threeFragment != null) {
            transaction.hide(threeFragment);
        }

        transaction.commitAllowingStateLoss();
    }
}
