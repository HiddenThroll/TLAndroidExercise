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

    private final String CURRENT_TAB_POSITION = "current_tab_position";
    private final int INDEX_ONE = 0;
    private final int INDEX_TWO = 1;
    private final int INDEX_THREE = 2;
    private int curPosition = INDEX_ONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_overlap_fragment);
        binding.setActivity(this);

        if (savedInstanceState != null) {
            curPosition = savedInstanceState.getInt(CURRENT_TAB_POSITION, INDEX_ONE);
        }

        initView();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        Logger.e("fragment is " + fragment.toString());
        if (oneFragment == null && fragment instanceof OverlapOneFragment) {
            Logger.e("onAttachFragment 赋值oneFragment");
            oneFragment = (OverlapOneFragment) fragment;
        } else if (twoFragment == null && fragment instanceof OverlapTwoFragment) {
            Logger.e("onAttachFragment 赋值twoFragment");
            twoFragment = (OverlapTwoFragment) fragment;
        } else if (threeFragment == null && fragment instanceof OverlapThreeFragment) {
            Logger.e("onAttachFragment 赋值threeFragment");
            threeFragment = (OverlapThreeFragment) fragment;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int curTabPosition = tabLayout.getSelectedTabPosition();
        outState.putInt(CURRENT_TAB_POSITION, curTabPosition);
        super.onSaveInstanceState(outState);
    }

    private void initView() {
        tabLayout = binding.tabLayout;
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Logger.e("select tab position is " + tab.getPosition());
                showFragmentByPosition(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Logger.e("reselect tab position is " + tab.getPosition());
                showFragmentByPosition(tab.getPosition());
            }
        });

        tabLayout.getTabAt(curPosition).select();
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
            Logger.e("oneFragment为null,新建");
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
            Logger.e("twoFragment为null,新建");
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
            Logger.e("threeFragment为null,新建");
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
