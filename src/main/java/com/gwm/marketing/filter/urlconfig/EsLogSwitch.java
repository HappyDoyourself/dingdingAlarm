package com.gwm.marketing.filter.urlconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:hongtaofan
 * @Version:1.0
 * @Description:
 * @Date: 2023/6/26 11:28
 */
@Configuration
@Setter
@Getter
@RefreshScope
@ConfigurationProperties(prefix = "dingding.switch")
public class EsLogSwitch {

    /**
     * 开关-后台日志是否进入es
     */
    private boolean logToEs;

    /**
     * 请求日志是否写入granfana
     */
    private boolean logToGranfana;
}
