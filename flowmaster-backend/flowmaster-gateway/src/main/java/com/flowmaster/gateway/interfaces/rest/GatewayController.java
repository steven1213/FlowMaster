package com.flowmaster.gateway.interfaces.rest;

import com.flowmaster.common.response.Result;
import com.flowmaster.gateway.application.dto.GatewayStatusDTO;
import com.flowmaster.gateway.application.service.MonitoringStatisticsService;
import com.flowmaster.gateway.application.service.RateLimitStrategyService;
import com.flowmaster.gateway.application.service.RouteManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 网关健康检查控制器
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/gateway")
@Tag(name = "网关管理", description = "API网关相关API")
@RequiredArgsConstructor
@Slf4j
public class GatewayController {

    private final RouteManagementService routeManagementService;
    private final RateLimitStrategyService rateLimitStrategyService;
    private final MonitoringStatisticsService monitoringStatisticsService;

    /**
     * 网关健康检查
     */
    @GetMapping("/health")
    @Operation(summary = "网关健康检查", description = "检查API网关服务状态")
    public Result<Map<String, Object>> health() {
        log.debug("网关健康检查请求");
        
        Map<String, Object> healthInfo = new HashMap<>();
        healthInfo.put("status", "UP");
        healthInfo.put("service", "flowmaster-gateway");
        healthInfo.put("timestamp", java.time.LocalDateTime.now());
        healthInfo.put("version", "1.0.0");
        
        return Result.success(healthInfo);
    }

    /**
     * 网关信息
     */
    @GetMapping("/info")
    @Operation(summary = "网关信息", description = "获取API网关服务信息")
    public Result<Map<String, Object>> info() {
        log.debug("网关信息请求");
        
        Map<String, Object> info = new HashMap<>();
        info.put("service", "FlowMaster API Gateway");
        info.put("version", "1.0.0");
        info.put("description", "FlowMaster API Gateway Service");
        info.put("buildTime", java.time.LocalDateTime.now());
        info.put("features", new String[]{
            "路由转发",
            "负载均衡", 
            "认证授权",
            "限流控制",
            "监控统计"
        });
        
        return Result.success(info);
    }

    /**
     * 获取路由统计信息
     */
    @GetMapping("/routes/statistics")
    @Operation(summary = "路由统计信息", description = "获取网关路由统计信息")
    public Result<RouteManagementService.RouteStatistics> getRouteStatistics() {
        log.debug("获取路由统计信息请求");
        
        RouteManagementService.RouteStatistics statistics = routeManagementService.getRouteStatistics();
        return Result.success(statistics);
    }

    /**
     * 获取限流统计信息
     */
    @GetMapping("/rate-limit/statistics")
    @Operation(summary = "限流统计信息", description = "获取网关限流统计信息")
    public Result<RateLimitStrategyService.RateLimitStatistics> getRateLimitStatistics() {
        log.debug("获取限流统计信息请求");
        
        return Result.success(rateLimitStrategyService.getRateLimitStatistics());
    }

    /**
     * 获取监控概览
     */
    @GetMapping("/monitoring/overview")
    @Operation(summary = "监控概览", description = "获取网关监控概览")
    public Mono<Result<MonitoringStatisticsService.GatewayMonitoringOverview>> getMonitoringOverview() {
        log.debug("获取监控概览请求");
        
        return monitoringStatisticsService.getGatewayMonitoringOverview()
            .map(Result::success);
    }

    /**
     * 获取服务健康状态
     */
    @GetMapping("/services/health")
    @Operation(summary = "服务健康状态", description = "获取后端服务健康状态")
    public Mono<Result<MonitoringStatisticsService.ServiceHealthStatus>> getServiceHealthStatus() {
        log.debug("获取服务健康状态请求");
        
        return monitoringStatisticsService.getServiceHealthStatus()
            .map(Result::success);
    }

    /**
     * 重置限流计数器
     */
    @PostMapping("/rate-limit/reset")
    @Operation(summary = "重置限流计数器", description = "重置所有限流计数器")
    public Result<Void> resetRateLimitCounters() {
        log.info("重置限流计数器请求");
        
        rateLimitStrategyService.resetRateLimitCounters();
        return Result.success();
    }

    /**
     * 重置指定路径的限流计数器
     */
    @PostMapping("/rate-limit/reset/{path}")
    @Operation(summary = "重置指定路径限流计数器", description = "重置指定路径的限流计数器")
    public Result<Void> resetRateLimitCounter(@PathVariable String path) {
        log.info("重置指定路径限流计数器请求: path={}", path);
        
        rateLimitStrategyService.resetRateLimitCounter(path);
        return Result.success();
    }
}
