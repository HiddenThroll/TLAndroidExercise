package com.tanlong.exercise.ui.activity.view.vieweffect;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.util.ToastHelp;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_palette_exercise);
        ButterKnife.bind(this);

        initData();
        initView();
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
        mAdapter = new PalettePagerAdapter(viewList, imgList);
        vpPalette.setAdapter(mAdapter);
    }

    @OnClick({R.id.iv_back, R.id.btn_help})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                break;
        }
    }

    private class PalettePagerAdapter extends PagerAdapter {

        private List<View> viewList;
        private List<Integer> imgList;

        public PalettePagerAdapter(List<View> viewList, List<Integer> imgList) {
            this.viewList = viewList;
            this.imgList = imgList;
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
            ivSrc.setImageResource(imgList.get(position % viewList.size()));

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    setColorByPalette(imgList.get(position % viewList.size()));
                }
            });

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = viewList.get(position % viewList.size());
            container.removeView(view);
        }
    }

    private void setColorByPalette(int img) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), img);
        new Palette.Builder(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch = palette.getDarkMutedSwatch();
                if (swatch != null) {
                    tbToolbar.setBackgroundColor(swatch.getRgb());

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(swatch.getRgb());
                    }
                } else {
                    ToastHelp.showShortMsg(getApplicationContext(), "获取颜色失败");
                }
            }
        });
    }
}
