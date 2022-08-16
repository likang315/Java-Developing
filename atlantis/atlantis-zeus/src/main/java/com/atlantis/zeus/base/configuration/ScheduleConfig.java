package com.atlantis.zeus.base.configuration;

import com.atlantis.zeus.base.utils.NetworkUtil;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 定时任务配置类
 * 启用线程池异步执行，防止多个定时任务同时执行时阻塞
 *
 * @author likang02@corp.netease.com
 * @date 2021-08-22 16:33
 */
@EnableScheduling
@Configuration
public class ScheduleConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }

    /**
     * 使用JUC 提供的定时任务线程池，bean 销毁时自动关闭线程池
     *
     * @return
     */
    @Bean(destroyMethod = "shutdown")
    public ScheduledExecutorService taskExecutor() {
        String ip = NetworkUtil.queryIpAddress();
        return new ScheduledThreadPoolExecutor(5, new BasicThreadFactory.Builder().daemon(true)
                .namingPattern(ip + "--" + "scheduled" + "-%d").build());
    }

}