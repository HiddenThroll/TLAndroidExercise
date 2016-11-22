package com.tanlong.exercise.ui.activity.map.basemap;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Polyline;
import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.util.LogTool;
import com.tanlong.exercise.util.ToastHelp;
import com.tanlong.maplibrary.BaiduMapService;
import com.tanlong.maplibrary.baiduImpl.OnLocationListener;
import com.tanlong.maplibrary.model.LatLngData;
import com.tanlong.maplibrary.service.BDGeometryService;
import com.tanlong.maplibrary.service.BDMarkerService;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 基础地图界面
 * Created by Administrator on 2016/8/31.
 */
public class BaseMapActivity extends BaseActivity {

    @Bind(R.id.mv_baidu_map)
    MapView mBaiduMap;

    BaiduMapService mMapService;
    BDGeometryService geometryService;
    BDMarkerService markerService;
    int fillColor;
    int strokeColor;
    int tempColor;
    List<LatLngData> points;
    List<Integer> colors;

    View locationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base_map);
        ButterKnife.bind(this);

        initData();
        initListener();

        startLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBaiduMap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBaiduMap.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapService.closeTimingLocation();
        mBaiduMap.onDestroy();
    }

    private void initData() {
        mMapService = new BaiduMapService(this);
        mMapService.initBaiduMap(mBaiduMap);
        geometryService = new BDGeometryService(this, mBaiduMap);
        markerService = new BDMarkerService(this, mBaiduMap);

        fillColor = ContextCompat.getColor(BaseMapActivity.this, R.color.color_282c76);
        strokeColor = ContextCompat.getColor(BaseMapActivity.this, R.color.color_86d0ab);
        tempColor = ContextCompat.getColor(BaseMapActivity.this, R.color.color_fb9b10);
        colors = new ArrayList<>();
        colors.add(fillColor);
        colors.add(strokeColor);
        colors.add(tempColor);

        points = new ArrayList<>();
        points.add(new LatLngData(29.6287780000, 106.4977940000, LatLngData.LatLngType.BAIDU));
        points.add(new LatLngData(29.6284950000, 106.4980450000, LatLngData.LatLngType.BAIDU));
        points.add(new LatLngData(29.6283700000, 106.4975330000, LatLngData.LatLngType.BAIDU));
        points.add(new LatLngData(29.6279380000, 106.4975870000, LatLngData.LatLngType.BAIDU));
        points.add(new LatLngData(29.6279540000, 106.4982610000, LatLngData.LatLngType.BAIDU));

        locationView = LayoutInflater.from(this).inflate(R.layout.layout_location, null);
    }

    private void initListener() {

    }

    private void startLocation() {
        mMapService.startLocationAndMoveCenter(new OnLocationListener() {
            @Override
            public void onLocation(BDLocation bdLocation) {
                LatLngData center = new LatLngData(bdLocation.getLatitude(), bdLocation.getLongitude(),
                        LatLngData.LatLngType.BAIDU);
                markerService.addMarker(locationView, center, null);
            }

            @Override
            public void onLocationFailed() {
                LogTool.e(TAG, "定位失败");
            }
        });
    }

    private void drawCircle(LatLngData center, int fillColor, int radius, int strokeColor,
                            int strokeWidth, Bundle bundle) {
        geometryService.drawCircle(center, fillColor, radius, strokeColor, strokeWidth, bundle);
    }

    private void drawLines(List<LatLngData> points, List<Integer> colors, int width,
                           boolean isDottedLine, Bundle bundle) {
        try {
            Polyline polyline = geometryService.drawLines(points, colors, width, isDottedLine, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawTextureLines() {
        List<String> drawables = new ArrayList<>();
        drawables.add("icon_road_red_arrow.png");
        drawables.add("icon_road_green_arrow.png");
        drawables.add("icon_road_blue_arrow.png");

        List<Integer> index = new ArrayList<>();
        index.add(0);
        index.add(0);
        index.add(1);
        index.add(2);

        try {
            Polyline polyline = geometryService.drawTextureLines(points, drawables, index, 12, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showInfoWindow(LatLngData tar, int yOff) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_info_window, null);
        final TextView tvName1 = (TextView) view.findViewById(R.id.tv_info_1);
        EditText etContent = (EditText) view.findViewById(R.id.et_info_2);
        tvName1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastHelp.showShortMsg(BaseMapActivity.this, tvName1.getText().toString());
            }
        });
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ToastHelp.showShortMsg(BaseMapActivity.this, "输入的内容是" + s.toString());
            }
        });

        mMapService.showInfoWindow(tar, view, yOff);
    }
}
