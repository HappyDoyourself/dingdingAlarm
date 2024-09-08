package com.gwm.marketing.filter.buffertrigger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.phantomthief.collection.BufferTrigger;
import com.gwm.marketing.filter.DingdingAlarmUtil;
import com.gwm.marketing.filter.urlconfig.NonNeedAlarmUri;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.Collections.synchronizedList;

/**
 * @Author:hongtaofan
 * @Version:1.0
 * @Description:集中处理钉钉告警信息
 * @Date: 2023/6/17 19:50
 */
@Component
public class SimpleBufferTriggerUtils {

    @Resource
    private NonNeedAlarmUri nonNeedAlarmUri;

    @Resource
    private BufferTriggerConfig bufferTriggerConfig;

    BufferTrigger<Tag> errorBufferTrigger = BufferTrigger.<Tag, List<Tag>>simple()
            .maxBufferCount(bufferTriggerConfig==null?1000:bufferTriggerConfig.getMaxErrorLogBufferCount())
            .interval(bufferTriggerConfig==null?60:bufferTriggerConfig.getErrorLogInterval(), TimeUnit.SECONDS)
            .setContainer(() -> synchronizedList(new ArrayList<>()), List::add)
            .consumer(this::consumerAlarmMess).build();

     BufferTrigger<Tag> timeOutBufferTrigger = BufferTrigger.<Tag, List<Tag>>simple()
            .maxBufferCount(bufferTriggerConfig==null?1000:bufferTriggerConfig.getMaxTimeOutBufferCount())
            .interval(bufferTriggerConfig==null?60:bufferTriggerConfig.getTimeOutInterval(), TimeUnit.SECONDS)
            .setContainer(() -> synchronizedList(new ArrayList<>()), List::add)
            .consumer(this::consumerTimeOutAlarmMess)
            .build();


    public void proceErrorAlarm(Tag tag) {
        errorBufferTrigger.enqueue(tag);
    }

    private void consumerAlarmMess(List<Tag> stringList) {
        Map<Tag,Long> map = stringList.stream().collect(Collectors.groupingBy(t->t,Collectors.mapping(m->new Tag(m.getExMessage(),m.getRequestUri()),Collectors.counting())));
        //告警次数可以配置大于指定次数才发出告警
        Optional<NonNeedAlarmUri> nonNeedAlarmUriOptional = Optional.ofNullable(nonNeedAlarmUri);
        Map<String,Long> mapTimes = nonNeedAlarmUriOptional.map(NonNeedAlarmUri::getLimitTimes).orElse(Collections.EMPTY_MAP);
        Map<String,Long> exMesLimits = nonNeedAlarmUriOptional.map(NonNeedAlarmUri::getExMessageLimits).orElse(Collections.EMPTY_MAP);
        //排除指定接口大于指定次数的信息
        map.keySet().removeIf(key->mapTimes.containsKey(key.getRequestUri()) && map.get(key) < mapTimes.get(key.getRequestUri()));
        if(map.size() >  0 && exMesLimits.size() > 0){
            //排除指定异常信息大于指定次数的信息
            map.keySet().removeIf(t -> exMesLimits.containsKey(t.getExMessage()) && map.get(t) < exMesLimits.get(t.getExMessage()));
        }
        if(map.size()> 0){
            String mess = "接口异常告警:" + (bufferTriggerConfig==null?"60":bufferTriggerConfig.getErrorLogInterval()) + "秒内总告警条数:" + (map.values().stream().mapToLong(Long::longValue).sum())  + "条;";
            String result = map.entrySet().stream().map(e->getMessage(e.getKey()) + ":" + e.getValue() + "条").collect(Collectors.joining());
            DingdingAlarmUtil.sendDingdingAlerm(mess + result, DingdingAlarmUtil.dingdingTokenUrl);
        }
    }

    public void proceTimeOutAlarm(Tag timeOutMess) {
        timeOutBufferTrigger.enqueue(timeOutMess);
    }

    private  void consumerTimeOutAlarmMess(List<Tag> stringList) {
        //统计key并计算重复的次数
        Map<Tag,Long> map  = stringList.stream().collect(Collectors.groupingBy(tag -> tag,Collectors.mapping(t->new Tag(t.getRequestUri()),Collectors.counting())));
        Optional<NonNeedAlarmUri> nonNeedAlarmUriOptional = Optional.ofNullable(nonNeedAlarmUri);
        Map<String,Long>  mapTimes = nonNeedAlarmUriOptional.map(NonNeedAlarmUri::getLimitTimes).orElse(Collections.EMPTY_MAP);
        map.keySet().removeIf(key -> mapTimes.containsKey(key.getRequestUri()) && map.get(key) < mapTimes.get(key.getRequestUri()));
        if(map.size()> 0){
            String mess = "接口超时告警:" + (bufferTriggerConfig==null?"60": bufferTriggerConfig.getTimeOutInterval())+ "秒内总告警条数:" + (map.values().stream().mapToLong(Long::longValue).sum()) + "条;";
            String result = map.entrySet().stream().map(e->getMessage(e.getKey()) + ":" + e.getValue() + "条").collect(Collectors.joining());
            DingdingAlarmUtil.sendDingdingAlerm(mess + result, DingdingAlarmUtil.dingdingTokenUrl);
        }
    }


    private String getMessage(Tag tag){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            String res = objectMapper.writeValueAsString(tag);
            return  res;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
