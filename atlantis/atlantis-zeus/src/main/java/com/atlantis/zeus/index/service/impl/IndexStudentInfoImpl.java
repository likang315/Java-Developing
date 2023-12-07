package com.atlantis.zeus.index.service.impl;

import com.atlantis.zeus.base.annotation.LogRecord;
import com.atlantis.zeus.base.exception.BizException;
import com.atlantis.zeus.base.exception.RespException;
import com.atlantis.zeus.index.dao.readonly.StudentInfoReadMapper;
import com.atlantis.zeus.index.dao.rw.StudentInfoMapper;
import com.atlantis.zeus.index.pojo.Score;
import com.atlantis.zeus.index.pojo.entity.StudentInfoDO;
import com.atlantis.zeus.index.service.IndexStudentInfo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
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

    @Resource(name = "redissonClient")
    private RedissonClient redissonClient;

    @Cacheable(value = "studentInfoDO")
    @Override
    public StudentInfoDO getStudentInfo(int id) {
        StudentInfoDO infoDO = studentInfoReadMapper.queryById(id);
        RSet<StudentInfoDO> rSet = redissonClient.getSet("INDEX:STUDENT:LIST");
        rSet.add(infoDO);
        rSet.expire(Duration.ofDays(3));
        return infoDO;
    }

    @LogRecord(operateTime = "new java.util.Date()", biz = "#info")
    @Override
    public boolean insertOneStudentInfo(StudentInfoDO info) {
        // 调试数据库也会执行
        int count = 0;
        try {
            count = studentInfoMapper.insertStudentInfo(info);
        } catch (Exception e) {
            throw new BizException("student insert error", info);
        }
        return count > 0;
    }

    /**
     * 使用重试注解
     * 指定异常重试，默认最大重试3次，@backoff 指定每次重试间隔3ms，每次增加2倍
     *
     * @param ids
     * @return
     */
    @Retryable(value = {RespException.class}, backoff = @Backoff(delay = 3, multiplier = 2))
    @Override
    public Map<String, Map<String, Object>> queryStudentInfoById(List<String> ids) {
        Map<String, Map<String, Object>> map =  studentInfoReadMapper.queryStudentInfoById(ids);
        if (Objects.isNull(map) || map.isEmpty()) {
            throw new RespException("IndexStudentInfoImpl_queryStudentInfoById", ids.toString());
        }

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
