package com.atlantis.zeus.task.index;

import com.atlantis.zeus.index.pojo.Score;
import com.atlantis.zeus.index.service.IndexStudentInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 首页定时任务
 *
 * @author likang02@corp.netease.com
 * @date 2021-08-22 16:11
 */
@Slf4j
@Component
public class IndexScheduled {

    @Resource
    private IndexStudentInfo indexStudentInfo;

    /**
     * 异步每分钟执行一次
     */
    @Async("scheduledThreadPool")
    @Scheduled(cron = "0 * * * * ?")
    public void execute() {
        try {
            Score score = indexStudentInfo.queryStuScoreById("beijing_001");
            log.info("IndexSchedule_execute success: score: {}", score);
        } catch (Exception e) {
            log.error("IndexSchedule_execute fail: exp: ", e);
        }
    }


}
