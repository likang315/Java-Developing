package com.atlantis.zeus.index.pojo.entity;

import lombok.Data;

/**
 * 日志记录DO
 *
 * @author likang02@corp.netease.com
 * @date 2021-09-23 18:10
 */
@Data
public class LogRecordDO {

    /**
     * 操作人
     *
     * @return
     */
    private String operatePerson;

    /**
     * 操作时间
     *
     * @return
     */
    private String operateTime;

    /**
     * 是否成功
     *
     * @return
     */
    private Boolean isSuccess;

    /**
     * 业务参数 Json 参数
     *
     * @return
     */
    private String biz;

    /**
     * 拓展字段
     *
     * @return
     */
    private String ext;
}
