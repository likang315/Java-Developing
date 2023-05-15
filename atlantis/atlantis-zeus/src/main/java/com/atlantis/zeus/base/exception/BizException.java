package com.atlantis.zeus.base.exception;

import com.atlantis.zeus.base.utils.JsonUtils;

/**
 * 自定义业务处理异常类
 *
 * @author likang02@corp.netease.com
 * @date 2023/5/15 19:27
 */
public class BizException extends AbstractBizException {

    private static final long serialVersionUID = 4375182019260432338L;

    public BizException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BizException(String msg, Object obj) {
        super(String.format(msg + ", req param: %s", JsonUtils.writeValueAsString(obj)));
    }

    public BizException(String msg, String obj, Throwable cause) {
        super(String.format(msg + ", req param: %s", obj), cause);
    }

}
