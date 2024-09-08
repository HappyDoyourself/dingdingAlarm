package com.gwm.marketing.filter.exception;

/**
 * 服务异常
 *
 * @author fanht
 * @date 2021/11/12
 */
public class ServiceException extends RuntimeException {

    /**
     * 异常码
     */
    private int code;

    /**
     * 是否为error级别异常，默认为是<br>
     * error级别异常，打印异常信息到error级别日志中;非error级别异常，打印异常信息到info级别日志中。
     */
    private boolean errorLevel = true;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(int restErrorCode) {
        this(restErrorCode, RestErrorCode.renderMsg(restErrorCode));
    }

    public ServiceException(int code, String message) {
        this(code, message, true);
    }

    public ServiceException(int code, String message, boolean errorLevel) {
        super(message);
        this.code = code;
        this.errorLevel = errorLevel;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isErrorLevel() {
        return errorLevel;
    }

    public void setErrorLevel(boolean errorLevel) {
        this.errorLevel = errorLevel;
    }

    @Override
    public String toString() {
        return "ServiceException[code=" + code + ",message=" + getMessage() + ",errorLevel=" + errorLevel + "]";
    }

    /**
     * 服务参数非法异常
     */
    public static class InvalidParamException extends ServiceException {
        public InvalidParamException(String message) {
            super(message);
        }
    }

}
