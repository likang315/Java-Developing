package com.atlantis.zeus.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作、统计日志记录
 *
 * @author likang02@corp.netease.com
 * @date 2021-09-23 17:38
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface LogRecord {

    /**
     * 操作人
     *
     * @return
     */
    String operatePerson() default "";

    /**
     * 操作时间
     *
     * @return
     */
    String operateTime() default "";

    /**
     * 是否成功
     *
     * @return
     */
    boolean isSuccess() default true;

    /**
     * 业务参数 Json 参数
     *
     * @return
     */
    String biz() default "";

    /**
     * 拓展字段
     *
     * @return
     */
    String ext() default "";

}
