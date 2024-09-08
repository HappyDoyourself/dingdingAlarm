package com.gwm.marketing.filter.slowsql;


import com.alibaba.druid.support.logging.SLF4JImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:hongtaofan
 * @Version:1.0
 * @Description:重写其实现日志 通过实时日志达到实时钉钉告警的目的
 * @Date: 2023/7/10 11:39
 */
@Configuration
public class DruidSlf4jImplFilter extends SLF4JImpl {


    private static final Logger logger = LoggerFactory.getLogger(DruidSlf4jImplFilter.class);


    public DruidSlf4jImplFilter(String loggerName) {
        super(loggerName);
        System.out.println("**************DruidSlf4jImplFilter**********" + loggerName);
    }
    public DruidSlf4jImplFilter(){
        super("com.gwm.marketing.filter.slowsql.DruidSlf4jImplFilter");
    }

    @Override
    public void error(String msg, Throwable e) {
        super.error(msg, e);
    }

    @Override
    public void error(String msg) {
        System.out.println("子类重写父类方法:" + msg);
        super.error(msg);
        System.out.println("子类重写父类方法:" + msg);
    }

    public DruidSlf4jImplFilter(LocationAwareLogger log) {
        super(log);
    }

    @Override
    public boolean isDebugEnabled() {
        return super.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return super.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        super.info(msg);
    }

    @Override
    public void debug(String msg) {
        super.debug(msg);
    }

    @Override
    public void debug(String msg, Throwable e) {
        super.debug(msg, e);
    }

    @Override
    public boolean isWarnEnabled() {
        return super.isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return super.isErrorEnabled();
    }

    @Override
    public void warn(String msg) {
        super.warn(msg);
    }

    @Override
    public void warn(String msg, Throwable e) {
        super.warn(msg, e);
    }

    @Override
    public int getErrorCount() {
        return super.getErrorCount();
    }

    @Override
    public int getWarnCount() {
        return super.getWarnCount();
    }

    @Override
    public int getInfoCount() {
        return super.getInfoCount();
    }

    @Override
    public int getDebugCount() {
        return super.getDebugCount();
    }

    @Override
    public void resetStat() {
        super.resetStat();
    }
}
