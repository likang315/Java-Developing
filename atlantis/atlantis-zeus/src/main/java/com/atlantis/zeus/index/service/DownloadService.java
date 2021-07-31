package com.atlantis.zeus.index.service;

import com.atlantis.zeus.index.pojo.vo.DownloadVO;

import java.util.List;

/**
 * @author likang02@corp.netease.com
 * @date 2021-07-31 14:44
 */
public interface DownloadService {

    /**
     * Excel 下载
     *
     * @param tableName
     * @param fields
     * @param where
     * @return
     */
    DownloadVO downloadExcel(String tableName, List<String> fields, String where);
}
