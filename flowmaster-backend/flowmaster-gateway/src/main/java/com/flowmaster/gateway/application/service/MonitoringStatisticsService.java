package com.flowmaster.gateway.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 监控统计服务
 * 收集和管理API网关的监控数据
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Service
@Slf4j
public class MonitoringStatisticsService {

    private final RouteManagementService routeManagementService;
    private final RateLimitStrategyService rateLimitStrategyService;

    @Autowired
    public MonitoringStatisticsService(RouteManagementService routeManagementService,
                                     RateLimitStrategyService rateLimitStrategyService) {
        this.routeManagementService = routeManagementService;
        this.rateLimitStrategyService = rateLimitStrategyService;
    }

    /**
     * 获取网关监控概览
     */
    public Mono<GatewayMonitoringOverview> getGatewayMonitoringOverview() {
        log.debug("获取网关监控概览");
        
        return Mono.zip(
            Mono.just(routeManagementService.getRouteStatistics()),
            Mono.just(rateLimitStrategyService.getRateLimitStatistics())
        ).map(tuple -> {
            RouteManagementService.RouteStatistics routeStats = tuple.getT1();
            RateLimitStrategyService.RateLimitStatistics rateLimitStats = tuple.getT2();
            
            return new GatewayMonitoringOverview(
                routeStats.getTotalRoutes(),
                routeStats.getUserServiceRoutes(),
                routeStats.getAuthServiceRoutes(),
                routeStats.getWorkflowServiceRoutes(),
                rateLimitStats.getTotalCounters(),
                LocalDateTime.now()
            );
        });
    }

    /**
     * 获取服务健康状态
     */
    public Mono<ServiceHealthStatus> getServiceHealthStatus() {
        log.debug("获取服务健康状态");
        
        return Mono.zip(
            Mono.just(routeManagementService.isRouteAvailable("user-service")),
            Mono.just(routeManagementService.isRouteAvailable("auth-service")),
            Mono.just(routeManagementService.isRouteAvailable("workflow-service"))
        ).map(tuple -> new ServiceHealthStatus(
            tuple.getT1(), // user service
            tuple.getT2(), // auth service
            tuple.getT3(), // workflow service
            LocalDateTime.now()
        ));
    }

    /**
     * 获取网关性能指标
     */
    public Mono<GatewayPerformanceMetrics> getGatewayPerformanceMetrics() {
        log.debug("获取网关性能指标");
        
        // 这里可以集成Micrometer等监控工具
        // 暂时返回模拟数据
        return Mono.just(new GatewayPerformanceMetrics(
            1000L, // 总请求数
            50L,   // 平均响应时间(ms)
            99.9,  // 可用性(%)
            10L,   // 错误数
            LocalDateTime.now()
        ));
    }

    /**
     * 网关监控概览
     */
    public static class GatewayMonitoringOverview {
        private final long totalRoutes;
        private final long userServiceRoutes;
        private final long authServiceRoutes;
        private final long workflowServiceRoutes;
        private final long activeRateLimitCounters;
        private final LocalDateTime timestamp;

        public GatewayMonitoringOverview(long totalRoutes, long userServiceRoutes, long authServiceRoutes,
                                       long workflowServiceRoutes, long activeRateLimitCounters, LocalDateTime timestamp) {
            this.totalRoutes = totalRoutes;
            this.userServiceRoutes = userServiceRoutes;
            this.authServiceRoutes = authServiceRoutes;
            this.workflowServiceRoutes = workflowServiceRoutes;
            this.activeRateLimitCounters = activeRateLimitCounters;
            this.timestamp = timestamp;
        }

        public long getTotalRoutes() {
            return totalRoutes;
        }

        public long getUserServiceRoutes() {
            return userServiceRoutes;
        }

        public long getAuthServiceRoutes() {
            return authServiceRoutes;
        }

        public long getWorkflowServiceRoutes() {
            return workflowServiceRoutes;
        }

        public long getActiveRateLimitCounters() {
            return activeRateLimitCounters;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }

    /**
     * 服务健康状态
     */
    public static class ServiceHealthStatus {
        private final boolean userServiceHealthy;
        private final boolean authServiceHealthy;
        private final boolean workflowServiceHealthy;
        private final LocalDateTime timestamp;

        public ServiceHealthStatus(boolean userServiceHealthy, boolean authServiceHealthy,
                                 boolean workflowServiceHealthy, LocalDateTime timestamp) {
            this.userServiceHealthy = userServiceHealthy;
            this.authServiceHealthy = authServiceHealthy;
            this.workflowServiceHealthy = workflowServiceHealthy;
            this.timestamp = timestamp;
        }

        public boolean isUserServiceHealthy() {
            return userServiceHealthy;
        }

        public boolean isAuthServiceHealthy() {
            return authServiceHealthy;
        }

        public boolean isWorkflowServiceHealthy() {
            return workflowServiceHealthy;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public boolean isAllServicesHealthy() {
            return userServiceHealthy && authServiceHealthy && workflowServiceHealthy;
        }
    }

    /**
     * 网关性能指标
     */
    public static class GatewayPerformanceMetrics {
        private final long totalRequests;
        private final long averageResponseTime;
        private final double availability;
        private final long errorCount;
        private final LocalDateTime timestamp;

        public GatewayPerformanceMetrics(long totalRequests, long averageResponseTime,
                                       double availability, long errorCount, LocalDateTime timestamp) {
            this.totalRequests = totalRequests;
            this.averageResponseTime = averageResponseTime;
            this.availability = availability;
            this.errorCount = errorCount;
            this.timestamp = timestamp;
        }

        public long getTotalRequests() {
            return totalRequests;
        }

        public long getAverageResponseTime() {
            return averageResponseTime;
        }

        public double getAvailability() {
            return availability;
        }

        public long getErrorCount() {
            return errorCount;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }
}
