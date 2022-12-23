package com.atlantis.zeus.base.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 启用spring cache, 声明缓存管理器
 *
 * @author likang02@corp.netease.com
 * @date 2022/12/23 15:36
 */
@EnableCaching
@Configuration
public class CacheConfig {

    /**
     * 声明缓存管理器
     *
     * @return
     */
    @Bean
    public CacheManager cacheManager() {
        // spring 内置的缓存管理器
        return new ConcurrentMapCacheManager();
    }

}
