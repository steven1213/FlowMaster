package com.flowmaster.gateway.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限流策略服务
 * 管理API网关的限流策略和统计
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Service
@Slf4j
public class RateLimitStrategyService {

    // 使用内存存储替代Redis
    private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> lastResetTimes = new ConcurrentHashMap<>();

    @Value("${rate-limit.user-rate:10}")
    private int userRate;

    @Value("${rate-limit.user-burst:20}")
    private int userBurst;

    @Value("${rate-limit.auth-rate:5}")
    private int authRate;

    @Value("${rate-limit.auth-burst:10}")
    private int authBurst;

    @Value("${rate-limit.workflow-rate:20}")
    private int workflowRate;

    @Value("${rate-limit.workflow-burst:40}")
    private int workflowBurst;

    @Value("${rate-limit.default-rate:100}")
    private int defaultRate;

    @Value("${rate-limit.default-burst:200}")
    private int defaultBurst;

    public RateLimitStrategyService() {
        log.info("初始化限流策略服务（使用内存存储）");
    }

    /**
     * 获取限流策略配置
     */
    public RateLimitConfig getRateLimitConfig(String path) {
        log.debug("获取限流策略配置: path={}", path);
        
        if (path.startsWith("/auth/")) {
            return new RateLimitConfig(authRate, authBurst, "认证服务");
        } else if (path.startsWith("/user/")) {
            return new RateLimitConfig(userRate, userBurst, "用户服务");
        } else if (path.startsWith("/workflow/")) {
            return new RateLimitConfig(workflowRate, workflowBurst, "工作流服务");
        } else {
            return new RateLimitConfig(defaultRate, defaultBurst, "默认策略");
        }
    }

    /**
     * 获取限流统计信息
     */
    public RateLimitStatistics getRateLimitStatistics() {
        log.debug("获取限流统计信息");
        
        // 使用内存存储的简化实现
        int authCount = getMemoryCount("auth");
        int userCount = getMemoryCount("user");
        int workflowCount = getMemoryCount("workflow");
        int totalCount = authCount + userCount + workflowCount;
        
        return new RateLimitStatistics(authCount, userCount, workflowCount, totalCount);
    }

    /**
     * 重置限流计数器
     */
    public void resetRateLimitCounters() {
        log.info("重置限流计数器");
        requestCounts.clear();
        lastResetTimes.clear();
    }

    /**
     * 检查是否超过限流
     */
    public boolean isRateLimited(String key, RateLimitConfig config) {
        long currentTime = System.currentTimeMillis();
        String fullKey = "rate_limit:" + key;
        
        // 检查是否需要重置计数器
        Long lastReset = lastResetTimes.get(fullKey);
        if (lastReset == null || currentTime - lastReset > 60000) { // 1分钟重置
            requestCounts.put(fullKey, new AtomicInteger(0));
            lastResetTimes.put(fullKey, currentTime);
        }
        
        AtomicInteger count = requestCounts.get(fullKey);
        int currentCount = count.incrementAndGet();
        
        return currentCount > config.getBurst();
    }

    /**
     * 获取内存中的计数
     */
    private int getMemoryCount(String service) {
        AtomicInteger count = requestCounts.get("rate_limit:" + service);
        return count != null ? count.get() : 0;
    }

    /**
     * 重置指定路径的限流计数器
     */
    public void resetRateLimitCounter(String path) {
        log.info("重置指定路径的限流计数器: path={}", path);
        requestCounts.remove("rate_limit:" + path);
        lastResetTimes.remove("rate_limit:" + path);
    }

    /**
     * 限流配置
     */
    public static class RateLimitConfig {
        private final int rate;
        private final int burst;
        private final String description;

        public RateLimitConfig(int rate, int burst, String description) {
            this.rate = rate;
            this.burst = burst;
            this.description = description;
        }

        public int getRate() {
            return rate;
        }

        public int getBurst() {
            return burst;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 限流统计信息
     */
    public static class RateLimitStatistics {
        private final long authCounters;
        private final long userCounters;
        private final long workflowCounters;
        private final long totalCounters;

        public RateLimitStatistics(long authCounters, long userCounters, long workflowCounters, long totalCounters) {
            this.authCounters = authCounters;
            this.userCounters = userCounters;
            this.workflowCounters = workflowCounters;
            this.totalCounters = totalCounters;
        }

        public long getAuthCounters() {
            return authCounters;
        }

        public long getUserCounters() {
            return userCounters;
        }

        public long getWorkflowCounters() {
            return workflowCounters;
        }

        public long getTotalCounters() {
            return totalCounters;
        }
    }
}
