package com.gwm.marketing.filter;

/**
 * @description: 常量定义
 * @author: fanht
 * @create: 2021-11-12 15:17
 **/
public class VerifyConst {

    /**
     * 手机号码验证正则表达式
     **/
    public static final String MOBILE_REGEX = "^1(3|4|5|6|7|8|9)[0-9]{9}$";
    /**
     * 账号的正则表达式
     **/
    public static final String ACCOUNT_REGEX = "^[a-zA-Z0-9_\\-]{3,30}$";
    /**
     * 电子邮箱的正则表达式
     **/
    public static final String EMAIL_REGEX = "^[A-Za-z0-9]+([-_\\.][A-Za-z0-9]+)*@([-A-Za-z0-9]+[\\.])+[A-Za-z0-9]+$";

    /**
     * 校验身份位数
     */
    public static final Integer NUMBERS = 17;

    /**如果是运行时异常 则不再打印*/
    public static final String UN_SEND_CLASS = "com.gwm.ora.filter.exception.DingdingHandleException";

    /**钉钉开关 开启*/
    public static final String DINGDING_SWITCH = "true";
    /**钉钉开关 关闭*/
    public static final String DINGDING_SWITCH_OFF = "false";
}