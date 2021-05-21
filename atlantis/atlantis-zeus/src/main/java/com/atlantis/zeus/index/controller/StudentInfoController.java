package com.atlantis.zeus.index.controller;

import com.atlantis.zeus.base.annotation.ElapsedTime;
import com.atlantis.zeus.base.utils.ApiResult;
import com.atlantis.zeus.index.service.impl.IndexStudentInfoImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    public ApiResult querySingle() {
        return ApiResult.success(indexStudentInfo.getStudentInfo(1));
    }

    @RequestMapping("/queryBatch")
    public ApiResult queryBatch() {
        List<String> list = new ArrayList<>();
        return ApiResult.success(indexStudentInfo.queryStudentInfoById(list));
    }

    @RequestMapping("/queryScore")
    public ApiResult queryScoreByGlobalKey(@RequestParam String globalKey) {
        Assert.isTrue(!StringUtils.isBlank(globalKey), "req param exp!!!");
        return ApiResult.success(indexStudentInfo.queryStuScoreById(globalKey));

    }

    @RequestMapping("/insert")
    public ApiResult insert() {
        return ApiResult.success(indexStudentInfo.insertOneStudentInfo());
    }



}
