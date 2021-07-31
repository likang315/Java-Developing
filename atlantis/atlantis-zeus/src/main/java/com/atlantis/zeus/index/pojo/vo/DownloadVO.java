package com.atlantis.zeus.index.pojo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 下载VO
 *
 * @author likang02@corp.netease.com
 * @date 2021-07-31 14:50
 */
@Data
@Accessors(chain = true)
public class DownloadVO {
    /**
     * 下载的IP地址
     */
    private String ip;

    /**
     * 下载的数量
     *
     */
    private Integer count;

    /**
     * nos URL
     */
    private String nosUrl;
}