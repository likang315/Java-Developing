package com.atlantis.zeus.base.aop;

import com.atlantis.zeus.base.annotation.LogRecord;
import com.atlantis.zeus.base.constant.AopOrderedConstant;
import com.atlantis.zeus.base.utils.JsonUtils;
import com.atlantis.zeus.index.pojo.entity.LogRecordDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.Ordered;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * 自定义日志记录器
 *
 * @author likang02@corp.netease.com
 * @date 2021-09-23 17:48
 */
@Aspect
@Slf4j
@Component
public class LogRecordInterceptor implements Ordered {

    /**
     * 自定义注解功能
     * 尽量少在业务代码里面加了一下与业务逻辑无关的代码
     *
     * @param jp
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.atlantis.zeus.base.annotation.LogRecord)")
    public Object logRecord(ProceedingJoinPoint jp) throws Throwable {
        Signature signature = jp.getSignature();
        if (signature instanceof MethodSignature) {
            Method method = ((MethodSignature) signature).getMethod();
            LogRecord annotation = method.getAnnotation(LogRecord.class);
            LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
            String[] params = discoverer.getParameterNames(method);
            Object[] args = jp.getArgs();
            EvaluationContext context = new StandardEvaluationContext();
            for (int i = 0; i < params.length; i++) {
                context.setVariable(params[i], args[i]);
            }

            boolean db = saveDb(annotation, context);
            log.info("LogRecordInterceptor_logRecord: result: {}", db);
            return jp.proceed();
        } else {
            return jp.proceed();
        }
    }

    /**
     * 构键记录参数落表
     *
     * @param annotation
     * @param context
     * @return
     */
    private boolean saveDb(LogRecord annotation, EvaluationContext context) {
        LogRecordDO recordDO = new LogRecordDO();
        // operatePerson
        if (StringUtils.isNotBlank(annotation.operatePerson())) {
            ExpressionParser parser = new SpelExpressionParser();
            Expression exp = parser.parseExpression(annotation.operatePerson());
            recordDO.setOperatePerson(exp.getValue(context, String.class));
        }
        // operateTime
        recordDO.setOperateTime(LocalDateTime.now().toString());
        // biz
        if (StringUtils.isNotBlank(annotation.biz())) {
            ExpressionParser parser = new SpelExpressionParser();
            Expression exp = parser.parseExpression(annotation.biz());
            Object obj = exp.getValue(context, Object.class);
            recordDO.setBiz(JsonUtils.writeValueAsString(obj));
        }

        // 落库
        int count = new Random().nextInt();
        return count > 0;
    }

    @Override
    public int getOrder() {
        return AopOrderedConstant.ORDERED_RECORD_TWO;
    }
}
