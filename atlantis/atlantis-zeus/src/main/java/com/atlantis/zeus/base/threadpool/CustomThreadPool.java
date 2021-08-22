package com.atlantis.zeus.base.threadpool;

import com.atlantis.zeus.base.utils.NetworkUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程池
 *
 * @author likang02@corp.netease.com
 * @date 2021-08-22 16:51
 */
@Slf4j(topic = "threadPool")
@Configuration
public class CustomThreadPool {

    @Bean("scheduledThreadPool")
    public ExecutorService commentThreadPool() {
        log.info("CustomThreadPool-scheduledThreadPool start !!!");
        return buildThreadPoolExecutor(24, 128, 1000 * 60L,
                new LinkedBlockingDeque<>(),
                "scheduled",
                new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 构键线程池
     *
     * @param corePoolSize
     * @param maxPoolSize
     * @param keepAliveTime
     * @param workQueue
     * @param threadName
     * @param rejectedExecutionHandler
     * @return
     */
    private ExecutorService buildThreadPoolExecutor(int corePoolSize,
                                                    int maxPoolSize,
                                                    long keepAliveTime,
                                                    BlockingQueue<Runnable> workQueue,
                                                    String threadName,
                                                    RejectedExecutionHandler rejectedExecutionHandler) {
        String ip = NetworkUtil.queryIpAddress();
        ExecutorService threadPool = new ThreadPoolExecutor(corePoolSize,
                maxPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                workQueue,
                new ThreadFactoryBuilder().setNameFormat(ip + "-" + threadName + "-%d").build(),
                rejectedExecutionHandler);

        log.info("CustomThreadPool-buildThreadPoolExecutor: threadPool prefix: {} launch successful", threadName);
        ThreadPoolMonitor.registerThreadPool(threadName, threadPool);
        return threadPool;
    }
}
