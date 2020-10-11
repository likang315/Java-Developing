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
    /** pxc core pool size */
    private int corePoolSize;
    /** pxc max pool size */
    private int maxPoolSize;
}
