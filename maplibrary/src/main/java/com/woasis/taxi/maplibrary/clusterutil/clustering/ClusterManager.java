/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

package com.woasis.taxi.maplibrary.clusterutil.clustering;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.Marker;
import com.woasis.taxi.maplibrary.clusterutil.MarkerManager;
import com.woasis.taxi.maplibrary.clusterutil.clustering.algo.Algorithm;
import com.woasis.taxi.maplibrary.clusterutil.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.woasis.taxi.maplibrary.clusterutil.clustering.algo.PreCachingAlgorithmDecorator;
import com.woasis.taxi.maplibrary.clusterutil.clustering.view.ClusterRenderer;
import com.woasis.taxi.maplibrary.clusterutil.clustering.view.DefaultClusterRenderer;
import com.woasis.taxi.maplibrary.impl.OnMapStatusChangeListener;
import com.woasis.taxi.maplibrary.impl.OnMarkerClickListener;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Groups many items on a map based on zoom level.
 * <p/>
 * ClusterManager should be added to the map
 * <li>
 */
public class ClusterManager<T extends ClusterItem> implements
        BaiduMap.OnMapStatusChangeListener, BaiduMap.OnMarkerClickListener {
    private final MarkerManager mMarkerManager;
    private final MarkerManager.Collection mMarkers;
    private final MarkerManager.Collection mClusterMarkers;

    private Algorithm<T> mAlgorithm;
    private final ReadWriteLock mAlgorithmLock = new ReentrantReadWriteLock();
    private ClusterRenderer<T> mRenderer;

    private BaiduMap mMap;
    private MapStatus mPreviousCameraPosition;
    private MapStatus mPreMapStatus;//Cluster使用的
    private MapStatus mOldMapStatus;// 正常缩放使用的
    private ClusterTask mClusterTask;
    private final ReadWriteLock mClusterTaskLock = new ReentrantReadWriteLock();

    private OnClusterItemClickListener<T> mOnClusterItemClickListener;
    private OnClusterInfoWindowClickListener<T> mOnClusterInfoWindowClickListener;
    private OnClusterItemInfoWindowClickListener<T> mOnClusterItemInfoWindowClickListener;
    private OnClusterClickListener<T> mOnClusterClickListener;

    private OnMapStatusChangeListener mOnMapStatusChangeListener;

    public ClusterManager(Context context, BaiduMap map) {
        this(context, map, new MarkerManager(map));
    }

    public ClusterManager(Context context, BaiduMap map, MarkerManager markerManager) {
        mMap = map;
        mMarkerManager = markerManager;
        mClusterMarkers = markerManager.newCollection();
        mMarkers = markerManager.newCollection();
        mRenderer = new DefaultClusterRenderer<T>(context, map, this);
        mAlgorithm = new PreCachingAlgorithmDecorator<T>(new NonHierarchicalDistanceBasedAlgorithm<T>());
        mClusterTask = new ClusterTask();
        mRenderer.onAdd();
    }

    public MarkerManager.Collection getMarkerCollection() {
        return mMarkers;
    }

    public MarkerManager.Collection getClusterMarkerCollection() {
        return mClusterMarkers;
    }

    public MarkerManager getMarkerManager() {
        return mMarkerManager;
    }

    public void setRenderer(ClusterRenderer<T> view) {
        mRenderer.setOnClusterClickListener(null);
        mRenderer.setOnClusterItemClickListener(null);
        mClusterMarkers.clear();
        mMarkers.clear();
        mRenderer.onRemove();
        mRenderer = view;
        mRenderer.onAdd();
        mRenderer.setOnClusterClickListener(mOnClusterClickListener);
        mRenderer.setOnClusterInfoWindowClickListener(mOnClusterInfoWindowClickListener);
        mRenderer.setOnClusterItemClickListener(mOnClusterItemClickListener);
        mRenderer.setOnClusterItemInfoWindowClickListener(mOnClusterItemInfoWindowClickListener);
        cluster();
    }

    public void setAlgorithm(Algorithm<T> algorithm) {
        mAlgorithmLock.writeLock().lock();
        try {
            if (mAlgorithm != null) {
                algorithm.addItems(mAlgorithm.getItems());
            }
            mAlgorithm = new PreCachingAlgorithmDecorator<T>(algorithm);
        } finally {
            mAlgorithmLock.writeLock().unlock();
        }
        cluster();
    }

    public void clearItems() {
        mAlgorithmLock.writeLock().lock();
        try {
            mAlgorithm.clearItems();
        } finally {
            mAlgorithmLock.writeLock().unlock();
        }
    }

    /**
     * 释放资源，不再显示地图时调用
     */
    public void releaseResource() {
        clearItems();
        mClusterMarkers.clear();
        mMarkers.clear();
    }

    /**
     * 添加要聚合的Marker集合
     * @param items
     */
    public void addItems(Collection<T> items) {
        mAlgorithmLock.writeLock().lock();
        try {
            mAlgorithm.addItems(items);
        } finally {
            mAlgorithmLock.writeLock().unlock();
        }

    }

    public void addItem(T myItem) {
        mAlgorithmLock.writeLock().lock();
        try {
            mAlgorithm.addItem(myItem);
        } finally {
            mAlgorithmLock.writeLock().unlock();
        }
    }

    public void removeItem(T item, Marker marker) {
        mAlgorithmLock.writeLock().lock();
        try {
            mAlgorithm.removeItem(item);
            mMarkerManager.remove(marker);
        } finally {
            mAlgorithmLock.writeLock().unlock();
        }
    }

    /**
     * 通过MarkerIdentify查找marker
     * @param markerIdentify -- ClusterItem的getBundle中设置
     * @return
     */
    public Marker getMarkerByIdentify(int markerIdentify) {
        return getMarkerCollection().getMarkerSparseArray().get(markerIdentify, null);
    }

    /**
     * Force a re-cluster. You may want to call this after adding new item(s).
     */
    public void cluster() {
        mClusterTaskLock.writeLock().lock();
        try {
            // Attempt to cancel the in-flight request.
            mClusterTask.cancel(true);
            mClusterTask = new ClusterTask();

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                mClusterTask.execute(mMap.getMapStatus().zoom);
            } else {
                mClusterTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mMap.getMapStatus().zoom);
            }
        } finally {
            mClusterTaskLock.writeLock().unlock();
        }
    }


    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {
        mOldMapStatus = mapStatus;
        if (mOnMapStatusChangeListener != null) {
            mOnMapStatusChangeListener.onMapStatusChangeStart(mapStatus);
        }
    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {
        if (mOnMapStatusChangeListener != null) {
            mOnMapStatusChangeListener.onMapStatusChange(mapStatus);
        }
    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        // Don't re-compute clusters if the map has just been panned/tilted/rotated.

        if (mOldMapStatus != null && mOnMapStatusChangeListener != null) {
            if (mOldMapStatus.zoom == mapStatus.zoom) {
                mOnMapStatusChangeListener.onMapMoveFinish(mapStatus);
            } else {
                mOnMapStatusChangeListener.onMapZoomChange(mOldMapStatus, mapStatus);
            }
        }

        MapStatus position = mMap.getMapStatus();
        if (mPreviousCameraPosition != null && mPreviousCameraPosition.zoom == position.zoom) {
            return;
        }
        mPreviousCameraPosition = mMap.getMapStatus();

        if (mPreMapStatus == null || mPreMapStatus.zoom != mapStatus.zoom) {
            cluster();
        }
        mPreMapStatus = mapStatus;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return getMarkerManager().onMarkerClick(marker);
    }

    /**
     * Runs the clustering algorithm in a background thread, then re-paints when results come back.
     */
    private class ClusterTask extends AsyncTask<Float, Void, Set<? extends Cluster<T>>> {
        @Override
        protected Set<? extends Cluster<T>> doInBackground(Float... zoom) {
            mAlgorithmLock.readLock().lock();
            try {
                return mAlgorithm.getClusters(zoom[0]);
            } finally {
                mAlgorithmLock.readLock().unlock();
            }
        }

        @Override
        protected void onPostExecute(Set<? extends Cluster<T>> clusters) {
            mRenderer.onClustersChanged(clusters);
        }
    }

    /**
     * Sets a callback that's invoked when a Cluster is tapped. Note: For this listener to function,
     * the ClusterManager must be added as a click listener to the map.
     */
    public void setOnClusterClickListener(OnClusterClickListener<T> listener) {
        mOnClusterClickListener = listener;
        mRenderer.setOnClusterClickListener(listener);
    }

    /**
     * Sets a callback that's invoked when a Cluster is tapped. Note: For this listener to function,
     * the ClusterManager must be added as a info window click listener to the map.
     */
    public void setOnClusterInfoWindowClickListener(OnClusterInfoWindowClickListener<T> listener) {
        mOnClusterInfoWindowClickListener = listener;
        mRenderer.setOnClusterInfoWindowClickListener(listener);
    }

    /**
     * Sets a callback that's invoked when an individual ClusterItem is tapped. Note: For this
     * listener to function, the ClusterManager must be added as a click listener to the map.
     */
    public void setOnClusterItemClickListener(OnClusterItemClickListener<T> listener) {
        mOnClusterItemClickListener = listener;
        mRenderer.setOnClusterItemClickListener(listener);
    }

    /**
     * Sets a callback that's invoked when an individual ClusterItem's Info Window is tapped. Note: For this
     * listener to function, the ClusterManager must be added as a info window click listener to the map.
     */
    public void setOnClusterItemInfoWindowClickListener(OnClusterItemInfoWindowClickListener<T> listener) {
        mOnClusterItemInfoWindowClickListener = listener;
        mRenderer.setOnClusterItemInfoWindowClickListener(listener);
    }

    public void setmOnMapStatusChangeListener(OnMapStatusChangeListener mOnMapStatusChangeListener) {
        this.mOnMapStatusChangeListener = mOnMapStatusChangeListener;
    }

    /**
     * 这里的Marker是指 Cluster 和 ClusterItem 之外的Marker
     * @param onSingleMarkerClickListener
     */
    public void setOnSingleMarkerClickListener(OnMarkerClickListener onSingleMarkerClickListener) {
        mMarkerManager.setmOnMarkerClickListener(onSingleMarkerClickListener);
    }

    /**
     * Called when a Cluster is clicked.
     */
    public interface OnClusterClickListener<T extends ClusterItem> {
        public boolean onClusterClick(Cluster<T> cluster);
    }

    /**
     * Called when a Cluster's Info Window is clicked.
     */
    public interface OnClusterInfoWindowClickListener<T extends ClusterItem> {
        public void onClusterInfoWindowClick(Cluster<T> cluster);
    }

    /**
     * Called when an individual ClusterItem is clicked.
     */
    public interface OnClusterItemClickListener<T extends ClusterItem> {
        public boolean onClusterItemClick(T item, Marker marker);
    }

    /**
     * Called when an individual ClusterItem's Info Window is clicked.
     */
    public interface OnClusterItemInfoWindowClickListener<T extends ClusterItem> {
        public void onClusterItemInfoWindowClick(T item);
    }

    public static int getMaxDistanceAtZoom() {
        return NonHierarchicalDistanceBasedAlgorithm.getMaxDistanceAtZoom();
    }

    public static void setMaxDistanceAtZoom(int maxDistanceAtZoom) {
        NonHierarchicalDistanceBasedAlgorithm.setMaxDistanceAtZoom(maxDistanceAtZoom);
    }
}
