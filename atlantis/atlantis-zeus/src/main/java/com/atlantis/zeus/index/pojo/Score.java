package com.atlantis.zeus.index.pojo;

import lombok.Data;

/**
 * 学生成绩
 *
 * @author kangkang.li@qunar.com
 * @date 2021-05-21 09:57
 */
@Data
public class Score {
    /**
     * 学生ID
     */
    private String stuGlobalKey;

    /**
     * 姓名
     */
    private String name;

    /**
     * 数学成绩
     */
    private Double math;

    /**
     * 英语成绩
     */
    private Double english;

    /**
     * 语文成绩
     */
    private Double chinese;
}