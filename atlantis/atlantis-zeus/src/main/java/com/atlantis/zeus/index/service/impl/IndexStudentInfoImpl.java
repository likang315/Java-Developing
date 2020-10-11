package com.atlantis.zeus.index.service.impl;

import com.atlantis.zeus.index.dao.readonly.StudentInfoReadMapper;
import com.atlantis.zeus.index.dao.rw.StudentInfoMapper;
import com.atlantis.zeus.index.dto.StudentInfo;
import com.atlantis.zeus.index.service.IndexStudentInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author kangkang.li@qunar.com
 * @date 2020-10-11 21:00
 */
@Service
public class IndexStudentInfoImpl implements IndexStudentInfo {

    @Resource
    private StudentInfoReadMapper studentInfoReadMapper;

    @Resource
    private StudentInfoMapper studentInfoMapper;

    @Override
    public StudentInfo getStudentInfo(int id) {
        return studentInfoReadMapper.queryById(id);
    }

    @Override
    public boolean insertOneStudentInfo() {
        StudentInfo studentInfo = new StudentInfo().setStuGlobalKey("beijing_003").setName("lisi").setAge(0).setSex(1);
        // 调试数据库也会执行
        int count = studentInfoMapper.insertStudentInfo(studentInfo);
        return count > 0;
    }


}
