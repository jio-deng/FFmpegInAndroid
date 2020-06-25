package com.dzm.ffmpeg.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程管理
 * Created by gyl on 2015/11/30.
 */
public class ThreadUtils {

    private static ExecutorService mSingleThreadPoolExecutor;
    private static ExecutorService mCachedThreadPool;
    private static RenameThreadFactory mRenameThreadFactory;

    static {
        mRenameThreadFactory = new RenameThreadFactory();
    }

    /**
     * 获取单线程的线程池
     *
     * @return
     */
    public static ExecutorService getSingleThreadPool() {
        if (null == mSingleThreadPoolExecutor) {
            if (null == mRenameThreadFactory) {
                mRenameThreadFactory = new RenameThreadFactory();
            }
            mSingleThreadPoolExecutor = Executors.newSingleThreadExecutor(mRenameThreadFactory);
        }
        return mSingleThreadPoolExecutor;
    }

    /**
     * 获取缓存的线程池
     *
     * @return
     */
    public static ExecutorService getCachedThreadPool() {
        if (null == mCachedThreadPool) {
            if (null == mRenameThreadFactory) {
                mRenameThreadFactory = new RenameThreadFactory();
            }
            mCachedThreadPool = Executors.newCachedThreadPool(mRenameThreadFactory);
        }
        return mCachedThreadPool;
    }

    private static class RenameThreadFactory implements ThreadFactory {
        private final ThreadGroup group;
        private final AtomicInteger poolNumber;
        private final AtomicInteger threadNumber;

        public RenameThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            poolNumber = new AtomicInteger(1);
            threadNumber = new AtomicInteger(1);
        }

        @Override
        public Thread newThread(Runnable r) {
            String s = "ThreadUtils-" +
                    String.valueOf(poolNumber.getAndIncrement()) +
                    "-thread-" +
                    String.valueOf(threadNumber.getAndIncrement());
            Thread t = new Thread(group, r, s, 0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
}