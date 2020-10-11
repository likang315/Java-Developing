package com.atlantis.zeus.index.controller;

import com.atlantis.zeus.index.dto.StudentInfo;
import com.atlantis.zeus.index.service.impl.IndexStudentInfoImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

    @RequestMapping("/studentById")
    public StudentInfo init() {
        return indexStudentInfo.getStudentInfo(1);
    }

    @RequestMapping("/insert")
    public boolean insert() {
        return indexStudentInfo.insertOneStudentInfo();
    }
}
