package com.gwm.marketing.filter.intercepter;


import com.gwm.marketing.filter.VerifyConst;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/***
 * @Description: 参数校验
 * @Author: fanht
 * @Date: 2021/11/12
 */
public class HttpParamVerifyValidator {

    private static final Predicate<String> NOT_NUMBER_PREDICATE = Pattern.compile("^\\d+$").asPredicate().negate();

    private static final int ID_CARD_LENGTH = 15;

    /**
     * 校验最小值限制
     *
     * @param value      1
     * @param threadHold 2
     * @return: java.lang.String
     */
    public String min(String value, String threadHold) {
        if (StringUtils.isEmpty(value) || StringUtils.isEmpty(threadHold)) {
            return null;
        }
        if (new BigDecimal(value).compareTo(new BigDecimal(threadHold)) == -1) {
            return "传入值" + value + "，小于最小值" + threadHold;
        }
        return null;
    }

    /**
     * 校验最大值限制
     *
     * @param value      1
     * @param threadHold 2
     * @return: java.lang.String
     */
    public String max(String value, String threadHold) {
        if (StringUtils.isEmpty(value) || StringUtils.isEmpty(threadHold)) {
            return null;
        }
        if (new BigDecimal(value).compareTo(new BigDecimal(threadHold)) == 1) {
            return "传入值" + value + "，大于最大值" + threadHold;
        }
        return null;
    }

    /**
     * 校验电子邮箱
     *
     * @param value 1
     * @return: java.lang.String
     */
    public String email(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        String regexp = VerifyConst.EMAIL_REGEX;
        if (!value.matches(regexp)) {
            return "传入值" + value + "，电子邮箱格式错误";
        }
        return null;
    }

    /**
     * 校验手机号码
     *
     * @param value 1
     * @return: java.lang.String
     */
    public String mobile(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        String regexp = VerifyConst.MOBILE_REGEX;
        if (!value.matches(regexp)) {
            return "传入值" + value + "，手机号码格式错误";
        }
        return null;
    }

    /**
     * 校验身份证号码
     *
     * @param value 1
     * @return: java.lang.String
     */
    public String idcard(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        if (value.trim().length() == ID_CARD_LENGTH) {
            return null;
        }
        String regexp = "^[1-9]{1}[0-9]{16}[0-9xX]{1}$";
        if (!value.matches(regexp)) {
            return "传入值" + value + "，身份证号码格式错误";
        }
        if (check(value) == false) {
            return "传入值" + value + "，身份证号码不正确";
        }
        return null;
    }

    /**
     * 自定义正则表达式
     *
     * @param value      1
     * @param threadHold 2
     * @return: java.lang.String
     */
    public String regExp(String value, String threadHold) {
        if (StringUtils.isEmpty(value) || StringUtils.isEmpty(threadHold)) {
            return null;
        }
        String regexp = threadHold;
        if (!value.matches(regexp)) {
            return "传入值" + value + "，格式错误";
        }
        return null;
    }

    /**
     * 校验逗号分隔的数字
     *
     * @param value 1
     * @return: java.lang.String
     */
    public String multiNumber(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        String[] numbers = value.split(",");
        List<String> notNumbers = Arrays.asList(numbers).parallelStream().filter(NOT_NUMBER_PREDICATE).collect(Collectors.toList());
        if (notNumbers != null && notNumbers.size() > 0) {
            return "格式错误，不是逗号分隔的数字";
        }
        return null;
    }


    /**
     * 身份证号码是否合法
     **/
    public static boolean check(String no) {
        // 1-17位相乘因子数组
        int[] factor = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        // 18位随机码数组
        char[] random = "10X98765432".toCharArray();
        // 计算1-17位与相应因子乘积之和
        int total = 0;
        for (int i = 0; i < VerifyConst.NUMBERS; i++) {
            total += Character.getNumericValue(no.charAt(i)) * factor[i];
        }
        // 判断随机码是否相等
        return random[total % 11] == no.charAt(17);
    }
}