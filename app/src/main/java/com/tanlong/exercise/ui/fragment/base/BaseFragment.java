package com.tanlong.exercise.ui.fragment.base;

import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/10/26.
 */

public class BaseFragment extends Fragment {

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
}
