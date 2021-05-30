package com.atlantis.zeus.index.service.impl;

import com.atlantis.zeus.base.exception.RespException;
import com.atlantis.zeus.index.dao.readonly.StudentInfoReadMapper;
import com.atlantis.zeus.index.dao.rw.StudentInfoMapper;
import com.atlantis.zeus.index.pojo.Score;
import com.atlantis.zeus.index.pojo.StudentInfoDO;
import com.atlantis.zeus.index.service.IndexStudentInfo;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    public boolean insertOneStudentInfo(StudentInfoDO info) {
        // 调试数据库也会执行
        int count = studentInfoMapper.insertStudentInfo(info);
        return count > 0;
    }

    @Override
    public Map<String, String> queryStudentInfoById(List<String> ids) {
        Map<String, String> map =  studentInfoReadMapper.queryStudentInfoById(ids);
        if (Objects.isNull(map) || map.isEmpty()) {
            throw new RespException("IndexStudentInfoImpl_queryStudentInfoById", ids.toString());
        }

        log.info(map.toString());
        return map;
    }

    @Override
    public Score queryStuScoreById(String globalKey) {
        Score result = null;
        try {
            result = studentInfoReadMapper.queryStuScoreById(globalKey);
        } catch (Exception e) {
            log.error("IndexStudentInfoImpl_queryStuScoreById: ", e);
        }

        return result;
    }


}
