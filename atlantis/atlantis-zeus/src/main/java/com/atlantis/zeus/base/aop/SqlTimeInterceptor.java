package com.atlantis.zeus.base.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 自定义 MyBatis 拦截器
 *
 * @author likang02@corp.netease.com
 * @date 2023/8/16 20:22
 */
@Slf4j
@Component
@Intercepts({
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
        ),
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        )
})
public class SqlTimeInterceptor implements Interceptor {

    /**
     * 拦截执行方法
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    public Object intercept(Invocation invocation) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = invocation.proceed();
        long endTime = System.currentTimeMillis();
        // 获取执行的 SQL 语句
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        String mapperId = mappedStatement.getId();
        BoundSql boundSql = mappedStatement.getBoundSql(invocation.getArgs()[1]);
        String sql = boundSql.getSql();
        Map<String, Object> paramMap = (Map<String, Object>) boundSql.getParameterObject();

        // 输出执行时间和 SQL 语句
        log.info(String.format("Mapper: %s\nSQL: %s\nparams: %s\n耗时：%d 毫秒",
                mapperId, sql, paramMap.toString(), endTime - startTime));

        return result;
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}
