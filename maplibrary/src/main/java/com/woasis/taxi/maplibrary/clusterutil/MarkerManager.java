package com.woasis.taxi.maplibrary.clusterutil;

import android.util.SparseArray;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.woasis.taxi.maplibrary.clusterutil.clustering.ClusterItem;
import com.woasis.taxi.maplibrary.impl.OnMarkerClickListener;
import com.woasis.taxi.maplibrary.model.MarkDataBase;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 追踪地图上缓存在Collection中的Marker。所有Marker相关事件由每个集合中独立管理的监听器代理。
 * 所有Marker操作都应该通过它的集合类，也就是说不能通过一个集合添加Marker，但调用该Marker自己的remove方法移除Marker
 */

public class MarkerManager implements BaiduMap.OnMarkerClickListener, BaiduMap.OnMarkerDragListener {

    private final BaiduMap mMap;

    private final Map<String, Collection> mNamedCollections = new HashMap<>();
    // 记录 Marker --> 缓存它的Collection
    private final Map<Marker, Collection> mAllMarkers = new HashMap<>();

    private OnMarkerClickListener mOnMarkerClickListener;

    public MarkerManager(BaiduMap map) {
        this.mMap = map;
    }

    public Collection newCollection() {
        return new Collection();
    }

    /**
     * Create a new named collection, which can later be looked up by {@link #getCollection(String)}
     * @param id a unique id for this collection.
     */
    public Collection newCollection(String id) {
        if (mNamedCollections.get(id) != null) {
            throw new IllegalArgumentException("collection id is not unique: " + id);
        }
        Collection collection = new Collection();
        mNamedCollections.put(id, collection);
        return collection;
    }

    /**
     * Gets a named collection that was created by {@link #newCollection(String)}
     * @param id the unique id for this collection.
     */
    public Collection getCollection(String id) {
        return mNamedCollections.get(id);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Collection collection = mAllMarkers.get(marker);
        if (collection != null && collection.mMarkerClickListener != null) {
            // you can set the click action
            return collection.mMarkerClickListener.onMarkerClick(marker);
        } else  {
            ; // click single maker out of cluster
            if(mOnMarkerClickListener != null) {
                MarkDataBase markDataBase = (MarkDataBase) marker.getExtraInfo()
                        .getSerializable(MarkDataBase.MARKER_DATA);
                mOnMarkerClickListener.onMarkerClick(markDataBase, marker);
            }
        }
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Collection collection = mAllMarkers.get(marker);
        if (collection != null && collection.mMarkerDragListener != null) {
            collection.mMarkerDragListener.onMarkerDragStart(marker);
        }
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        Collection collection = mAllMarkers.get(marker);
        if (collection != null && collection.mMarkerDragListener != null) {
            collection.mMarkerDragListener.onMarkerDrag(marker);
        }
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Collection collection = mAllMarkers.get(marker);
        if (collection != null && collection.mMarkerDragListener != null) {
            collection.mMarkerDragListener.onMarkerDragEnd(marker);
        }
    }

    /**
     * Removes a marker from its collection.
     *
     * @param marker the marker to remove.
     * @return true if the marker was removed.
     */
    public boolean remove(Marker marker) {
        Collection collection = mAllMarkers.get(marker);
        return collection != null && collection.remove(marker);
    }

    public class Collection {
        private final Set<Marker> mMarkers = new HashSet<>();//缓存Marker
        private BaiduMap.OnMarkerClickListener mMarkerClickListener;
        private BaiduMap.OnMarkerDragListener mMarkerDragListener;

        private final SparseArray<Marker> markerSparseArray = new SparseArray<>();//用于查找单独Marker

        public Collection() {
        }

        public Marker addMarker(MarkerOptions opts, ClusterItem clusterItem) {
            Marker marker = (Marker) mMap.addOverlay(opts);
            mMarkers.add(marker);
            mAllMarkers.put(marker, Collection.this);

            if (clusterItem != null) {
                if (clusterItem.getBundle() != null) {
                    int key = clusterItem.getBundle().getInt(ClusterItem.MARKER_IDENTITY, Integer.MIN_VALUE);
                    if (key != Integer.MIN_VALUE) {
                        markerSparseArray.put(key, marker);
                    }
                }
            }

            return marker;
        }

        public boolean remove(Marker marker) {
            if (mMarkers.remove(marker)) {
                mAllMarkers.remove(marker);
                marker.remove();
                return true;
            }
            return false;
        }

        public void clear() {
            for (Marker marker : mMarkers) {
                marker.remove();
                mAllMarkers.remove(marker);
            }
            mMarkers.clear();
            markerSparseArray.clear();
        }

        public java.util.Collection<Marker> getMarkers() {
            return Collections.unmodifiableCollection(mMarkers);
        }

        public void setOnMarkerClickListener(BaiduMap.OnMarkerClickListener markerClickListener) {
            mMarkerClickListener = markerClickListener;
        }

        public void setOnMarkerDragListener(BaiduMap.OnMarkerDragListener markerDragListener) {
            mMarkerDragListener = markerDragListener;
        }

        public SparseArray<Marker> getMarkerSparseArray() {
            return markerSparseArray;
        }
    }

    public void setmOnMarkerClickListener(OnMarkerClickListener mOnMarkerClickListener) {
        this.mOnMarkerClickListener = mOnMarkerClickListener;
    }
}
