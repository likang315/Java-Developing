package com.atlantis.zeus.base.exception;

/**
 * 接口响应异常
 *
 * @author kangkang.li@qunar.com
 * @date 2021-04-13 17:20
 */
public class RespException extends AbstractBizException {

    public RespException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RespException(String msg, String resp) {
        super(String.format(msg + ", msg: %s", resp));
    }

    public RespException(String msg, String resp, Throwable cause) {
        super(String.format(msg + ", msg: %s", resp), cause);
    }
}
