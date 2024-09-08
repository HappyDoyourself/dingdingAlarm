package com.gwm.marketing.filter.intercepter;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.DispatcherServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author:hongtaofan
 * @Version:1.0
 * @Description:
 * @Date: 2023/4/26 18:21
 */
public class OraDispatcherServlet extends DispatcherServlet  {

    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws  Exception {
        if(isApplicationJson(request.getContentType()) && request.getInputStream().available() > 0){
            super.doDispatch(new OraHttpServletRequestWrapper(request), response);
        }else {
            super.doDispatch(request,response);
        }
    }


    private boolean isApplicationJson(String contentType) {
        if (StringUtils.isEmpty(contentType)) {
            return false;
        }
        //文本以及表单的走自定义流处理
        return contentType.contains("application/json") || contentType.contains("text/html") || contentType.contains("application/x-www-form-urlencoded");
    }

}
