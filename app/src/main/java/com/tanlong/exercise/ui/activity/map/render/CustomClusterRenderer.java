package com.tanlong.exercise.ui.activity.map.render;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.tanlong.exercise.R;
import com.woasis.taxi.maplibrary.clusterutil.clustering.Cluster;
import com.woasis.taxi.maplibrary.clusterutil.clustering.ClusterItem;
import com.woasis.taxi.maplibrary.clusterutil.clustering.ClusterManager;
import com.woasis.taxi.maplibrary.clusterutil.clustering.view.DefaultClusterRenderer;

/**
 * Created by 龙 on 2017/10/13.
 */

public class CustomClusterRenderer<T extends ClusterItem> extends DefaultClusterRenderer<T> {
    private LayoutInflater layoutInflater;
    public CustomClusterRenderer(Context context, BaiduMap map, ClusterManager clusterManager) {
        super(context, map, clusterManager);
        layoutInflater = LayoutInflater.from(context);
    }

    protected void onBeforeClusterRendered(Cluster<T> cluster, MarkerOptions markerOptions) {
        View clusterView = layoutInflater.inflate(R.layout.layout_cluster, null, false);
        TextView tvInfo = (TextView) clusterView.findViewById(R.id.tv_cluster_info);
        tvInfo.setText(String.valueOf(cluster.getSize()));

        markerOptions.icon(BitmapDescriptorFactory.fromView(clusterView));
    }
}
