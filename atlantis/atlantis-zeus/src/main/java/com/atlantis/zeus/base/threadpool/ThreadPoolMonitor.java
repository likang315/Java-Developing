package com.atlantis.zeus.base.threadpool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池监控
 *
 * @author likang02@corp.netease.com
 * @date 2021-08-22 16:52
 */
@Slf4j(topic = "threadPool")
@Component
public class ThreadPoolMonitor {
    /**
     * 业务线程池
     *
     */
    public static final ConcurrentHashMap<String, ExecutorService> BUSINESS_THREAD_POOL = new ConcurrentHashMap<>();

    /**
     * 注册线程池到队列中
     *
     * @param name
     * @param threadPoolExecutor
     */
    static void registerThreadPool(String name, ExecutorService threadPoolExecutor) {
        BUSINESS_THREAD_POOL.put(name, threadPoolExecutor);
    }

    /**
     * 业务线程池监控
     */
    private void threadPoolMonitor() {
        for (Map.Entry<String, ExecutorService> entry : BUSINESS_THREAD_POOL.entrySet()) {
            ThreadPoolExecutor pool = (ThreadPoolExecutor)entry.getValue();
            // 线程池线程数量
            int poolSize = pool.getPoolSize();
            // 正在执行任务的线程数量
            int activeCount = pool.getActiveCount();
            // 核心线程数
            int corePoolSize = pool.getCorePoolSize();
            // 排队任务数
            int queueSize = pool.getQueue().size();
            // 最大线程数
            int maximumPoolSize = pool.getMaximumPoolSize();
            // 已经执行的和未执行的任务总数
            long taskCount = pool.getTaskCount();
            // 已经执行的任务总数
            long completedTaskCount = pool.getCompletedTaskCount();
            log.info("ThreadPoolMonitor-" + entry.getKey() + ":  poolSize: {}, activeCount:{}, corePoolSize: {}," +
                            " queueSize:{}, maximumPoolSize:{}, taskCount:{}, completedTaskCount: {}", poolSize, activeCount, corePoolSize,
                    queueSize, maximumPoolSize, taskCount, completedTaskCount);
        }
    }

    /**
     * 跟贴相关定时删除
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void commentClearDataTask() {
        threadPoolMonitor();
    }

}
