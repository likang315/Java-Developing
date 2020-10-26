package com.atlantis.zeus.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义耗时注解
 *
 * @author kangkang.li@qunar.com
 * @date 2020-10-26 10:12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface ElapsedTime {
    String value() default "";
}
