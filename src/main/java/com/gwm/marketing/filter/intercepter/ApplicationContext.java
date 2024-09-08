package com.gwm.marketing.filter.intercepter;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author fanht
 * @descrpiton 初始化时候无法获取bean,可以通过这种方式实例化
 * @date 2023/2/27 18:01:33
 * @versio 1.0
 */
@Component
public class ApplicationContext implements ApplicationContextAware {
    private static org.springframework.context.ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext) throws BeansException {
        if(ApplicationContext.applicationContext == null) {
            ApplicationContext.applicationContext = applicationContext;
        }
    }

    public static org.springframework.context.ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }
}
