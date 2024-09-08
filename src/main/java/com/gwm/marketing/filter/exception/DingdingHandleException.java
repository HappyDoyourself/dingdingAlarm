package com.gwm.marketing.filter.exception;

import com.alibaba.fastjson.JSONObject;
import com.gwm.marketing.filter.DingdingAlarmUtil;
import com.gwm.marketing.filter.IpUtil;
import com.gwm.marketing.filter.buffertrigger.SimpleBufferTriggerUtils;
import com.gwm.marketing.filter.buffertrigger.Tag;
import com.gwm.marketing.filter.intercepter.ApplicationContext;
import com.gwm.marketing.filter.urlconfig.OptionalAlarmUriUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;


/**
 * 全局异常发送钉钉消息
 *
 * @author fanht
 * @date 2022/02/18
 */
@Configuration
public class DingdingHandleException extends AbstractHandlerExceptionResolver implements HandlerExceptionResolver {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private SimpleBufferTriggerUtils simpleBufferTriggerUtils;

    @Resource
    private OptionalAlarmUriUtils optionalAlarmUriUtils;

    /**
     * 重写HandlerExceptionResolver,实现异常可以全局捕获。也可以通过DingGlobalException来获取。但是无法打印其他的信息
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return
     */
    @Nullable
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) {
        if (ex instanceof Throwable && Objects.nonNull(ex)) {
            //如果nacos里面设置的有某些接口不想让告警，则不告警
            if(optionalAlarmUriUtils == null){
                optionalAlarmUriUtils = ApplicationContext.getBean(OptionalAlarmUriUtils.class);
            }
            if (!optionalAlarmUriUtils.isExistUri(request.getRequestURI())) {
                //todo 此处不要在打印error级别的日志了,MarketingLogFilter已经基于error级别打印了
                if (simpleBufferTriggerUtils == null) {
                    simpleBufferTriggerUtils = ApplicationContext.getBean(SimpleBufferTriggerUtils.class);
                }
                //todo 此处不要在打印error级别的日志了,MarketingLogFilter已经基于error级别打印了
                simpleBufferTriggerUtils.proceErrorAlarm(Tag.builder().applicationName(DingdingAlarmUtil.applicationName)
                        .env(DingdingAlarmUtil.env)
                        .ip(IpUtil.initIp())
                        .traceId(MDC.get("traceId"))
                        .requestUri(request.getRequestURI())
                        .exMessage(JSONObject.toJSON(ex.toString().length() > 500 ?
                                ex.toString().substring(0, 500) : ex.toString()).toString())
                        .build());
            }

        }
        logger.error("resolveException" , ex);
        return null;
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        return null;
    }
}
