package com.atlantis.zeus.index.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author kangkang.li@qunar.com
 * @date 2020-11-29 16:12
 */
@ToString
@Data
public class Teacher {

    private List<String> name;
    private List<String> id;
}
