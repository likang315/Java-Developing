package com.atlantis.zeus.index.service.impl;

import com.atlantis.zeus.index.dao.readonly.StudentInfoReadMapper;
import com.atlantis.zeus.index.dao.rw.StudentInfoMapper;
import com.atlantis.zeus.index.pojo.StudentInfoDO;
import com.atlantis.zeus.index.service.IndexStudentInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author kangkang.li@qunar.com
 * @date 2020-10-11 21:00
 */
@Slf4j
@Service
public class IndexStudentInfoImpl implements IndexStudentInfo {

    @Resource
    private StudentInfoReadMapper studentInfoReadMapper;

    @Resource
    private StudentInfoMapper studentInfoMapper;

    @Override
    public StudentInfoDO getStudentInfo(int id) {
        return studentInfoReadMapper.queryById(id);
    }

    @Override
    public boolean insertOneStudentInfo() {
        StudentInfoDO studentInfo = new StudentInfoDO().setStuGlobalKey("beijing_003").setName("lisi").setAge(0).setSex(1);
        // 调试数据库也会执行
        int count = studentInfoMapper.insertStudentInfo(studentInfo);
        return count > 0;
    }

    @Override
    public Map<String, String> queryStudentInfoById(List<String> ids) {
        Map<String, String> map =  studentInfoReadMapper.queryStudentInfoById(ids);
        log.info(map.toString());
        return map;
    }


}
