package com.gwm.marketing.filter;

import com.alibaba.fastjson.JSON;
import com.gwm.marketing.filter.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

/**
 * 接口返回数据统一模板
 *
 * @param <T>
 * @author fanht
 */
public class ResponseResult<T> {

    private static Logger logger = LoggerFactory.getLogger(ResponseResult.class);

    /**
     * 1 - 成功
     */
    public static final int SUCCESS_CODE = 1;

    /**
     * 0 - 失败
     */
    public static final int ERROR_CODE = 0;

    /**
     * 成功msg
     */
    public static final String SUCCESS_MSG = "SUCCESS";

    /**
     * 失败msg
     */
    public static final String ERROR_MSG = "ERROR";

    /**
     *
     */
    public static final String SIGN_ERROR_MSG = "SIGN_ERROR";

    /**
     * 最大异常信息
     */
    private static final int ERROR_MSG_LENGTH = 100;

    /**
     * 成功
     */
    public static final ResponseResult SUCCESS = new ResponseResult(SUCCESS_CODE, SUCCESS_MSG);

    /**
     * 失败
     */
    public static final ResponseResult ERROR = new ResponseResult(ERROR_CODE, ERROR_MSG);

    /**
     * 返回状态码
     */
    private int code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 数据类型
     */
    private T data;

    public ResponseResult() {

    }

    public ResponseResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ResponseResult<T> build(int code, String msg, T data) {
        return new ResponseResult<>(code, msg, data);
    }

    public static <T> ResponseResult<T> build(int code, String msg) {
        return new ResponseResult<>(code, msg);
    }

    public static <T> ResponseResult<T> buildError(String msg) {
        return new ResponseResult<>(ERROR_CODE, msg);
    }

    public static <D> ResponseResult<D> responseResultHandle(Supplier<D> supplier, String exceptionPrefix) {
        return responseResultHandle(supplier, logger, exceptionPrefix);
    }

    public static <D> ResponseResult<D> responseResultHandle(Supplier<D> supplier, Logger logger, String exceptionPrefix) {

        ResponseResult<D> responseResult;
        try {
            D d = supplier.get();
            responseResult = build(SUCCESS_CODE, SUCCESS_MSG, d);
        } catch (Exception e) {
            responseResult = exceptionHandle(e, logger, exceptionPrefix);
        }

        logger.info("{} responseResult: {}", exceptionPrefix, JSON.toJSON(responseResult));

        return responseResult;
    }

    /**
     * 异常处理
     *
     * @param e               异常
     * @param logger          日志
     * @param exceptionPrefix 异常前缀
     * @return RestResponse
     */
    public static <T> ResponseResult<T> exceptionHandle(Exception e, Logger logger, String exceptionPrefix) {

        boolean errorLevel = true;
        String errorMsg = "";
        if (e instanceof ServiceException) {
            ServiceException se = (ServiceException) e;
            errorLevel = se.isErrorLevel();
            errorMsg = e.getMessage();
        } else {
            errorMsg = "系统内部异常";
        }

        if (errorLevel) {
            logger.error("{} Exception:", exceptionPrefix, e);
        } else {
            logger.info("{} exceptionName={},exceptionMsg={}", exceptionPrefix, e.getClass().getName(), e.getMessage());
        }

        return buildError(errorMsg);
    }

    public static <T> ResponseResult<T> fail(int code, String msg) {
        return new ResponseResult<>(code, msg);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

}
