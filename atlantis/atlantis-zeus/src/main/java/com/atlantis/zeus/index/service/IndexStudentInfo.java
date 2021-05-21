package com.atlantis.zeus.index.service;

import com.atlantis.zeus.index.pojo.Score;
import com.atlantis.zeus.index.pojo.StudentInfoDO;

import java.util.List;
import java.util.Map;

/**
 * 学生信息服务
 *
 * @author kangkang.li@qunar.com
 * @date 2020-10-11 21:56
 */
public interface IndexStudentInfo {
    /**
     * 获取学生信息
     *
     * @param id
     * @return
     */
    StudentInfoDO getStudentInfo(int id);

    /**
     * 批量查询学生信息
     *
     * @param ids
     * @return
     */
    Map<String, String> queryStudentInfoById(List<String> ids);

    /**
     * 插入一个学生信息
     *
     * @return
     */
    boolean insertOneStudentInfo();

    /**
     * 通过学生ID 查询学生对应成绩
     *
     * @return
     */
    Score queryStuScoreById(String globalKey);
}
