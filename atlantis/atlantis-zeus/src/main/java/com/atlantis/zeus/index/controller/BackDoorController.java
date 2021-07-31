package com.atlantis.zeus.index.controller;

import com.atlantis.zeus.base.utils.ApiResult;
import com.atlantis.zeus.index.pojo.dto.ExcelDownloadDTO;
import com.atlantis.zeus.index.pojo.vo.DownloadVO;
import com.atlantis.zeus.index.service.DownloadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author likang02@corp.netease.com
 * @date 2021-07-31 14:41
 */
@Slf4j
@RequestMapping("/backdoor")
@RestController
public class BackDoorController {

    @Resource
    private DownloadService downloadService;

    @RequestMapping("/download/table")
    public ApiResult download(@RequestBody ExcelDownloadDTO req) {
        DownloadVO vo = null;
        try {
            String tableName = req.getTableName();
            String where = req.getWhere();
            List<String> fields =  req.getFields();
            Assert.isTrue(StringUtils.isNoneBlank(tableName)
                    && StringUtils.isNotBlank(where)
                    && !CollectionUtils.isEmpty(fields), "req param is illegal!!!");
            vo = downloadService.downloadExcel(tableName, fields, where);
            return ApiResult.success(vo);
        } catch (Exception e) {
            log.info("BackDoorController_downloadData: exp: ", e);
        }

        return ApiResult.error("", vo);
    }
}
