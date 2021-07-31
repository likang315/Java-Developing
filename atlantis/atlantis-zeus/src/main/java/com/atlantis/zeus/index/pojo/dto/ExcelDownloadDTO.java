package com.atlantis.zeus.index.pojo.dto;

import lombok.Data;

import java.util.List;

/**
 * @author likang02@corp.netease.com
 * @date 2021-07-31 15:24
 */
@Data
public class ExcelDownloadDTO {
    /**
     * 表名
     */
    private String tableName;

    /**
     * 字段列表
     */
    private List<String> fields;

    /**
     * 限定条件
     */
    private String where;
}
