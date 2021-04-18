package com.atlantis.zeus.base.exception;

/**
 * 自定义异常抽象类
 *
 * @author kangkang.li@qunar.com
 * @date 2021-04-13 17:11
 */
public abstract class AbstractBizException extends RuntimeException {

    private static final long serialVersionUID = 3630342547460796046L;

    public AbstractBizException() {
        super();
    }

    public AbstractBizException(Throwable cause) {
        super(cause);
    }


    public AbstractBizException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public AbstractBizException(String msgTemplate, Object... args) {
        super(String.format(msgTemplate, args));
    }

}
