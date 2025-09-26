package com.flowmaster.gateway.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 路由管理服务
 * 管理API网关的路由配置和状态
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Service
@Slf4j
public class RouteManagementService {

    private final RouteLocator routeLocator;

    public RouteManagementService(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    /**
     * 获取所有路由信息
     */
    public Flux<Route> getAllRoutes() {
        log.debug("获取所有路由信息");
        return routeLocator.getRoutes();
    }

    /**
     * 根据服务名获取路由
     */
    public Flux<Route> getRoutesByService(String serviceName) {
        log.debug("根据服务名获取路由: serviceName={}", serviceName);
        return routeLocator.getRoutes()
            .filter(route -> route.getId().contains(serviceName));
    }

    /**
     * 检查路由是否可用
     */
    public boolean isRouteAvailable(String routeId) {
        log.debug("检查路由是否可用: routeId={}", routeId);
        return routeLocator.getRoutes()
            .any(route -> route.getId().equals(routeId))
            .block();
    }

    /**
     * 获取路由统计信息
     */
    public RouteStatistics getRouteStatistics() {
        log.debug("获取路由统计信息");
        
        List<Route> routes = routeLocator.getRoutes()
            .collectList()
            .block();
        
        if (routes == null || routes.isEmpty()) {
            return new RouteStatistics(0, 0, 0, 0);
        }
        
        long totalRoutes = routes.size();
        long userServiceRoutes = routes.stream()
            .filter(route -> route.getId().contains("user"))
            .count();
        long authServiceRoutes = routes.stream()
            .filter(route -> route.getId().contains("auth"))
            .count();
        long workflowServiceRoutes = routes.stream()
            .filter(route -> route.getId().contains("workflow"))
            .count();
        
        return new RouteStatistics(totalRoutes, userServiceRoutes, authServiceRoutes, workflowServiceRoutes);
    }

    /**
     * 路由统计信息
     */
    public static class RouteStatistics {
        private final long totalRoutes;
        private final long userServiceRoutes;
        private final long authServiceRoutes;
        private final long workflowServiceRoutes;

        public RouteStatistics(long totalRoutes, long userServiceRoutes, long authServiceRoutes, long workflowServiceRoutes) {
            this.totalRoutes = totalRoutes;
            this.userServiceRoutes = userServiceRoutes;
            this.authServiceRoutes = authServiceRoutes;
            this.workflowServiceRoutes = workflowServiceRoutes;
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
    }
}
