/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

package com.woasis.taxi.maplibrary.clusterutil.clustering.algo;

import com.baidu.mapapi.model.LatLng;
import com.woasis.taxi.maplibrary.clusterutil.clustering.Cluster;
import com.woasis.taxi.maplibrary.clusterutil.clustering.ClusterItem;
import com.woasis.taxi.maplibrary.clusterutil.projection.Bounds;
import com.woasis.taxi.maplibrary.clusterutil.projection.Point;
import com.woasis.taxi.maplibrary.clusterutil.projection.SphericalMercatorProjection;
import com.woasis.taxi.maplibrary.clusterutil.quadtree.PointQuadTree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A simple clustering algorithm with O(nlog n) performance. Resulting clusters are not
 * hierarchical.
 * <p/>
 * High level algorithm:<br>
 * 1. Iterate over items in the order they were added (candidate clusters).<br>
 * 2. Create a cluster with the center of the item. <br>
 * 3. Add all items that are within a certain distance to the cluster. <br>
 * 4. Move any items out of an existing cluster if they are closer to another cluster. <br>
 * 5. Remove those items from the list of candidate clusters.
 * <p/>
 * Clusters have the center of the first element (not the centroid of the items within it).
 */
public class NonHierarchicalDistanceBasedAlgorithm<T extends ClusterItem> implements Algorithm<T> {
//    private final String TAG = getClass().getSimpleName();
    private static int maxDistanceAtZoom = 200; // essentially 100 dp.

    /**
     * Any modifications should be synchronized on mQuadTree.
     */
    private final Collection<QuadItem<T>> mItems = new ArrayList<QuadItem<T>>();

    /**
     * Any modifications should be synchronized on mQuadTree.
     */
    private final PointQuadTree<QuadItem<T>> mQuadTree = new PointQuadTree<QuadItem<T>>(0, 1, 0, 1);

    private static final SphericalMercatorProjection PROJECTION = new SphericalMercatorProjection(1);

    @Override
    public void addItem(T item) {
        final QuadItem<T> quadItem = new QuadItem<T>(item);
        synchronized (mQuadTree) {
            mItems.add(quadItem);
            mQuadTree.add(quadItem);
        }
    }

    @Override
    public void addItems(Collection<T> items) {
        for (T item : items) {
            addItem(item);
        }
    }

    @Override
    public void clearItems() {
        synchronized (mQuadTree) {
            mItems.clear();
            mQuadTree.clear();
        }
    }

    @Override
    public void removeItem(T item) {
        // TODO: delegate QuadItem#hashCode and QuadItem#equals to its item.
        throw new UnsupportedOperationException("NonHierarchicalDistanceBasedAlgorithm.remove not implemented");
    }

    /**
     * cluster算法核心
     * @param zoom map的级别
     * @return
     */
    @Override
    public Set<? extends Cluster<T>> getClusters(double zoom) {
        final int discreteZoom = (int) zoom;
        //可以进行聚合的距离
        final double zoomSpecificSpan = maxDistanceAtZoom / Math.pow(2, discreteZoom) / 256;
        //遍历QuadItem时保存被遍历过的Item
        final Set<QuadItem<T>> visitedCandidates = new HashSet<QuadItem<T>>();
        //保存要返回的cluster簇，每个cluster中包含若干个MyItem对象
        final Set<Cluster<T>> results = new HashSet<Cluster<T>>();
        //Key：Item对象 --> Value：此Item与所属的cluster中心点的距离
        final Map<QuadItem<T>, Double> distanceToCluster = new HashMap<QuadItem<T>, Double>();
        //Key：Item对象 --> Value：此Item所属的cluster
        final Map<QuadItem<T>, StaticCluster<T>> itemToCluster = new HashMap<>();

        synchronized (mQuadTree) {
            for (QuadItem<T> candidate : mItems) {//遍历所有的QuadItem
                if (visitedCandidates.contains(candidate)) {//如果该QuadItem已经属于其他的Cluster
                    // Candidate is already part of another cluster.
                    continue;
                }
                //以QuadItem的坐标为中心，创建一个矩形区域来框住其他的QuadItem
                Bounds searchBounds = createBoundsFromSpan(candidate.getPoint(), zoomSpecificSpan);
                Collection<QuadItem<T>> clusterItems;
                // search出该边界范围内的QuadItems，并存入clusterItems
                clusterItems = mQuadTree.search(searchBounds);
                if (clusterItems.size() == 1) {//只有当前Quaditem在范围内，将它添加到results中
                    // Only the current marker is in range. Just add the single item to the results.
                    results.add(candidate);
                    visitedCandidates.add(candidate);
                    distanceToCluster.put(candidate, 0d);
                    continue;//结束此次循环
                }
                // 指定边界范围内有多个QuadItem
                StaticCluster<T> cluster =
                        new StaticCluster<>(candidate.mClusterItem.getPosition());
                results.add(cluster);

                for (QuadItem<T> clusterItem : clusterItems) {
                    //此QuadItem与原Cluster中心的距离，（如果该Item之前被框住过）
                    Double existingDistance = distanceToCluster.get(clusterItem);
                    double distance = distanceSquared(clusterItem.getPoint(), candidate.getPoint());
                    if (existingDistance != null) {//之前被框住过
                        // Item already belongs to another cluster. Check if it's closer to this cluster.
                        if (existingDistance < distance) {//判断 之前距离 是否 小于 当前距离
                            continue;
                        }
                        // Move item to the closer cluster.
                        // 当前距离 小于 之前距离，将clusterItem从之前的Cluster中移除
                        itemToCluster.get(clusterItem).remove(clusterItem.mClusterItem);
                    }
                    distanceToCluster.put(clusterItem, distance);
                    cluster.add(clusterItem.mClusterItem);
                    itemToCluster.put(clusterItem, cluster);
                }
                visitedCandidates.addAll(clusterItems);
            }
        }
        return results;
    }

    @Override
    public Collection<T> getItems() {
        final List<T> items = new ArrayList<T>();
        synchronized (mQuadTree) {
            for (QuadItem<T> quadItem : mItems) {
                items.add(quadItem.mClusterItem);
            }
        }
        return items;
    }

    /**
     * 计算两点之间的距离（差值X平方 + 差值Y平方）
     * @param a
     * @param b
     * @return
     */
    private double distanceSquared(Point a, Point b) {
        return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
    }

    /**
     * 针对坐标点p，以给定的距离span生成一个矩形Bounds
     * @param p -- 矩形的中心点
     * @param span -- 给定的距离
     * @return
     */
    private Bounds createBoundsFromSpan(Point p, double span) {
        // TODO: Use a span that takes into account the visual size of the marker, not just its
        // LatLng.
        double halfSpan = span / 2;
        return new Bounds(
                p.x - halfSpan, p.x + halfSpan,
                p.y - halfSpan, p.y + halfSpan);
    }

    private static class QuadItem<T extends ClusterItem> implements PointQuadTree.Item, Cluster<T> {
        private final T mClusterItem;
        private final Point mPoint;
        private final LatLng mPosition;
        private Set<T> singletonSet;

        private QuadItem(T item) {
            mClusterItem = item;
            mPosition = item.getPosition();
            mPoint = PROJECTION.toPoint(mPosition);
            singletonSet = Collections.singleton(mClusterItem);
        }

        @Override
        public Point getPoint() {
            return mPoint;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Override
        public Set<T> getItems() {
            return singletonSet;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            QuadItem<?> quadItem = (QuadItem<?>) o;

            Point point = quadItem.getPoint();
            LatLng position = quadItem.getPosition();

            if (!mPoint.equals(point)) {
                return false;
            }

            if (Double.compare(position.latitude, mPosition.latitude) != 0 ||
                    Double.compare(position.longitude, position.longitude) != 0) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = mPoint != null ? mPoint.hashCode() : 0;
            result = 31 * result + (mPosition != null ? mPosition.hashCode() : 0);
            return result;
        }
    }

    public static int getMaxDistanceAtZoom() {
        return maxDistanceAtZoom;
    }

    public static void setMaxDistanceAtZoom(int maxDistanceAtZoom) {
        NonHierarchicalDistanceBasedAlgorithm.maxDistanceAtZoom = maxDistanceAtZoom;
    }
}
