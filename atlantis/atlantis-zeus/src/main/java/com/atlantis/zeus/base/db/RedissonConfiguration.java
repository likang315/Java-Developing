package com.atlantis.zeus.base.db;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 初始化 Redission 配置
 *
 * @author likang02@corp.netease.com
 * @date 7/7/22 8:21 PM
 */
@Configuration
public class RedissonConfiguration {

    @Data
    public class RedissonConfig {
        private String address;
        private String password;
        private int database;
        private int timeout;
        private int maxIdle;
        private int minIdle;
        private int maxTotal;
        private int maxWaitMillis;
    }

    /**
     * 直接@Value(${}) 注入失败，只能通过前缀匹配类
     *
     * @return
     */
    @Bean(name = "redissonConfig")
    @ConfigurationProperties(prefix = "zeus.datasource.redisson")
    public RedissonConfig pxcDataSourceConfig() {
        return new RedissonConfig();
    }


    /**
     * 初始化 redissonClient
     *
     * @param redissonConfig
     * @return
     */
    @Bean(name = "redissonClient", destroyMethod = "shutdown")
    RedissonClient initRedissonClient(@Qualifier("redissonConfig") RedissonConfig redissonConfig) {
        Config config = new Config();
        config.setCodec(StringCodec.INSTANCE)
                // 单点模式
                .useSingleServer()
                .setAddress(redissonConfig.getAddress())
                .setPassword(redissonConfig.password)
                .setTimeout(redissonConfig.getTimeout())
                .setDatabase(redissonConfig.getDatabase())
                .setConnectionPoolSize(redissonConfig.getMaxIdle())
                .setConnectionMinimumIdleSize(redissonConfig.getMinIdle());

        return Redisson.create(config);
    }
}
