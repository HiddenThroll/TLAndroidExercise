package com.tanlong.exercise.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.FragmentSimpleBinding;

public class LazyLoadFragment extends Fragment {
    private static final String TAG = "LazyLoadFragment" ;

    FragmentSimpleBinding binding;
    private static final String SELECT_COLOR = "select_color";

    private int color;

    public static LazyLoadFragment newInstance(int bgColor) {

        LazyLoadFragment fragment = new LazyLoadFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SELECT_COLOR, bgColor);
        fragment.setArguments(bundle);
        return fragment;
    }
    /**
     * Fragment是否对用户可见,可见时才加载数据
     */
    private boolean isUIVisiable = false;
    /**
     * setUserVisibleHint有可能在onCreateView之前调用,故需确保View已创建后再加载数据
     */
    private boolean isViewCreated = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e(TAG, "isVisibleToUser " + isVisibleToUser);
        if (isVisibleToUser) {
            isUIVisiable = true;
            //ViewPager会缓存至少相邻一页Fragment,当切换Fragment时,会调用setUserVisibleHint方法
            //这里再次触发懒加载
            lazyLoad();
        } else {
            isUIVisiable = false;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        color = ContextCompat.getColor(getActivity(), R.color.text_color_title);
        if (getArguments() != null) {
            color = getArguments().getInt(SELECT_COLOR, color);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_simple, container, false);
        binding.llContainer.setBackgroundColor(color);
        binding.swipeRefreshLayout.setOnRefreshListener(onRefreshListener);

        isViewCreated = true;
        Log.e(TAG, "onCreateView");
        lazyLoad();
        return binding.getRoot();
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Log.e(TAG, "onRefresh回调");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e("test", "结束动画");
                    binding.tvContent.setText("已加载新数据");
                    binding.swipeRefreshLayout.setRefreshing(false);
                }
            }, 2000L);

        }
    };

    private void lazyLoad() {
        Log.e(TAG, "isUIVisiable " + isUIVisiable + " isViewCreated " + isViewCreated);
        if (isUIVisiable && isViewCreated) {
            Log.e(TAG, "开始懒加载");
            isUIVisiable = false;
            isViewCreated = false;
            getData();
        }
    }

    private void getData() {
        binding.swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "开始刷新");
                binding.swipeRefreshLayout.setRefreshing(true);
                onRefreshListener.onRefresh();
            }
        });
    }
}
