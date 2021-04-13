package com.atlantis.zeus.base.constant.enums;

import java.util.Objects;

/**
 * 性别枚举
 *
 * @author kangkang.li@qunar.com
 * @date 2021-04-11 17:30
 */
public enum SexEnum {
    /**
     * 男孩
     */
    SEX_BOY((byte)1, "男"),

    /**
     * 女孩
     */
    SEX_GIRL((byte)2, "女");

    private byte code;

    private String sex;

    SexEnum(byte code, String sex) {
        this.code = code;
        this.sex = sex;
    }

    public byte getCode() {
        return code;
    }

    /**
     * 通过 code 获取对应的性别枚举
     *
     * @param code
     * @return
     */
    public SexEnum getSexEnumByCode(byte code) {
        for ( SexEnum sex : SexEnum.values()) {
            if (Objects.equals(code, sex.getCode())) {
                return sex;
            }
        }
        return SexEnum.SEX_BOY;
    }
}
