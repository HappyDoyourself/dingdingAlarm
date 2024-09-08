package com.gwm.marketing.filter.urlconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @Author:hongtaofan
 * @Version:1.0
 * @Description:可以手动设置哪些接口不需要告警
 * @Date: 2023/6/20 9:18
 */
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "dingding.forbidden")
public class NonNeedAlarmUri {

    /**
     * 禁止的接口数组
     */
    private String[] uris;


    /**
     * 指定接口限制次数
     */
    private Map<String,Long> limitTimes;

    /**
     * 指定异常信息超过指定次数
     */
    private Map<String,Long>  exMessageLimits;

    public String[] getUris() {
        return uris;
    }

    public void setUris(String[] uris) {
        this.uris = uris;
    }

    public Map<String, Long> getLimitTimes() {
        return limitTimes;
    }

    public void setLimitTimes(Map<String, Long> limitTimes) {
        this.limitTimes = limitTimes;
    }

    public Map<String, Long> getExMessageLimits() {
        return exMessageLimits;
    }

    public void setExMessageLimits(Map<String, Long> exMessageLimits) {
        this.exMessageLimits = exMessageLimits;
    }
}
