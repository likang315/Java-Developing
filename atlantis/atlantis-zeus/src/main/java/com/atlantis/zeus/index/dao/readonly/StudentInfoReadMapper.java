package com.atlantis.zeus.index.dao.readonly;

import com.atlantis.zeus.index.dto.StudentInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 学生信息表 只读 dao
 *
 * @author kangkang.li@qunar.com
 * @date 2020-10-11 20:51
 */
@Repository
public interface StudentInfoReadMapper {

    /**
     * 通过ID查询学生信息
     *
     * @param id
     * @return
     */
    StudentInfo queryById(@Param("id") int id);
}
