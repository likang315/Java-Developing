package com.atlantis.zeus.index.dao.rw;

import com.atlantis.zeus.index.pojo.StudentInfoDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 学生信息表 只写dao
 *
 * @author kangkang.li@qunar.com
 * @date 2020-10-11 21:37
 */
public interface StudentInfoMapper {

    /**
     * 插入一个学生信息
     *
     * @param studentInfo
     * @return
     */
    int insertStudentInfo(@Param("student") StudentInfoDO studentInfo);

    /**
     * 批量学生插入
     *
     * @param studentInfoList
     * @return
     */
    int insertBatchStudent(@Param("list") List<StudentInfoDO> studentInfoList);

    /**
     * 更新
     *
     * @param studentInfo
     * @return
     */
    int update(StudentInfoDO studentInfo);

    /**
     * 批量更新
     *
     * @param studentInfoList
     * @return
     */
    int updateBatch(@Param("list") List<StudentInfoDO> studentInfoList);

    /**
     * 根据 ID 删除
     *
     * @param id
     * @return
     */
    int delete(long id);


}
