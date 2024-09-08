package com.gwm.marketing.filter;

import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;

import java.awt.*;
import java.util.UUID;

/**
 * @Author:hongtaofan
 * @Version:1.0
 * @Description:
 * @Date: 2023/4/19 13:37
 */
public class TraceUtils {
    private static final String TRACE_ID = "traceId";

    public static String createTraceId() {
        String traceId = MDC.get(TRACE_ID);
        if (StringUtils.isBlank(traceId)) {
            traceId = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
            MDC.put(TRACE_ID, traceId);
        }
        return traceId;
    }

    public static void destroyTraceId() {
        MDC.remove(TRACE_ID);
        MDC.clear();
    }


}
