package com.hoho.android.usbserial.util;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author DELL
 */
public class ThreadPoolExecutorUtils {
    private static ThreadPoolExecutor threadPoolExecutor = null;

    public static ThreadPoolExecutor getInstance() {
        if (null == threadPoolExecutor) {
            threadPoolExecutor = new ThreadPoolExecutor(6, 10, 0, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(10000), new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("my-thread-" + thread.getId());
                    return thread;
                }
            });
        }
        return threadPoolExecutor;
    }

    public static void onShutDownExecutor() {
        if (null != threadPoolExecutor) {
            threadPoolExecutor.shutdown();
            threadPoolExecutor = null;
        }
    }

}
