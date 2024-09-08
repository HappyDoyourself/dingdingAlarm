package com.gwm.marketing.filter.intercepter;

import com.alibaba.fastjson.JSON;
import com.gwm.marketing.filter.ResponseResult;
import com.gwm.marketing.filter.anno.Verify;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 基于Spring MVC拦截器的参数合法性验证
 *
 * @author fanht
 **/
public class HttpParamVerifyInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(HttpParamVerifyInterceptor.class);

    private static final String REQUIRED_RULE = "required";

    private static final String RULE_SPLIT_STR = "\\|";

    /**
     * 每个URI的请求数计数器
     */
    private static final Map<String, AtomicLong> URI_COUNTER = new ConcurrentHashMap<String, AtomicLong>();
    private static final String HTTP_REQUEST_START_TIMESTAMP = "HTTP_REQUEST_START_TIMESTAMP";
    private static final String HTTP_REQUEST_STOP_WATCH = "HTTP_REQUEST_STOP_WATCH";

    /**
     * 规则校验器
     **/
    private Object validator = new HttpParamVerifyValidator();

    public Object getValidator() {
        return validator;
    }

    public void setValidator(Object validator) {
        this.validator = validator;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler instanceof HandlerMethod == false) {
            return true;
        }
        Method method = ((HandlerMethod) handler).getMethod();
        if (method.getParameterCount() == 0) {
            return true;
        }
        //一、针对方法中的每个参数，依次循环进行校验
        String invalidReason = this.execueValidate(request, method);
        //二、校验不通过
        if (invalidReason != null) {
            log.info("[PARAM_VERIFY_ERROR] " + invalidReason);
            ResponseResult ajaxResponse = ResponseResult.buildError("HTTP_PARAM_INVALID");
            ajaxResponse.setMsg(invalidReason);
            /**如果在此前进行任何request.getPragrmber()方法都会使字符转换失效！必须要在读取request的getParameter()和 getReader() 方法之前调用 否则字符转换失效 */
            response.setContentType("application/json;charset=utf-8");
            this.outJson(response, ajaxResponse);
            return false;
        } else {
            return true;
        }
    }

    private void outJson(HttpServletResponse response, ResponseResult ajaxResponse) {
        PrintWriter out = null;
        try {
            response.setStatus(HttpStatus.SC_OK);
            out = response.getWriter();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=UTF-8");
            response.setHeader("pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            /**返回客户端乱码问题*/
            response.setHeader("content-type", "application/json; charset=utf-8");
            out.write(JSON.toJSONString(ajaxResponse, true));
            return;
        } catch (Exception e) {
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private String execueValidate(HttpServletRequest request, Method method)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        for (Parameter p : method.getParameters()) {
            String invalidReason = this.execueValidate(request, method, p);
            if (invalidReason != null) {
                return invalidReason;
            }
        }
        return null;
    }

    private String execueValidate(HttpServletRequest request, Method method, Parameter p)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        Verify verify = p.getAnnotation(Verify.class);
        if (verify == null) {
            return null;
        }
        if (StringUtils.isEmpty(verify.param()) || StringUtils.isEmpty(verify.rule())) {
            return null;
        }
        //---------------------------------------------准备：本参数名称、参数值、规则列表
        String httpParam = verify.param().trim();
        String[] httpValues = request.getParameterValues(httpParam);
        List<String> rules = new ArrayList<String>();
        for (String rule : verify.rule().split(RULE_SPLIT_STR)) {
            if (StringUtils.isNotEmpty(rule.trim())) {
                rules.add(rule.trim());
            }
        }
        //----------------------------------------------先判断必须有值
        if (rules.contains(REQUIRED_RULE)) {
            if (httpValues == null || httpValues.length == 0) {
                return "参数" + httpParam + "：必须传入值";
            } else if (httpValues.length == 1) {
                if (StringUtils.isEmpty(httpValues[0])) {
                    return "参数" + httpParam + "：必须传入值";
                }
            } else if (httpValues.length > 1) {
                for (String v : httpValues) {
                    if (StringUtils.isEmpty(v)) {
                        return "参数" + httpParam + "：传入了多个值，但存在空值";
                    }
                }
            }
        }
        rules.remove(REQUIRED_RULE);
        //---------------------------------------------再判断其它规则
        if (httpValues == null) {
            return null;
        }
        String vadation = vadation(rules,httpParam,httpValues);
        if(vadation != null){
            return vadation;
        }
        return null;
    }

    private String vadation(List<String> rules,String httpParam,String[] httpValues)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException{
        for (String rule : rules) {
            //取得校验器方法名称、配置阀值、校验器方法对象
            String validatorMethodName = null;
            String validatorThreadhold = null;
            Method validatorMethod = null;
            if (rule.endsWith(")")) {
                validatorMethodName = rule.substring(0, rule.indexOf("("));
                validatorThreadhold = rule.substring(rule.indexOf("(") + 1, rule.lastIndexOf(")"));
                try {
                    validatorMethod = validator.getClass().getMethod(validatorMethodName, String.class, String.class);
                } catch (Exception ex) {
                    log.error("找不到方法：" + validator.getClass().getName() + "." + validatorMethodName);
                } finally {
                }
                if (validatorMethod == null) {
                    return "参数" + httpParam + "：无法校验此参数";
                }
                //对每个值，进行校验
                for (String value : httpValues) {
                    String invalidReason = (String) validatorMethod.invoke(validator, value, validatorThreadhold);
                    if (invalidReason != null) {
                        return "参数" + httpParam + "：" + invalidReason;
                    }
                }
            } else {
                validatorMethodName = rule;
                try {
                    validatorMethod = validator.getClass().getMethod(validatorMethodName, String.class);
                } catch (Exception ex) {
                    log.error("找不到方法：" + validator.getClass().getName() + "." + validatorMethodName);
                } finally {
                }
                if (validatorMethod == null) {
                    return "参数" + httpParam + "：无法校验此参数";
                }
                //对每个值，进行校验
                for (String value : httpValues) {
                    String invalidReason = (String) validatorMethod.invoke(validator, value);
                    if (invalidReason != null) {
                        return "参数" + httpParam + "：" + invalidReason;
                    }
                }
            }
        }
        return null;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }
}