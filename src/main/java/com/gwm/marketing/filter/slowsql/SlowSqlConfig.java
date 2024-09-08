package com.gwm.marketing.filter.slowsql;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:hongtaofan
 * @Version:1.0
 * @Description: 慢sql属性配置
 * @Date: 2023/8/15 22:15
 */
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "spring.druid")
public class SlowSqlConfig {

    /**druid超过指定时间才会进行慢sql日志打印以及参数的入参打印*/
    private Long slowSqlMillis;

    public Long getSlowSqlMillis() {
        return slowSqlMillis;
    }

    public void setSlowSqlMillis(Long slowSqlMillis) {
        this.slowSqlMillis = slowSqlMillis;
    }
}
