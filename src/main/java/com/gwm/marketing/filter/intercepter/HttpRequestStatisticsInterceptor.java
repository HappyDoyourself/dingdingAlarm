package com.gwm.marketing.filter.intercepter;

import com.alibaba.fastjson.JSONObject;
import com.gwm.marketing.document.MonitorDocument;
import com.gwm.marketing.document.UserLogRecordDocument;
import com.gwm.marketing.filter.*;
import com.gwm.marketing.filter.buffertrigger.SimpleBufferTriggerUtils;
import com.gwm.marketing.filter.buffertrigger.Tag;
import com.gwm.marketing.filter.urlconfig.EsLogSwitch;
import com.gwm.marketing.filter.urlconfig.OptionalAlarmUriUtils;
import com.gwm.marketing.mq.producer.message.SysUserLogProducer;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * HTTP 打印请求参数、打印请求状况统计
 *
 * @author fanht
 **/
@Component
@Order(1)
public class HttpRequestStatisticsInterceptor extends HandlerInterceptorAdapter implements HandlerInterceptor, InitializingBean {

    private static final String SOURCE_TYPE = "SYSTEM";

    @Resource
    private SysUserLogProducer sysUserLogProducer;
    private static final String TRACE_ID = "traceId";

    private static final Logger log = LoggerFactory.getLogger(HttpRequestStatisticsInterceptor.class);

    /**
     * 每个URI的请求数计数器
     */
    private static final Map<String, AtomicLong> URI_COUNTER = new ConcurrentHashMap<String, AtomicLong>();
    private static final String HTTP_REQUEST_START_TIMESTAMP = "HTTP_REQUEST_START_TIMESTAMP";
    @Resource
    private SimpleBufferTriggerUtils simpleBufferTriggerUtils;
    @Resource
    private OptionalAlarmUriUtils optionalAlarmUriUtils;

    @Resource
    private EsLogSwitch esLogSwitch;

    @PostConstruct
    private  void  init(){
        log.info("init esLogSwitch:" + esLogSwitch);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //写入初始时间
        request.setAttribute(HTTP_REQUEST_START_TIMESTAMP, System.currentTimeMillis());
        request.setAttribute(TRACE_ID, TraceUtils.createTraceId());
        /*更新URI 请求数计数器*/
        String uri = request.getRequestURI();
        if (!URI_COUNTER.containsKey(uri)) {
            URI_COUNTER.put(uri, new AtomicLong(1));
        } else {
            AtomicLong atomicLong = URI_COUNTER.get(uri);
            atomicLong.incrementAndGet();
        }
        //输出URI及参数详情
        StringBuffer queryString = new StringBuffer();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            queryString.append(paramName + "=");
            if (paramValues != null) {
                if (paramValues.length == 1) {
                    queryString.append(paramValues[0]);
                } else {
                    queryString.append(Arrays.asList(paramValues));
                }
            }
            if (paramNames.hasMoreElements()) {
                queryString.append("&");
            }
        }
        log.info("[HTTP_STATIS] " + uri + " ,QS: " + queryString.toString());
        queryString = null;
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.error("afterCompletion", ex);
        //读出初始时间
        long startTimestamp = (long) request.getAttribute(HTTP_REQUEST_START_TIMESTAMP);
        /*更新URI 请求数计数器*/
        String uri = request.getRequestURI();
        if (!URI_COUNTER.containsKey(uri)) {
            URI_COUNTER.put(uri, new AtomicLong(1));
        } else {
            AtomicLong atomicLong = URI_COUNTER.get(uri);
            atomicLong.incrementAndGet();
        }
        long costMiliseconds = System.currentTimeMillis() - startTimestamp;
        String dingdingAlermSwitch = DingdingAlarmUtil.dingdingAlermSwitch;
        if (VerifyConst.DINGDING_SWITCH_OFF.equals(dingdingAlermSwitch)) {
            log.debug("开关关闭，不发送钉钉报警通知");
        } else {
            //如果接口不在禁用名单里面则进行告警提示
            if(optionalAlarmUriUtils == null){
                optionalAlarmUriUtils = ApplicationContext.getBean(OptionalAlarmUriUtils.class);
            }
            if (!optionalAlarmUriUtils.isExistUri(uri)) {
                if (simpleBufferTriggerUtils == null) {
                    simpleBufferTriggerUtils = ApplicationContext.getBean(SimpleBufferTriggerUtils.class);
                }
                if (null != ex) {
                    simpleBufferTriggerUtils.proceErrorAlarm(Tag.builder().applicationName(DingdingAlarmUtil.applicationName).env(DingdingAlarmUtil.env).ip(IpUtil.initIp()).traceId(MDC.get("traceId")).requestUri(request.getRequestURI())
                            .method(request.getMethod()).exMessage(JSONObject.toJSON(ex.toString().length() > 500 ? ex.toString().substring(0, 500) : ex.toString()).toString()).build());
                } else {
                    if (costMiliseconds > DingdingAlarmUtil.dingdingAlermTimeOut) {
                        simpleBufferTriggerUtils.proceTimeOutAlarm(Tag.builder().applicationName(DingdingAlarmUtil.applicationName).env(DingdingAlarmUtil.env).ip(IpUtil.initIp()).traceId(MDC.get("traceId")).requestUri(request.getRequestURI())
                                .method(request.getMethod()).timeOut(costMiliseconds).build());
                    }
                }
            }
        }
        //进入es时间,注意es的时区是UTC,比当前时间快了8小时。故需要减去8小时
        try {
            logToEs(request,response,handler,costMiliseconds);
        } catch (Exception e) {
            log.error("es入库异常", e);
        }
        request.removeAttribute(HTTP_REQUEST_START_TIMESTAMP);
        //销毁traceId放到网,因为创建就是在网关处创建的
    }


    private  void  logToEs(HttpServletRequest request, HttpServletResponse response,Object handler,long costMiliseconds ){
        try {
            if(esLogSwitch == null){
                esLogSwitch = SpringApplicationContextUtil.getBean(EsLogSwitch.class);
            }
            if(esLogSwitch.isLogToGranfana()){
                //进入es时间,注意es的时区是UTC,比当前时间快了8小时。故需要减去8小时
                String nowTime = LocalDateTime.now().minusHours(8L).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                MonitorDocument m = new MonitorDocument();
                m.setIp(IpUtil.initIp());
                m.setStatus(String.valueOf(response.getStatus()));
                m.setMethod(request.getMethod());
                m.setCostTime(costMiliseconds);
                m.setApiUrl(request.getRequestURI());
                m.setAppName(DingdingAlarmUtil.applicationName);
                m.setTraceId(request.getAttribute(TRACE_ID) == null ? null : request.getAttribute(TRACE_ID).toString());
                m.setCreateTime(nowTime);
                if (sysUserLogProducer == null) {
                    sysUserLogProducer = SpringApplicationContextUtil.getBean(SysUserLogProducer.class);
                }
                /*es展示granfana.现在使用的是mq,后面如果流量比较大使用kafka;如果是kafka 使用 kafkaUtils.kafkaSendMsg(KafkaTopicConfiguration.grafanaTopic,m);*/
                sysUserLogProducer.sendUserLogMgs(m);
            }
            if(esLogSwitch.isLogToEs()){
                //es存放用户后台登陆数据 通过traceId进行一次请求的标识
                String requestBody = null;
                if (SOURCE_TYPE.equals(request.getHeader(OraConstants.HEADER_SOURCE_TYPE))) {
                    if (isApplicationJson(request.getContentType())) {
                        try {
                            StringBuilder sb = new StringBuilder();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                sb.append(line);
                            }
                            requestBody = sb.toString();
                        } catch (IOException e) {
                            log.error("stream close exception.debug 模式异常");
                        }
                    }
                    //如果是Controller本身，且上面含有ApiOpertaion注解,直接取其值；否则获取其接口上的注解(代码结构新增了client)
                    String operationName = getOperationName(handler);
                    String requstAddress = StringUtils.isNotEmpty(request.getQueryString()) ? request.getRequestURI().concat("?").concat(request.getQueryString()) : request.getRequestURI();
                    UserLogRecordDocument logRecord = UserLogRecordDocument.builder().createTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).requestMethod(request.getMethod())
                            .authorization(request.getHeader(OraConstants.HEADER_AUTHORAZATION)).loginIp(IpUtil.initIp()).userAgent(request.getHeader(OraConstants.HEADER_USER_AGENT))
                            .status(String.valueOf(response.getStatus())).operationName(operationName)
                            .jsonParam(requestBody).sourceType(request.getHeader("sourceType") == null ? null : request.getHeader("sourceType")).requestAddress(requstAddress)
                            .traceId(request.getAttribute(TRACE_ID) == null ? null : request.getAttribute(TRACE_ID).toString()).costTime(costMiliseconds).build();
                    sysUserLogProducer.sendUserLogMgs(logRecord);
                }
            }
        } catch (Exception e) {
            log.error("es入库异常", e);
        }
    }
    private String getOperationName(Object handler) {
        //如果是Controller本身，且上面含有ApiOpertaion注解,直接取其值；否则获取其接口上的注解(代码结构新增了client)
        String operationName = "";
        try {
            operationName = ((HandlerMethod) handler).getMethod().getAnnotation(ApiOperation.class) == null ? null : ((HandlerMethod) handler).getMethod().getAnnotation(ApiOperation.class).value();
            if (StringUtils.isEmpty(operationName)) {
                Method m = ((HandlerMethod) handler).getMethod();
                Class<?> declaringClass = m.getDeclaringClass();
                if (declaringClass.getInterfaces().length > 0) {
                    //注意 如果是同名的构造方法，获取的是第一个
                    Method[] methods = declaringClass.getInterfaces()[0].getMethods();
                    Method interfaceMethod = Arrays.stream(methods).filter(method -> method.getName().equals(m.getName())).findFirst().orElse(null);
                    if (interfaceMethod != null) {
                        operationName = interfaceMethod.getAnnotation(ApiOperation.class) != null ? interfaceMethod.getAnnotation(ApiOperation.class).value() : null;
                    }
                }
            }
        } catch (SecurityException e) {
            log.error("获取操作异常");
            return operationName;
        }
        return operationName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    private boolean isApplicationJson(String contentType) {
        if (StringUtils.isEmpty(contentType)) {
            return false;
        }
        //文本以及表单的可以记录入参，视频、音频、图片不入库
        return contentType.contains("application/json") || contentType.contains("text/html") || contentType.contains("application/x-www-form-urlencoded");
    }

}