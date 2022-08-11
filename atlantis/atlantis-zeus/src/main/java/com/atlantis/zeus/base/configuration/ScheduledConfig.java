package com.atlantis.zeus.base.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 定时任务配置类
 * 启用线程池异步执行，防止多个定时任务同时执行时阻塞
 *
 * @author likang02@corp.netease.com
 * @date 2021-08-22 16:33
 */
@EnableScheduling
@Configuration
public class ScheduledConfig {

}
