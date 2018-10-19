package com.tanlong.exercise.ui.activity.view.image;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;
import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityCustomImageLoaderBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.util.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class CustomImageLoaderActivity extends BaseActivity {

    ActivityCustomImageLoaderBinding binding;
    List<String> mListImgUrl;
    private ImageAdapter adapter;
    private GridView gvList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_custom_image_loader);
        initData();
        initView();
    }

    private void initData() {
        mListImgUrl = new ArrayList<>();
        mListImgUrl.add("http://img.dbohui.com//upload//20180827//20652ef8ce3a6bed18e78a87e6e1a370.jpg");
        mListImgUrl.add("http://img.dbohui.com//upload//20181005//11007b7598f844e41ae3c6c7b88d0692.jpg");
        mListImgUrl.add("http://img.dbohui.com///upload///20180922///d8a629d224984733eff877c0f59dd854.png");
        mListImgUrl.add("http://img.dbohui.com///upload///20180920///21e77436de25570cb86e4ee137c3d761.jpg");
        mListImgUrl.add("http://img.dbohui.com///upload///20180907///023e85c79da5e1d87e7ca4bf82797776.jpg");
        mListImgUrl.add("http://img.dbohui.com///upload///20180831///2d5a8cffa03c93ce76109be329b8f677.jpg");
        mListImgUrl.add("http://img.dbohui.com///upload///20180727///81be7be12533276bd575775c30f167d8.png");
        mListImgUrl.add("http://img.dbohui.com///upload///20181011///f387273d3bac6fc2d2c4a82bb442452c.png");
        mListImgUrl.add("http://img.dbohui.com///upload///20180726///c7fa90554d03372256465fbc4db98c13.jpg");
        mListImgUrl.add("http://img.dbohui.com///upload///20180922///e85ddd5d2647b9d1c46136abfb30c31c.png");
        mListImgUrl.add("http://img.dbohui.com///upload///20180910///b5f81a1007da8fcc2a33bdd317f88ccb.jpg");
        mListImgUrl.add("http://img.dbohui.com///upload///20180907///8bbdeb00011aa3847694a23e110daa85.jpg");

        adapter = new ImageAdapter(this, mListImgUrl);
    }

    private void initView() {
        gvList = binding.gvList;
        gvList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    mIsGridViewIdle = true;
                    adapter.notifyDataSetChanged();
                } else {
                    mIsGridViewIdle = false;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        gvList.setAdapter(adapter);
    }

    private boolean mIsGridViewIdle = true;

    private class ImageAdapter extends BaseAdapter {

        private List<String> urlList;
        private LayoutInflater mInflater;
        private ImageLoader imageLoader;

        public ImageAdapter(Context context, List<String> urlList) {
            this.urlList = urlList;
            mInflater = LayoutInflater.from(context);
            imageLoader = ImageLoader.build(context);
        }

        @Override
        public int getCount() {
            return urlList.size();
        }

        @Override
        public String getItem(int position) {
            return urlList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_image_loader, parent, false);
                holder = new ViewHolder();
                holder.imageView = convertView.findViewById(R.id.imageView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ImageView imageView = holder.imageView;
            String tag = (String) imageView.getTag();
            String url = getItem(position);

            if (!url.equals(tag)) {
                //不相等 使用默认图片
                imageView.setImageResource(R.drawable.bg_rectangle_cccccc);
            }
            if (mIsGridViewIdle) {
                imageView.setTag(url);
                imageLoader.bindBitmap(url, imageView);
            }

            return convertView;
        }

        private class ViewHolder {
            public ImageView imageView;

            public ViewHolder() {
            }
        }
    }
}
