package com.atlantis.zeus.index.dao.readonly;

import com.atlantis.zeus.index.pojo.Score;
import com.atlantis.zeus.index.pojo.entity.StudentInfoDO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 学生信息表 只读 dao
 *
 * @author kangkang.li@qunar.com
 * @date 2020-10-11 20:51
 */
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
    Map<String, Map<String, String>>  queryStudentInfoById(@Param("ids") List<String> ids);

    /**
     * 通过学生ID查询学生对应成绩
     *
     * @param globalKey
     * @return
     */
    Score queryStuScoreById(@Param("globalKey") String globalKey);

    /**
     * 统计学生数量
     *
     * @return
     */
    Long studentCount();
}
