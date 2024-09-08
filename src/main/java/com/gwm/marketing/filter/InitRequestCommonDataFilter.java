package com.gwm.marketing.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 对每一个请求，初始化各种变量及参数，以用于日志输出、展现层输出等目的
 *
 * @author fanht
 **/
@Configuration
@Order(value = 0)
@WebFilter(filterName = "initRequestCommonDataFilterRepeat", urlPatterns = "/*")
public class InitRequestCommonDataFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //增加其它共性的逻辑
        filterChain.doFilter(request, response);
    }
}