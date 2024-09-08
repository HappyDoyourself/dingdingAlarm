package com.gwm.marketing.filter.urlconfig;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @Author:hongtaofan
 * @Version:1.0
 * @Description:判断是否含有指定的uri
 * @Date: 2023/6/20 10:44
 */
public class OptionalAlarmUriUtils {

    @Resource
    private NonNeedAlarmUri nonNeedAlarmUri;

    @PostConstruct
    public void init(){
        System.out.println("init nonNeedAlarmUri" + nonNeedAlarmUri);
    }
    public boolean isExistUri(String uri){
        try {
            Optional<NonNeedAlarmUri> nonNeedAlarmUriOptional = Optional.ofNullable(nonNeedAlarmUri);
            return nonNeedAlarmUriOptional.map(t -> Arrays.stream(t.getUris())).orElse(Stream.empty())
                    .anyMatch(uri::equals);
        } catch (Exception e) {
            //如果异常,则继续
            return false;
        }
    }
}
