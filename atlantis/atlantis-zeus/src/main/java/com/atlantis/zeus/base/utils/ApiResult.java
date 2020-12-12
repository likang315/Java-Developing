package com.atlantis.zeus.base.utils;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 封装的统一响应类
 *
 * @author kangkang.li@qunar.com
 * @date 2020-12-12 19:43
 */
@NoArgsConstructor
public class ApiResult<T> implements Serializable {
    /**
     * 状态返回值
     */
    private boolean ret;

    /**
     * 前端弹出消息
     */
    private String msg;

    /**
     * 后端具体传递给前端的消息
     */
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private T data;

    /**
     * 错误码
     */
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private Integer errcode;

    /**
     * 为了保证符合规范，我们闭合构造权限
     */
    private ApiResult(boolean ret) {
        this.ret = ret;
    }

    /**
     * eg.
     * <p/>
     * <pre>
     *      {
     *          "ret" : false,
     *          "msg" : "错误的id，修改失败",
     *          "code" : 1
     *      }
     * </pre>
     *
     * @param message
     * @param errcode
     * @return ApiResult
     */
    public static ApiResult error(String message, Integer errcode) {
        ApiResult result = new ApiResult(false);
        result.msg = message;
        result.errcode = errcode;
        return result;
    }

    /**
     * eg.
     * <p/>
     * <pre>
     *      {
     *          "ret" : false,
     *          "msg" : "错误的id，修改失败",
     *          "code" : 1
     *      }
     * </pre>
     *
     * @param message
     * @param data
     * @return ApiResult
     */
    public static <T> ApiResult error(String message, T data) {
        ApiResult result = new ApiResult(false);
        result.msg = message;
        result.data = data;
        return result;
    }

    /**
     * eg.
     * <p/>
     * <pre>
     *      {
     *          "ret" : false,
     *          "msg" : "错误的id，修改失败"
     *      }
     * </pre>
     *
     * @param message
     * @return ApiResult
     */
    public static ApiResult error(String message) {
        return error(message, null);
    }

    /**
     * eg.
     * <p/>
     * <pre>
     *      {
     *          "ret" : false
     *      }
     * </pre>
     *
     * @return ApiResult
     */
    public static ApiResult error() {
        return error(null, null);
    }

    /**
     * eg.
     * <p/>
     * <pre>
     *      {
     *          "ret" : false
     *          "code" : 1
     *      }
     * </pre>
     *
     * @param errcode 错误码
     * @return ApiResult
     */
    public static ApiResult error(Integer errcode) {
        return error(null, errcode);
    }

    /**
     * eg.
     * <p/>
     * <pre>
     *      {
     *          "ret" : true,
     *          "data": {
     *                      "encodedRID": "2916181129",
     *                      "operator": "gambol2",
     *                      "createTime": 1411363837776,
     *                      "rescueStatus": 1,
     *                      "pFunction": "free",
     *                      "departure": "北京",
     *                      "arrive": "大连"
     *          },
     *          "msg";"修改成功"
     *      }
     * </pre>
     *
     * @param object 对象
     * @param msg
     * @return ApiResult
     */
    public static <T> ApiResult success(T object, String msg) {
        ApiResult result = new ApiResult(true);
        result.data = object;
        result.msg = msg;
        return result;
    }

    /**
     * eg.
     * <p/>
     * <pre>
     *      {
     *          "ret" : true,
     *          "data": {
     *                      "encodedRID": "2916181129",
     *                      "operator": "gambol2",
     *                      "createTime": 1411363837776,
     *                      "rescueStatus": 1,
     *                      "pFunction": "free",
     *                      "departure": "北京",
     *                      "arrive": "大连"
     *          }
     *      }
     * </pre>
     *
     * @param object 对象
     * @return ApiResult
     */
    public static <T> ApiResult success(T object) {
        return success(object, "");
    }

    public static <T> ApiResult success(T object, String msg, Integer errcode) {
        ApiResult result = success(object, null);
        result.errcode = errcode;
        return result;
    }


    /**
     * eg.
     * <p/>
     * <pre>
     *      {
     *          "ret" : true,
     *          "msg";"修改成功"
     *      }
     * </pre>
     *
     * @param msg
     * @return ApiResult
     */
    public static ApiResult success(String msg) {
        return success(null, msg);
    }

    /**
     * eg.
     * <p/>
     * <pre>
     *      {
     *          "ret" : true
     *      }
     * </pre>
     *
     * @return ApiResult
     */
    public static ApiResult success() {
        return success(null, null);
    }

    /**
     * 获得ret值
     *
     * @return boolean
     */
    public boolean getRet() {
        return ret;
    }

    /**
     * 获得message
     *
     * @return String
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 获得数据
     *
     * @return Object
     */
    public Object getData() {
        return data;
    }

    public boolean isRet() {
        return ret;
    }

    public Integer getErrcode() {
        return errcode;
    }
}
