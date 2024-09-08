package com.gwm.marketing.filter.buffertrigger;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:hongtaofan
 * @Version:1.0
 * @Description:配置参数
 * @Date: 2023/6/18 9:37
 */
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "dingding.trigger")
public class BufferTriggerConfig {

    /**
     * 错误日志最大条数
     */
    private  Long maxErrorLogBufferCount;

    private  Long maxTimeOutBufferCount;

    private  Long errorLogInterval;

    private  Long timeOutInterval;

    public Long getMaxErrorLogBufferCount() {
        return maxErrorLogBufferCount;
    }

    public void setMaxErrorLogBufferCount(Long maxErrorLogBufferCount) {
        this.maxErrorLogBufferCount = maxErrorLogBufferCount;
    }

    public Long getMaxTimeOutBufferCount() {
        return maxTimeOutBufferCount;
    }

    public void setMaxTimeOutBufferCount(Long maxTimeOutBufferCount) {
        this.maxTimeOutBufferCount = maxTimeOutBufferCount;
    }

    public Long getErrorLogInterval() {
        return errorLogInterval;
    }

    public void setErrorLogInterval(Long errorLogInterval) {
        this.errorLogInterval = errorLogInterval;
    }

    public Long getTimeOutInterval() {
        return timeOutInterval;
    }

    public void setTimeOutInterval(Long timeOutInterval) {
        this.timeOutInterval = timeOutInterval;
    }
}
