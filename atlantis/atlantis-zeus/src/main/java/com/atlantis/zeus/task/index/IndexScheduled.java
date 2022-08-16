package com.atlantis.zeus.task.index;

import com.atlantis.zeus.base.utils.NetworkUtil;
import com.atlantis.zeus.base.utils.RedissonUtil;
import com.atlantis.zeus.index.pojo.Score;
import com.atlantis.zeus.index.service.IndexStudentInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

/**
 * 首页定时任务
 *
 * @author likang02@corp.netease.com
 * @date 2021-08-22 16:11
 */
@Slf4j
@Component
public class IndexScheduled {

    private final String LOCK = "index:task:lock";

    @Resource
    private IndexStudentInfo indexStudentInfo;

    @Resource
    private RedissonUtil redissonUtil;

    /**
     * 异步每分钟执行一次
     * 注意：同一个任务依次执行
     */
    @Scheduled(cron = "0 * * * * ?")
    public void execute() {
        // 分布式锁(可重入锁)
        if (!redissonUtil.tryLock(LOCK, 0,  60 * 10)) {
            return;
        }
        log.info("IndexScheduled_execute start, ip: {}", NetworkUtil.queryIpAddress());
        try {
            Score score = indexStudentInfo.queryStuScoreById("beijing_001");
            CompletableFuture.runAsync(() -> {
                System.out.println(redissonUtil.tryLock(LOCK, 0, 60 * 10));
            });
            log.info("IndexSchedule_execute success: score: {}", score);
        } catch (Exception e) {
            log.error("IndexSchedule_execute fail: exp: ", e);
        }

        log.info("IndexScheduled_execute end!!!");
    }

    /**
     * 使用异步线程池，多个任务并发执行
     * 注意：同一个任务也是并发执行
     */
    @Async("scheduledThreadPool")
    @Scheduled(cron = "0 * * * * ?")
    public void scheduleAsync() {
        log.info("IndexScheduled_scheduleAsync: use scheduledThreadPool execute");
    }
}
