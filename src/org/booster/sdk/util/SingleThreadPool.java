package org.booster.sdk.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: 单线程线程池,确保线程执行的顺序,复用线程,不必再创建新的线程
 * @author Merry.Zhao
 * @date 2014-3-13 下午6:33:16
 */
public class SingleThreadPool {
    private static ExecutorService threadPool;

    /**
     * 添加一个任务到处理线程池中
     * @param task
     */
    public static void addTask(Runnable task) {
        if (threadPool == null) {
            synchronized (SingleThreadPool.class) {
                if (threadPool == null) {
                    threadPool = Executors.newSingleThreadExecutor();
                    threadPool.execute(task);
                }
            }
        } else {
            threadPool.execute(task);
        }
    }

    /**
     * 停止线程池,并销毁
     */
    public static void shutdown() {
        if (threadPool != null && !threadPool.isShutdown()) {
            threadPool.shutdown();
            threadPool = null;
        }
    }
}
