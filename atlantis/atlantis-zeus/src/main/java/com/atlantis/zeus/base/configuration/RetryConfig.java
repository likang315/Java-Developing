package com.atlantis.zeus.base.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

/**
 * 启动spring 重试组件
 *
 * @author likang02@corp.netease.com
 * @date 2021-08-23 20:18
 */
@EnableRetry
@Configuration
public class RetryConfig {

}
