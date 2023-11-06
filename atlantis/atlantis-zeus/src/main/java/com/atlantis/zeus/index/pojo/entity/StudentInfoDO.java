package com.atlantis.zeus.index.pojo.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author kangkang.li@qunar.com
 * @date 2020-10-11 20:55
 */
@Data
@Accessors(chain = true)
@ToString
public class StudentInfoDO {
    /**
     * 学生唯一标识
     */
    @NotNull(message = "#{stuGlobalKey property is null !!!}")
    public String stuGlobalKey;

    /**
     * name
     */
    @NotBlank(message = "名字异常")
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
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
