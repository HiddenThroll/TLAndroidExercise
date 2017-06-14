package com.tanlong.exercise.ui.activity.view.vieweffect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;
import com.tanlong.exercise.util.ToastHelp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.media.CamcorderProfile.get;

/**
 * Created by Administrator on 2017/6/5.
 */

public class PaletteExerciseActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;
    @Bind(R.id.vp_palette)
    ViewPager vpPalette;
    @Bind(R.id.tb_toolbar)
    Toolbar tbToolbar;

    List<View> viewList;
    List<Integer> imgList;
    PalettePagerAdapter mAdapter;

    Palette mPalette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_palette_exercise);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initData();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        LayoutInflater inflater = LayoutInflater.from(this);
        imgList = new ArrayList<>();
        imgList.add(R.mipmap.ic_wallpaper_1);
        imgList.add(R.mipmap.ic_wallpaper_2);
        imgList.add(R.mipmap.ic_wallpaper_3);
        imgList.add(R.mipmap.ic_wallpaper_4);
        viewList = new ArrayList<>();

        for (int i = 0, size = imgList.size(); i < size; i++) {
            View contentView = inflater.inflate(R.layout.layout_palette_item, null);
            viewList.add(contentView);
        }

    }

    private void initView() {
        tvTitle.setText("根据图片动态改变颜色");
        btnHelp.setVisibility(View.VISIBLE);

        mAdapter = new PalettePagerAdapter(viewList, imgList, this);
        vpPalette.setAdapter(mAdapter);

        vpPalette.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Logger.e("position is " + position % viewList.size());
                int img = imgList.get(position % viewList.size());
                setColorByPalette(img);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.iv_back, R.id.btn_help})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                showTips();
                break;
        }
    }

    private void showTips() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("根据图片动态改变颜色：")
                .append("1. 使用Palette从Bitmap中获取颜色\n")
                .append("2. Toolbar.setBackgroundColor(int color)改变ToolBar背景色，getWindow().setStatusBarColor(int color)改变状态栏颜色")
                .append("");

        ShowTipsFragment.newInstance(stringBuilder.toString()).show(getSupportFragmentManager(), "");
    }

    private class PalettePagerAdapter extends PagerAdapter {

        private List<View> viewList;
        private List<Integer> imgList;
        private Context context;

        public PalettePagerAdapter(List<View> viewList, List<Integer> imgList, Context context) {
            this.viewList = viewList;
            this.imgList = imgList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = viewList.get(position % viewList.size());

            ImageView ivSrc = (ImageView) view.findViewById(R.id.iv_palette_src);
            Glide.with(context).load(imgList.get(position % viewList.size())).into(ivSrc);

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = viewList.get(position % viewList.size());
            container.removeView(view);
        }
    }

    /**
     * 从传入Img中通过调色板取出颜色，设置 StatusBar 颜色
     * @param img
     */
    private void setColorByPalette(final int img) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), img);
                new Palette.Builder(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        // 放主线程
                        mPalette = palette;
                        EventBus.getDefault().post(new SetStatusBarColorEvent());
                    }
                });
            }
        }).start();
    }

    private class SetStatusBarColorEvent {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setStatusBarColor(SetStatusBarColorEvent event) {
        if (mPalette == null) {
            Logger.e("mPalette is null");
            return;
        }
        Palette.Swatch swatch = mPalette.getDarkMutedSwatch();
        if (swatch != null) {
            tbToolbar.setBackgroundColor(swatch.getRgb());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(swatch.getRgb());
            }
        } else {
            ToastHelp.showShortMsg(getApplicationContext(), "获取颜色失败");
        }
    }
}
