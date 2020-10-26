package com.atlantis.zeus.base.aop;


import com.atlantis.zeus.base.annotation.ElapsedTime;
import com.atlantis.zeus.base.constant.AopOrderedConstant;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 自定义耗时注解AOP实现
 *
 * @author kangkang.li@qunar.com
 * @date 2020-10-26 10:13
 */
@Component
@Aspect
@Slf4j
public class ElapsedTimeInterceptor implements Ordered {

    /**
     * 自定义注解功能
     *
     * @param jp
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.atlantis.zeus.base.annotation.ElapsedTime)")
    public Object elapsedTime(ProceedingJoinPoint jp) throws Throwable {
        Signature signature = jp.getSignature();
        if (signature instanceof MethodSignature) {
            Method method = ((MethodSignature) signature).getMethod();
            ElapsedTime annotation = method.getAnnotation(ElapsedTime.class);
            String monitorName = annotation != null && StringUtils.isNotEmpty(annotation.value()) ? annotation.value()
                    : String.format("%s.%s", method.getDeclaringClass().getSimpleName(), method.getName());

            StopWatch stopWatch = StopWatch.createStarted();
            Object result = jp.proceed();

            log.info(monitorName + "."  + stopWatch.getTime());
            return result;
        } else {
            return jp.proceed();
        }
    }

    @Override
    public int getOrder() {
        return AopOrderedConstant.ORDERED_RECORD_TIME;
    }
}