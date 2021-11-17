package com.atlantis.zeus.base.threadpool;

import com.atlantis.zeus.base.constant.NumberConstants;
import com.atlantis.zeus.base.utils.NetworkUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
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

    /**
     * 初始化钩子，优雅的退出线程池
     */
    @PostConstruct
    private void addHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::closeRegisterThreadPool));

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
                new BasicThreadFactory.Builder().namingPattern(ip + "-" + threadName + "-%d").build(),
                rejectedExecutionHandler);

        log.info("CustomThreadPool-buildThreadPoolExecutor: threadPool prefix: {} launch successful", threadName);
        ThreadPoolMonitor.registerThreadPool(threadName, threadPool);
        return threadPool;
    }

    private void closeRegisterThreadPool() {
        ThreadPoolMonitor.BUSINESS_THREAD_POOL.forEachEntry(1,
                entry -> close(entry.getKey(), entry.getValue()));
    }


    /**
     * 优雅的关闭线程池
     * 注意：关闭线程过程中，该线程可能被中断
     *
     * @param threadName
     * @param pool
     */
    private void close(String threadName, ExecutorService pool) {
        log.info("CustomThreadPool_close: start to shutdown the theadPool: {}", threadName);
        pool.shutdown();
        try {
            if (!pool.awaitTermination(NumberConstants.ONE, TimeUnit.SECONDS)) {
                log.warn("CustomThreadPool_close: interrupt the worker for {}, which may cause some task inconsistent!", threadName);
                pool.shutdownNow();

                if (!pool.awaitTermination(NumberConstants.FIFTY, TimeUnit.SECONDS)) {
                    log.error("CustomThreadPool_close: {} pool can't be shutdown even with interrupting worker threads," +
                            " which may cause some task inconsistent", threadName);
                }
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            log.error("CustomThreadPool_close: the current server thread is interrupted" +
                    " when it is trying to stop the worker threads of {}", pool);
            // 保留中断位
            Thread.currentThread().interrupt();
        }

        log.info("CustomThreadPool_close: the threadPool of {} is successful closed!!!", threadName);
    }



    @Bean("scheduledThreadPool")
    public ExecutorService commentThreadPool() {
        log.info("CustomThreadPool-scheduledThreadPool start !!!");
        return buildThreadPoolExecutor(24, 128, 1000 * 60L,
                new LinkedBlockingDeque<>(),
                "scheduled",
                new ThreadPoolExecutor.AbortPolicy());
    }
}
