package com.gwm.marketing.filter;

import java.util.Map;

/**
 * @Description: 功能描述
 * @Author: fanht
 * @Date: 2021/11/12
 */
public class CharacterFilter {

    public static String validScriptFilter(Map<String, String> param) {
        for (String key : param.keySet()) {
            if (param.get(key).toUpperCase().indexOf("%3C") != -1) {
                return key;
            }
            if (param.get(key).toUpperCase().indexOf("%3E") != -1) {
                return key;
            }
            if (param.get(key).toLowerCase().indexOf("script") != -1) {
                return key;
            }
            if (param.get(key).toLowerCase().indexOf("javascript:") != -1) {
                return key;
            }
            if (param.get(key).toLowerCase().indexOf("alert") != -1) {
                return key;
            }
            if (param.get(key).toLowerCase().indexOf("onerror") != -1) {
                return key;
            }
        }
        return null;
    }
}
