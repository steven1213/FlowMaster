package com.flowmaster.monitoring.application.service;

import com.flowmaster.monitoring.infrastructure.metrics.MetricsCollector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 监控应用服务
 * 提供监控数据的业务逻辑处理
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MonitoringApplicationService {

    private final MetricsCollector metricsCollector;

    /**
     * 获取系统监控概览
     */
    public SystemMonitoringOverview getSystemMonitoringOverview() {
        log.debug("获取系统监控概览");
        
        Map<String, Number> metrics = metricsCollector.getAllMetrics();
        
        return new SystemMonitoringOverview(
                metrics.getOrDefault("flowmaster.requests.total.COUNT", 0).longValue(),
                metrics.getOrDefault("flowmaster.requests.successful.COUNT", 0).longValue(),
                metrics.getOrDefault("flowmaster.requests.failed.COUNT", 0).longValue(),
                calculateSuccessRate(metrics),
                calculateFailureRate(metrics),
                metrics.getOrDefault("flowmaster.users.active.VALUE", 0).longValue(),
                metrics.getOrDefault("flowmaster.sessions.active.VALUE", 0).longValue(),
                metrics.getOrDefault("flowmaster.workflows.active.VALUE", 0).longValue(),
                LocalDateTime.now()
        );
    }

    /**
     * 获取业务指标统计
     */
    public BusinessMetricsStatistics getBusinessMetricsStatistics() {
        log.debug("获取业务指标统计");
        
        Map<String, Number> metrics = metricsCollector.getAllMetrics();
        
        return new BusinessMetricsStatistics(
                metrics.getOrDefault("flowmaster.users.registrations.COUNT", 0).longValue(),
                metrics.getOrDefault("flowmaster.users.logins.COUNT", 0).longValue(),
                metrics.getOrDefault("flowmaster.workflow.instances.COUNT", 0).longValue(),
                metrics.getOrDefault("flowmaster.workflow.tasks.COUNT", 0).longValue(),
                LocalDateTime.now()
        );
    }

    /**
     * 计算成功率
     */
    private double calculateSuccessRate(Map<String, Number> metrics) {
        long total = metrics.getOrDefault("flowmaster.requests.total.COUNT", 0).longValue();
        long successful = metrics.getOrDefault("flowmaster.requests.successful.COUNT", 0).longValue();
        return total > 0 ? (double) successful / total * 100 : 0;
    }

    /**
     * 计算失败率
     */
    private double calculateFailureRate(Map<String, Number> metrics) {
        long total = metrics.getOrDefault("flowmaster.requests.total.COUNT", 0).longValue();
        long failed = metrics.getOrDefault("flowmaster.requests.failed.COUNT", 0).longValue();
        return total > 0 ? (double) failed / total * 100 : 0;
    }

    /**
     * 获取性能指标
     */
    public PerformanceMetrics getPerformanceMetrics() {
        log.debug("获取性能指标");
        
        // 这里可以从Micrometer获取更详细的性能指标
        // 暂时返回模拟数据
        return new PerformanceMetrics(
                1000L,    // 平均响应时间(ms)
                99.9,     // 可用性(%)
                50L,      // 错误数
                10000L,   // 吞吐量(requests/min)
                LocalDateTime.now()
        );
    }

    /**
     * 获取健康检查状态
     */
    public HealthCheckStatus getHealthCheckStatus() {
        log.debug("获取健康检查状态");
        
        return new HealthCheckStatus(
                "UP",
                "FlowMaster Monitoring Service",
                "1.0.0",
                LocalDateTime.now(),
                Map.of(
                        "database", "UP",
                        "redis", "UP",
                        "elasticsearch", "UP"
                )
        );
    }

    /**
     * 系统监控概览
     */
    public static class SystemMonitoringOverview {
        private final double totalRequests;
        private final double successfulRequests;
        private final double failedRequests;
        private final double successRate;
        private final double failureRate;
        private final long activeUsers;
        private final long activeSessions;
        private final long activeWorkflows;
        private final LocalDateTime timestamp;

        public SystemMonitoringOverview(double totalRequests, double successfulRequests, double failedRequests,
                                      double successRate, double failureRate, long activeUsers,
                                      long activeSessions, long activeWorkflows, LocalDateTime timestamp) {
            this.totalRequests = totalRequests;
            this.successfulRequests = successfulRequests;
            this.failedRequests = failedRequests;
            this.successRate = successRate;
            this.failureRate = failureRate;
            this.activeUsers = activeUsers;
            this.activeSessions = activeSessions;
            this.activeWorkflows = activeWorkflows;
            this.timestamp = timestamp;
        }

        // Getters
        public double getTotalRequests() { return totalRequests; }
        public double getSuccessfulRequests() { return successfulRequests; }
        public double getFailedRequests() { return failedRequests; }
        public double getSuccessRate() { return successRate; }
        public double getFailureRate() { return failureRate; }
        public long getActiveUsers() { return activeUsers; }
        public long getActiveSessions() { return activeSessions; }
        public long getActiveWorkflows() { return activeWorkflows; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }

    /**
     * 业务指标统计
     */
    public static class BusinessMetricsStatistics {
        private final double userRegistrations;
        private final double userLogins;
        private final double workflowInstances;
        private final double workflowTasks;
        private final LocalDateTime timestamp;

        public BusinessMetricsStatistics(double userRegistrations, double userLogins,
                                       double workflowInstances, double workflowTasks, LocalDateTime timestamp) {
            this.userRegistrations = userRegistrations;
            this.userLogins = userLogins;
            this.workflowInstances = workflowInstances;
            this.workflowTasks = workflowTasks;
            this.timestamp = timestamp;
        }

        // Getters
        public double getUserRegistrations() { return userRegistrations; }
        public double getUserLogins() { return userLogins; }
        public double getWorkflowInstances() { return workflowInstances; }
        public double getWorkflowTasks() { return workflowTasks; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }

    /**
     * 性能指标
     */
    public static class PerformanceMetrics {
        private final long averageResponseTime;
        private final double availability;
        private final long errorCount;
        private final long throughput;
        private final LocalDateTime timestamp;

        public PerformanceMetrics(long averageResponseTime, double availability,
                                long errorCount, long throughput, LocalDateTime timestamp) {
            this.averageResponseTime = averageResponseTime;
            this.availability = availability;
            this.errorCount = errorCount;
            this.throughput = throughput;
            this.timestamp = timestamp;
        }

        // Getters
        public long getAverageResponseTime() { return averageResponseTime; }
        public double getAvailability() { return availability; }
        public long getErrorCount() { return errorCount; }
        public long getThroughput() { return throughput; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }

    /**
     * 健康检查状态
     */
    public static class HealthCheckStatus {
        private final String status;
        private final String service;
        private final String version;
        private final LocalDateTime timestamp;
        private final Map<String, String> components;

        public HealthCheckStatus(String status, String service, String version,
                               LocalDateTime timestamp, Map<String, String> components) {
            this.status = status;
            this.service = service;
            this.version = version;
            this.timestamp = timestamp;
            this.components = components;
        }

        // Getters
        public String getStatus() { return status; }
        public String getService() { return service; }
        public String getVersion() { return version; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public Map<String, String> getComponents() { return components; }
    }
}
