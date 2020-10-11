package com.atlantis.zeus.index.service;

import com.atlantis.zeus.index.dto.StudentInfo;

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
    StudentInfo getStudentInfo(int id);

    /**
     * 插入一个学生信息
     *
     * @return
     */
    boolean insertOneStudentInfo();
}
