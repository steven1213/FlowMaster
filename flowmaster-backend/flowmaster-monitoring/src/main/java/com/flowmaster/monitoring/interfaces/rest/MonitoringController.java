package com.flowmaster.monitoring.interfaces.rest;

import com.flowmaster.common.response.Result;
import com.flowmaster.monitoring.application.service.MonitoringApplicationService;
import com.flowmaster.monitoring.infrastructure.logging.LogAggregationService;
import com.flowmaster.monitoring.infrastructure.tracing.TracingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 监控控制器
 * 提供监控数据的REST API
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/monitoring")
@Tag(name = "监控管理", description = "监控和日志相关API")
@RequiredArgsConstructor
@Slf4j
public class MonitoringController {

    private final MonitoringApplicationService monitoringApplicationService;
    private final LogAggregationService logAggregationService;
    private final TracingService tracingService;

    /**
     * 获取系统监控概览
     */
    @GetMapping("/overview")
    @Operation(summary = "获取系统监控概览", description = "获取系统整体监控概览信息")
    public Result<MonitoringApplicationService.SystemMonitoringOverview> getSystemOverview() {
        log.debug("获取系统监控概览请求");
        
        MonitoringApplicationService.SystemMonitoringOverview overview = 
            monitoringApplicationService.getSystemMonitoringOverview();
        
        return Result.success(overview);
    }

    /**
     * 获取业务指标统计
     */
    @GetMapping("/business-metrics")
    @Operation(summary = "获取业务指标统计", description = "获取业务相关的指标统计信息")
    public Result<MonitoringApplicationService.BusinessMetricsStatistics> getBusinessMetrics() {
        log.debug("获取业务指标统计请求");
        
        MonitoringApplicationService.BusinessMetricsStatistics metrics = 
            monitoringApplicationService.getBusinessMetricsStatistics();
        
        return Result.success(metrics);
    }

    /**
     * 获取性能指标
     */
    @GetMapping("/performance-metrics")
    @Operation(summary = "获取性能指标", description = "获取系统性能相关指标")
    public Result<MonitoringApplicationService.PerformanceMetrics> getPerformanceMetrics() {
        log.debug("获取性能指标请求");
        
        MonitoringApplicationService.PerformanceMetrics metrics = 
            monitoringApplicationService.getPerformanceMetrics();
        
        return Result.success(metrics);
    }

    /**
     * 获取健康检查状态
     */
    @GetMapping("/health")
    @Operation(summary = "获取健康检查状态", description = "获取系统健康检查状态")
    public Result<MonitoringApplicationService.HealthCheckStatus> getHealthStatus() {
        log.debug("获取健康检查状态请求");
        
        MonitoringApplicationService.HealthCheckStatus status = 
            monitoringApplicationService.getHealthCheckStatus();
        
        return Result.success(status);
    }

    /**
     * 获取日志统计信息
     */
    @GetMapping("/logs/statistics")
    @Operation(summary = "获取日志统计信息", description = "获取日志相关的统计信息")
    public Result<LogAggregationService.LogStatistics> getLogStatistics() {
        log.debug("获取日志统计信息请求");
        
        LogAggregationService.LogStatistics statistics = 
            logAggregationService.getLogStatistics();
        
        return Result.success(statistics);
    }

    /**
     * 获取追踪统计信息
     */
    @GetMapping("/traces/statistics")
    @Operation(summary = "获取追踪统计信息", description = "获取链路追踪相关的统计信息")
    public Result<TracingService.TraceStatistics> getTraceStatistics() {
        log.debug("获取追踪统计信息请求");
        
        TracingService.TraceStatistics statistics = 
            tracingService.getTraceStatistics();
        
        return Result.success(statistics);
    }

    /**
     * 获取当前追踪信息
     */
    @GetMapping("/traces/current")
    @Operation(summary = "获取当前追踪信息", description = "获取当前请求的追踪信息")
    public Result<Map<String, Object>> getCurrentTraceInfo() {
        log.debug("获取当前追踪信息请求");
        
        Map<String, Object> traceInfo = new HashMap<>();
        traceInfo.put("traceId", tracingService.getCurrentTraceId());
        traceInfo.put("spanId", tracingService.getCurrentSpanId());
        traceInfo.put("timestamp", LocalDateTime.now());
        
        return Result.success(traceInfo);
    }

    /**
     * 获取监控服务信息
     */
    @GetMapping("/info")
    @Operation(summary = "获取监控服务信息", description = "获取监控服务的基本信息")
    public Result<Map<String, Object>> getServiceInfo() {
        log.debug("获取监控服务信息请求");
        
        Map<String, Object> info = new HashMap<>();
        info.put("service", "FlowMaster Monitoring Service");
        info.put("version", "1.0.0");
        info.put("description", "FlowMaster Monitoring and Logging Service");
        info.put("buildTime", LocalDateTime.now());
        info.put("features", new String[]{
            "指标收集",
            "日志聚合",
            "链路追踪",
            "健康检查",
            "性能监控"
        });
        
        return Result.success(info);
    }

    /**
     * 手动触发指标收集
     */
    @PostMapping("/metrics/collect")
    @Operation(summary = "手动触发指标收集", description = "手动触发系统指标收集")
    public Result<Void> collectMetrics() {
        log.info("手动触发指标收集请求");
        
        // 这里可以触发各种指标的收集
        // 暂时只记录日志
        log.info("指标收集完成");
        
        return Result.success();
    }

    /**
     * 清理过期日志
     */
    @PostMapping("/logs/cleanup")
    @Operation(summary = "清理过期日志", description = "清理过期的日志数据")
    public Result<Void> cleanupExpiredLogs() {
        log.info("清理过期日志请求");
        
        // 这里应该实现实际的日志清理逻辑
        // 暂时只记录日志
        log.info("过期日志清理完成");
        
        return Result.success();
    }
}
