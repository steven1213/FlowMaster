package com.flowmaster.common.response;

import org.slf4j.MDC;

/**
 * 请求追踪上下文
 * 
 * @author FlowMaster Team
 * @version 1.0
 * @since 2024-01-01
 */
public class TraceContext {
    
    private static final String TRACE_ID_KEY = "traceId";
    
    /**
     * 获取追踪ID
     * 
     * @return 追踪ID
     */
    public static String getTraceId() {
        String traceId = MDC.get(TRACE_ID_KEY);
        if (traceId == null) {
            traceId = generateTraceId();
            MDC.put(TRACE_ID_KEY, traceId);
        }
        return traceId;
    }
    
    /**
     * 设置追踪ID
     * 
     * @param traceId 追踪ID
     */
    public static void setTraceId(String traceId) {
        MDC.put(TRACE_ID_KEY, traceId);
    }
    
    /**
     * 清除追踪ID
     */
    public static void clearTraceId() {
        MDC.remove(TRACE_ID_KEY);
    }
    
    /**
     * 生成追踪ID
     * 
     * @return 追踪ID
     */
    private static String generateTraceId() {
        return System.currentTimeMillis() + "-" + Thread.currentThread().getId();
    }
}
