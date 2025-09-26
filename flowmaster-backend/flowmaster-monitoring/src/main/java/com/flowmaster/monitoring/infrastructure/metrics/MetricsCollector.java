package com.flowmaster.monitoring.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 监控指标收集器
 * 收集和记录各种业务指标
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Component
@Slf4j
public class MetricsCollector {

    private final MeterRegistry meterRegistry;

    // 计数器
    private final Counter totalRequests;
    private final Counter successfulRequests;
    private final Counter failedRequests;
    private final Counter userRegistrations;
    private final Counter userLogins;
    private final Counter workflowInstances;
    private final Counter workflowTasks;

    // 计时器
    private final Timer requestDuration;
    private final Timer userServiceDuration;
    private final Timer authServiceDuration;
    private final Timer workflowServiceDuration;
    private final Timer databaseQueryDuration;
    private final Timer cacheAccessDuration;
    private final Timer messageQueueProcessingDuration;

    // 仪表盘
    private final AtomicLong activeUsers = new AtomicLong(0);
    private final AtomicLong activeSessions = new AtomicLong(0);
    private final AtomicLong activeWorkflows = new AtomicLong(0);

    public MetricsCollector(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // 初始化计数器
        this.totalRequests = Counter.builder("flowmaster.requests.total")
                .description("总请求数")
                .register(meterRegistry);

        this.successfulRequests = Counter.builder("flowmaster.requests.successful")
                .description("成功请求数")
                .register(meterRegistry);

        this.failedRequests = Counter.builder("flowmaster.requests.failed")
                .description("失败请求数")
                .register(meterRegistry);

        this.userRegistrations = Counter.builder("flowmaster.users.registrations")
                .description("用户注册数")
                .register(meterRegistry);

        this.userLogins = Counter.builder("flowmaster.users.logins")
                .description("用户登录数")
                .register(meterRegistry);

        this.workflowInstances = Counter.builder("flowmaster.workflow.instances")
                .description("工作流实例数")
                .register(meterRegistry);

        this.workflowTasks = Counter.builder("flowmaster.workflow.tasks")
                .description("工作流任务数")
                .register(meterRegistry);

        // 初始化计时器
        this.requestDuration = Timer.builder("flowmaster.request.duration")
                .description("请求处理时间")
                .register(meterRegistry);

        this.userServiceDuration = Timer.builder("flowmaster.service.user.duration")
                .description("用户服务处理时间")
                .register(meterRegistry);

        this.authServiceDuration = Timer.builder("flowmaster.service.auth.duration")
                .description("认证服务处理时间")
                .register(meterRegistry);

        this.workflowServiceDuration = Timer.builder("flowmaster.service.workflow.duration")
                .description("工作流服务处理时间")
                .register(meterRegistry);

        this.databaseQueryDuration = Timer.builder("flowmaster.database.query.duration")
                .description("数据库查询时间")
                .register(meterRegistry);

        this.cacheAccessDuration = Timer.builder("flowmaster.cache.access.duration")
                .description("缓存访问时间")
                .register(meterRegistry);

        this.messageQueueProcessingDuration = Timer.builder("flowmaster.message.queue.processing.duration")
                .description("消息队列处理时间")
                .register(meterRegistry);

        // 注册仪表盘指标
        Gauge.builder("flowmaster.users.active", activeUsers, AtomicLong::get)
                .description("活跃用户数")
                .register(meterRegistry);

        Gauge.builder("flowmaster.sessions.active", activeSessions, AtomicLong::get)
                .description("活跃会话数")
                .register(meterRegistry);

        Gauge.builder("flowmaster.workflows.active", activeWorkflows, AtomicLong::get)
                .description("活跃工作流数")
                .register(meterRegistry);
    }

    /**
     * 记录总请求数
     */
    public void incrementTotalRequests() {
        totalRequests.increment();
        log.debug("总请求数增加: {}", totalRequests.count());
    }

    /**
     * 记录成功请求数
     */
    public void incrementSuccessfulRequests() {
        successfulRequests.increment();
        log.debug("成功请求数增加: {}", successfulRequests.count());
    }

    /**
     * 记录失败请求数
     */
    public void incrementFailedRequests() {
        failedRequests.increment();
        log.debug("失败请求数增加: {}", failedRequests.count());
    }

    /**
     * 记录用户注册数
     */
    public void incrementUserRegistrations() {
        userRegistrations.increment();
        log.debug("用户注册数增加: {}", userRegistrations.count());
    }

    /**
     * 记录用户登录数
     */
    public void incrementUserLogins() {
        userLogins.increment();
        log.debug("用户登录数增加: {}", userLogins.count());
    }

    /**
     * 记录工作流实例数
     */
    public void incrementWorkflowInstances() {
        workflowInstances.increment();
        log.debug("工作流实例数增加: {}", workflowInstances.count());
    }

    /**
     * 记录工作流任务数
     */
    public void incrementWorkflowTasks() {
        workflowTasks.increment();
        log.debug("工作流任务数增加: {}", workflowTasks.count());
    }

    /**
     * 记录请求处理时间
     */
    public Timer.Sample startRequestTimer() {
        return Timer.start(meterRegistry);
    }

    /**
     * 停止请求计时器
     */
    public void stopRequestTimer(Timer.Sample sample) {
        sample.stop(requestDuration);
    }

    /**
     * 记录用户服务处理时间
     */
    public Timer.Sample startUserServiceTimer() {
        return Timer.start(meterRegistry);
    }

    /**
     * 停止用户服务计时器
     */
    public void stopUserServiceTimer(Timer.Sample sample) {
        sample.stop(userServiceDuration);
    }

    /**
     * 记录认证服务处理时间
     */
    public Timer.Sample startAuthServiceTimer() {
        return Timer.start(meterRegistry);
    }

    /**
     * 停止认证服务计时器
     */
    public void stopAuthServiceTimer(Timer.Sample sample) {
        sample.stop(authServiceDuration);
    }

    /**
     * 记录工作流服务处理时间
     */
    public Timer.Sample startWorkflowServiceTimer() {
        return Timer.start(meterRegistry);
    }

    /**
     * 停止工作流服务计时器
     */
    public void stopWorkflowServiceTimer(Timer.Sample sample) {
        sample.stop(workflowServiceDuration);
    }

    /**
     * 记录数据库查询时间
     */
    public Timer.Sample startDatabaseQueryTimer() {
        return Timer.start(meterRegistry);
    }

    /**
     * 停止数据库查询计时器
     */
    public void stopDatabaseQueryTimer(Timer.Sample sample) {
        sample.stop(databaseQueryDuration);
    }

    /**
     * 记录缓存访问时间
     */
    public Timer.Sample startCacheAccessTimer() {
        return Timer.start(meterRegistry);
    }

    /**
     * 停止缓存访问计时器
     */
    public void stopCacheAccessTimer(Timer.Sample sample) {
        sample.stop(cacheAccessDuration);
    }

    /**
     * 记录消息队列处理时间
     */
    public Timer.Sample startMessageQueueProcessingTimer() {
        return Timer.start(meterRegistry);
    }

    /**
     * 停止消息队列处理计时器
     */
    public void stopMessageQueueProcessingTimer(Timer.Sample sample) {
        sample.stop(messageQueueProcessingDuration);
    }

    /**
     * 设置活跃用户数
     */
    public void setActiveUsers(long count) {
        activeUsers.set(count);
        log.debug("活跃用户数设置为: {}", count);
    }

    /**
     * 设置活跃会话数
     */
    public void setActiveSessions(long count) {
        activeSessions.set(count);
        log.debug("活跃会话数设置为: {}", count);
    }

    /**
     * 设置活跃工作流数
     */
    public void setActiveWorkflows(long count) {
        activeWorkflows.set(count);
        log.debug("活跃工作流数设置为: {}", count);
    }

    /**
     * 获取所有注册的指标
     * 
     * @return 指标名称到值的映射
     */
    public Map<String, Number> getAllMetrics() {
        log.info("收集所有应用指标");
        Map<String, Number> metrics = new HashMap<>();
        meterRegistry.getMeters().forEach(meter -> {
            meter.measure().forEach(measurement -> {
                metrics.put(meter.getId().getName() + "." + measurement.getStatistic().getTagValueRepresentation(), measurement.getValue());
            });
        });
        return metrics;
    }
}