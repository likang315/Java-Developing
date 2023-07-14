package com.atlantis.zeus.base.db;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库实体类
 *
 * @author kangkang.li@qunar.com
 * @date 2020-10-11 19:32
 */
@Data
@NoArgsConstructor
public class PxcDataSourceConfig {
    /** pxc namespace */
    private String namespace;
    /** pxc username */
    private String username;
    /** pxc password */
    private String password;
    /** pxc db name */
    private String dbName;
    /**
     * min idle pool size
     * 最小空闲连接
     */
    private int minIdleSize;
    /**
     * max pool size
     */
    private int maxPoolSize;

    /**
     * 连接池中的连接最小空闲时间，单位为毫秒
     */
    private int minEvictableIdleTimeMillis;

    /**
     * 两次连接池的空闲连接检测之间的时间间隔，单位为毫秒
     * destroy 线程定时检测时间间隔
     */
    private int timeBetweenEvictionRunsMillis;

}
