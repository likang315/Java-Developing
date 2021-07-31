package com.atlantis.zeus.index.dao.readonly;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 通用只读服务
 *
 * @author likang02@corp.netease.com
 * @date 2021-07-31 14:38
 */
public interface CommonReadMapper {

    /**
     * 统计表中数据量
     *
     * @param tableName
     * @param where
     * @return
     */
    int queryCount(@Param("tableName") String tableName, @Param("where") String where);


    /**
     * 查询表中的数据，分页查
     *
     * @param tableName
     * @param fields
     * @param where
     * @param pageSize
     * @param offSet
     * @return
     */
    @MapKey("id")
    Map<String, Map<String, String>> query(@Param("tableName") String tableName,
                                           @Param("fields") List<String> fields,
                                           @Param("where") String where,
                                           @Param("pageSize") int pageSize,
                                           @Param("offset") int offSet);
}
