package com.gwm.marketing.filter.intercepter;

import com.alibaba.fastjson.JSON;
import com.gwm.marketing.filter.CharacterFilter;
import com.gwm.marketing.filter.ServletUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证参数合法性拦截器
 *
 * @author fanht
 */
public class CharacterValidInterceptors extends HandlerInterceptorAdapter {

    private static final Log log = LogFactory.getLog(CharacterValidInterceptors.class);

    /**
     * 执行方法前
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        Map<String, String> param = ServletUtils.getParameters(request);
        String r = CharacterFilter.validScriptFilter(param);
        if (r != null) {
            Map json = new HashMap(16);
            json.put("status", "0");
            json.put("message", "抱歉，您的请求参数[" + r + "]输入不合法");
            json.put("result", null);
            log.error("请求参数含有SCRIPT注入");
            response.getWriter().write(JSON.toJSONString(json));
            return false;
        }
        return super.preHandle(request, response, handler);
    }

}
