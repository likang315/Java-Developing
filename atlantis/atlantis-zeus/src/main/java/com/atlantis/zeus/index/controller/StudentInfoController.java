package com.atlantis.zeus.index.controller;

import com.atlantis.zeus.base.annotation.ElapsedTime;
import com.atlantis.zeus.base.utils.ApiResult;
import com.atlantis.zeus.index.pojo.entity.StudentInfoDO;
import com.atlantis.zeus.index.service.impl.IndexStudentInfoImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 学生信息Controller层
 *
 * @author kangkang.li@qunar.com
 * @date 2020-10-11 19:15
 */
@RestController
@RequestMapping("/index")
public class StudentInfoController {

    @Resource
    private IndexStudentInfoImpl indexStudentInfo;

    @ElapsedTime
    @RequestMapping("/querySingle")
    public ApiResult querySingle(Integer value) {
        return ApiResult.success(indexStudentInfo.getStudentInfo(value));
    }

    @RequestMapping("/queryBatch")
    public ApiResult queryBatch(@RequestBody List<String> globalKey) {
        return ApiResult.success(indexStudentInfo.queryStudentInfoById(globalKey));
    }

    @RequestMapping("/queryScore")
    public ApiResult queryScoreByGlobalKey(@RequestParam String globalKey) {
        Assert.isTrue(!StringUtils.isBlank(globalKey), "req param exp!!!");
        return ApiResult.success(indexStudentInfo.queryStuScoreById(globalKey));

    }

    @RequestMapping("/insert")
    public ApiResult insert(@Valid @RequestBody StudentInfoDO info) {
        return ApiResult.success(indexStudentInfo.insertOneStudentInfo(info));
    }

}
