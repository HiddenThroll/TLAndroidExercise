package com.tanlong.exercise.ui.fragment.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/10/26.
 */

public class BaseFragment extends Fragment {
    /**
     * Fragment统一使用的Context，避免getActivity()方法返回null引起的错误
     */
    protected Context mFragmentContext;

    protected final String TAG = getClass().getSimpleName();
    // Fragment + ViewPager练习使用
    private String updateContent = "0";
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragmentContext = context;
    }

    @Override
    public void onDetach() {
        mFragmentContext = null;
        super.onDetach();
    }

    public void showShortMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void showShortMessage(int msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void showLongMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    public void showLongMessage(int msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }
}
