package com.atlantis.zeus.index.dao.readonly;

import com.atlantis.zeus.index.pojo.StudentInfoDO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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
    StudentInfoDO queryById(@Param("id") int id);

    /**
     * 通过GlobalKey获取学生信息
     * 若是Key有重复的会被替换掉
     *
     * @param ids
     * @return
     */
    @MapKey("stuGlobalKey")
    Map<String, String>  queryStudentInfoById(@Param("ids") List<String> ids);
}
