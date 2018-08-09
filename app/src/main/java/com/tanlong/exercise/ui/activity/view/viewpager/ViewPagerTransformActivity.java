package com.tanlong.exercise.ui.activity.view.viewpager;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityViewpagerTransformBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.util.DisplayUtil;

/**
 * @author 龙
 */
public class ViewPagerTransformActivity extends BaseActivity {
    ActivityViewpagerTransformBinding binding;
    int[] imges = new int[] {
      R.mipmap.ic_vehicle,
      R.mipmap.ic_vehicle,
      R.mipmap.ic_vehicle,
      R.mipmap.ic_vehicle
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_viewpager_transform);
        //设置缓存页面数
        binding.viewPager.setOffscreenPageLimit(3);
        //设置Item之间距离
        binding.viewPager.setPageMargin(DisplayUtil.dip2px(ViewPagerTransformActivity.this, 8));

        binding.viewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = binding.viewPager.getWidth();
                if (width != 0) {

                    Log.e("test", "width is " + width);
                    //设置ViewPager的左右margin
                    int divider = DisplayUtil.dip2px(ViewPagerTransformActivity.this, 8);
                    //margin = viewPager宽度/2 - 图片宽度/2 - 间隔
                    int margin = width / 2 - 350/2 - divider;
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.viewPager.getLayoutParams();
                    params.setMargins(margin, 0, margin, 0);
                    binding.viewPager.setLayoutParams(params);

                    binding.viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
        Log.e("test", "setAdapter");
        binding.viewPager.setAdapter(adapter);
    }

    private PagerAdapter adapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return imges.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = new ImageView(ViewPagerTransformActivity.this);
            imageView.setImageResource(imges[position]);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    };
}
