package com.gwm.marketing.filter.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import com.alibaba.fastjson.JSONObject;
import com.gwm.marketing.filter.DingdingAlarmUtil;
import com.gwm.marketing.filter.IpUtil;
import com.gwm.marketing.filter.buffertrigger.SimpleBufferTriggerUtils;
import com.gwm.marketing.filter.buffertrigger.Tag;
import com.gwm.marketing.filter.intercepter.ApplicationContext;
import com.gwm.marketing.filter.urlconfig.OptionalAlarmUriUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 异常日志堆栈-用于非运行时异常的告警提示
 * @author fanht
 * @date 2022-03-21  17:28
 * @versio 1.0
 */
@Component
public class MarketingLogFilter extends TurboFilter {

    /**rocketmq异常信息打印*/
    private static final String ROCKETMQ_ERROR = "rocketmq";

    @Resource
    private SimpleBufferTriggerUtils simpleBufferTriggerUtils;

    @Resource
    private OptionalAlarmUriUtils optionalAlarmUriUtils;
    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level, String s, Object[] objects, Throwable throwable) {
        if(logger.getName().contains("rocketmq")){
            System.out.println("mq error:" + (Optional.of(logger).map(l->l.getName()).orElse(null))+ ",throwable:" + Optional.ofNullable(throwable).map(t->t.getMessage()).orElse(null));
        }
        //todo 如果是非运行时异常 发送钉钉告警。排除运行时异常
        if(throwable != null && StringUtils.isNotEmpty(throwable.getMessage()) && level.equals(Level.ERROR)){
            if(optionalAlarmUriUtils == null){
                optionalAlarmUriUtils = ApplicationContext.getBean(OptionalAlarmUriUtils.class);
            }
            if(!optionalAlarmUriUtils.isExistUri(logger.getName())){
                if(simpleBufferTriggerUtils == null){
                    simpleBufferTriggerUtils =  ApplicationContext.getBean(SimpleBufferTriggerUtils.class);
                }
                simpleBufferTriggerUtils.proceErrorAlarm(Tag.builder().applicationName(DingdingAlarmUtil.applicationName)
                        .env(DingdingAlarmUtil.env)
                        .ip(IpUtil.initIp())
                        .traceId(MDC.get("traceId"))
                        .requestUri(logger.getName())
                        .exMessage(JSONObject.toJSON(throwable.getMessage().length() >500 ?
                                throwable.getMessage().substring(0,500).toString():throwable.getMessage()).toString())
                        .build());
            }
        }else if(Level.ERROR.equals(level) && logger.getName().contains(ROCKETMQ_ERROR)){
            //如果是mq的异常 也要进行告警
            if(!optionalAlarmUriUtils.isExistUri(logger.getName())){
                String exMessage = Optional.ofNullable(s).orElse("") + Optional.ofNullable(objects).map(t->t[0]).orElse(null);
                simpleBufferTriggerUtils.proceErrorAlarm(Tag.builder().applicationName(DingdingAlarmUtil.applicationName)
                        .env(DingdingAlarmUtil.env).ip(IpUtil.initIp()).requestUri(logger.getName()).traceId(MDC.get("traceId"))
                        .exMessage(Optional.ofNullable(exMessage).map(t->t.length()>500?t.substring(0,500):t).orElse(""))
                        .build());
            }
        }
        //todo 如果接入kafka,则此处需要需要改为NEUTRAL，以防止kakfa的多次打印
        return FilterReply.NEUTRAL;
    }
}
