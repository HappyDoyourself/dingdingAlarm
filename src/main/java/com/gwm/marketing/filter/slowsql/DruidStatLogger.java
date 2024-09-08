package com.gwm.marketing.filter.slowsql;

import com.alibaba.druid.pool.DruidDataSourceStatLogger;
import com.alibaba.druid.pool.DruidDataSourceStatLoggerAdapter;
import com.alibaba.druid.pool.DruidDataSourceStatValue;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Properties;


/**
 * @Author:hongtaofan
 * @Version:1.0
 * @Description:druid作者wenshao推荐使用这种的，实际上发现实现不了
 * @Date: 2023/7/11 14:31
 */
public class DruidStatLogger extends DruidDataSourceStatLoggerAdapter implements DruidDataSourceStatLogger {

    public DruidStatLogger() {
        super();
        System.out.println("DruidStatLogger:");
    }

    @Override
    public void log(DruidDataSourceStatValue statValue) {
        super.log(statValue);
        System.out.println("DruidStatLogger log:" + JSONObject.toJSON(statValue));
    }

    @Override
    public void configFromProperties(Properties properties) {
        super.configFromProperties(properties);
        System.out.println("DruidStatLogger configFromProperties:" + JSONObject.toJSON(properties));
    }

    @Override
    public void setLogger(Log logger) {
        super.setLogger(logger);
        System.out.println("DruidStatLogger setLogger:" + JSONObject.toJSON(logger));
    }

    @Override
    public void setLoggerName(String loggerName) {
        super.setLoggerName(loggerName);
        System.out.println("DruidStatLogger setLoggerName:" + loggerName);
    }
}
