package com.atlantis.zeus.base.aop;

import com.atlantis.zeus.base.exception.BizException;
import com.atlantis.zeus.base.utils.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器，作用域：从 Controller 开始
 *
 * @author likang02@corp.netease.com
 * @date 2023/5/15 19:22
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义的业务异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public ApiResult<String> bizExceptionHandler(HttpServletRequest req, BizException e) {
        log.error("biz error: url: {}, req: {}", req.getRequestURL(), req.getParameterMap(), e);
        return ApiResult.error(e.getMessage(), 500);
    }

    /**
     * 处理其他异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ApiResult<String> exceptionHandler(HttpServletRequest req, Exception e) {
        log.error("process req error: url: {}, req: {}", req.getRequestURL(), req.getParameterMap(), e);
        return ApiResult.error(e.getMessage(), 500);
    }
}
