package com.flowmaster.monitoring.infrastructure.tracing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 链路追踪服务
 * 处理分布式链路追踪
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TracingService {

    private final Tracer tracer;

    /**
     * 开始新的追踪
     */
    public TraceContext startTrace(String operationName) {
        log.debug("开始新的追踪: operationName={}", operationName);
        
        Span span = tracer.nextSpan()
                .name(operationName)
                .start();
        
        return new TraceContext(span, LocalDateTime.now());
    }

    /**
     * 开始子追踪
     */
    public TraceContext startChildTrace(String operationName) {
        log.debug("开始子追踪: operationName={}", operationName);
        
        Span span = tracer.nextSpan()
                .name(operationName)
                .start();
        
        return new TraceContext(span, LocalDateTime.now());
    }

    /**
     * 添加追踪标签
     */
    public void addTag(String key, String value) {
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            currentSpan.tag(key, value);
            log.debug("添加追踪标签: {}={}", key, value);
        }
    }

    /**
     * 添加追踪事件
     */
    public void addEvent(String eventName) {
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            currentSpan.event(eventName);
            log.debug("添加追踪事件: {}", eventName);
        }
    }

    /**
     * 完成追踪
     */
    public void finishTrace(TraceContext context) {
        if (context != null && context.getSpan() != null) {
            context.getSpan().end();
            log.debug("完成追踪: operationName={}, duration={}ms", 
                     "trace-operation", 
                     java.time.Duration.between(context.getStartTime(), LocalDateTime.now()).toMillis());
        }
    }

    /**
     * 记录错误
     */
    public void recordError(Throwable error) {
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            currentSpan.tag("error", "true");
            currentSpan.tag("error.message", error.getMessage());
            currentSpan.tag("error.class", error.getClass().getSimpleName());
            log.debug("记录追踪错误: {}", error.getMessage());
        }
    }

    /**
     * 获取当前追踪ID
     */
    public String getCurrentTraceId() {
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            return currentSpan.context().traceId();
        }
        return null;
    }

    /**
     * 获取当前Span ID
     */
    public String getCurrentSpanId() {
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            return currentSpan.context().spanId();
        }
        return null;
    }

    /**
     * 获取追踪统计信息
     */
    public TraceStatistics getTraceStatistics() {
        log.debug("获取追踪统计信息");
        
        // 这里应该从Zipkin或其他追踪系统获取统计数据
        // 暂时返回模拟数据
        return new TraceStatistics(
                1000L,    // 总追踪数
                50L,      // 错误追踪数
                950L,     // 成功追踪数
                100L,     // 平均追踪时间(ms)
                LocalDateTime.now()
        );
    }

    /**
     * 追踪上下文
     */
    public static class TraceContext {
        private final Span span;
        private final LocalDateTime startTime;

        public TraceContext(Span span, LocalDateTime startTime) {
            this.span = span;
            this.startTime = startTime;
        }

        public Span getSpan() {
            return span;
        }

        public LocalDateTime getStartTime() {
            return startTime;
        }
    }

    /**
     * 追踪统计信息
     */
    public static class TraceStatistics {
        private final long totalTraces;
        private final long errorTraces;
        private final long successfulTraces;
        private final long averageDuration;
        private final LocalDateTime timestamp;

        public TraceStatistics(long totalTraces, long errorTraces, long successfulTraces,
                             long averageDuration, LocalDateTime timestamp) {
            this.totalTraces = totalTraces;
            this.errorTraces = errorTraces;
            this.successfulTraces = successfulTraces;
            this.averageDuration = averageDuration;
            this.timestamp = timestamp;
        }

        // Getters
        public long getTotalTraces() { return totalTraces; }
        public long getErrorTraces() { return errorTraces; }
        public long getSuccessfulTraces() { return successfulTraces; }
        public long getAverageDuration() { return averageDuration; }
        public LocalDateTime getTimestamp() { return timestamp; }

        public double getErrorRate() {
            return totalTraces > 0 ? (double) errorTraces / totalTraces * 100 : 0;
        }

        public double getSuccessRate() {
            return totalTraces > 0 ? (double) successfulTraces / totalTraces * 100 : 0;
        }
    }
}
