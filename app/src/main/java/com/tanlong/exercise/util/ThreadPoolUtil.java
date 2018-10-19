package com.tanlong.exercise.util;

import android.support.annotation.NonNull;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtil {

    /**
     * 可用CPU数量
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    /**
     * 核心线程数量
     */
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    /**
     * 最大线程数量
     */
    private static final int MAX_POOL_SIZE = 2 * CPU_COUNT + 1;
    /**
     * 非核心线程空闲存活时间
     */
    private static final int KEEP_ALIVE_TIME = 10;

    private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {
        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r);
        }
    };

    public static ThreadPoolExecutor getNormalThreadPool() {
        return new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), THREAD_FACTORY);
    }
}
