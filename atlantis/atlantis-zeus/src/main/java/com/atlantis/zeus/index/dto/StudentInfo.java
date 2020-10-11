package com.atlantis.zeus.index.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author kangkang.li@qunar.com
 * @date 2020-10-11 20:55
 */
@Data
@Accessors(chain = true)
public class StudentInfo {
    /**
     * 学生唯一标识
     */
    private String stuGlobalKey;

    /**
     * name
     */
    private String name;

    /**
     * sex
     */
    private Integer sex;

    /**
     * age
     */
    private Integer age;

    /**
     * 创建时间
     */
    private LocalDateTime create_time;

    /**
     * 更新时间
     */
    private LocalDateTime update_time;
}
