package com.gwm.marketing.filter.slowsql;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import com.gwm.marketing.filter.log.DruidSqlLogger;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:hongtaofan
 * @Version:1.0
 * @Description:
 * @Date: 2023/8/8 14:34
 */
@Configuration
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@ConditionalOnClass(DruidDataSource.class)
@EnableConfigurationProperties({DruidStatProperties.class, DataSourceProperties.class})
@AutoConfigureAfter(DruidDataSourceAutoConfigure.class)
public class DruidSlowSqlConfig {

    @Bean
    @ConditionalOnMissingBean
    public DruidSlf4jImplFilter slowSqlFilter(){
        return new DruidSlf4jImplFilter();
    }
    @Bean
    public DruidSqlLogger druidLogger(){
        return new DruidSqlLogger();
    }
}
