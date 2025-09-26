package com.flowmaster.registry.interfaces.rest;

import com.flowmaster.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务注册与发现控制器
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/registry")
@Tag(name = "服务注册与发现", description = "服务注册与发现相关API")
@Slf4j
public class RegistryController {

    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * 获取所有服务列表
     */
    @GetMapping("/services")
    @Operation(summary = "获取所有服务列表", description = "获取注册中心中的所有服务")
    public Result<List<String>> getAllServices() {
        log.debug("获取所有服务列表请求");
        
        List<String> services = discoveryClient.getServices();
        log.debug("发现服务数量: {}", services.size());
        
        return Result.success(services);
    }

    /**
     * 获取指定服务的实例列表
     */
    @GetMapping("/services/{serviceName}/instances")
    @Operation(summary = "获取服务实例列表", description = "获取指定服务的所有实例")
    public Result<List<ServiceInstance>> getServiceInstances(@PathVariable String serviceName) {
        log.debug("获取服务实例列表请求: serviceName={}", serviceName);
        
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        log.debug("服务 {} 的实例数量: {}", serviceName, instances.size());
        
        return Result.success(instances);
    }

    /**
     * 获取服务注册中心状态
     */
    @GetMapping("/status")
    @Operation(summary = "获取注册中心状态", description = "获取服务注册中心的运行状态")
    public Result<Map<String, Object>> getRegistryStatus() {
        log.debug("获取注册中心状态请求");
        
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "flowmaster-registry");
        status.put("timestamp", LocalDateTime.now());
        status.put("version", "1.0.0");
        status.put("registeredServices", discoveryClient.getServices().size());
        
        return Result.success(status);
    }

    /**
     * 获取服务健康检查信息
     */
    @GetMapping("/health")
    @Operation(summary = "服务健康检查", description = "检查服务注册与发现服务的健康状态")
    public Result<Map<String, Object>> health() {
        log.debug("服务健康检查请求");
        
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "flowmaster-registry");
        health.put("timestamp", LocalDateTime.now());
        health.put("discoveryClient", discoveryClient.getClass().getSimpleName());
        
        return Result.success(health);
    }

    /**
     * 获取服务统计信息
     */
    @GetMapping("/statistics")
    @Operation(summary = "获取服务统计信息", description = "获取服务注册与发现的统计信息")
    public Result<Map<String, Object>> getStatistics() {
        log.debug("获取服务统计信息请求");
        
        List<String> services = discoveryClient.getServices();
        Map<String, Object> statistics = new HashMap<>();
        
        statistics.put("totalServices", services.size());
        statistics.put("services", services);
        
        Map<String, Integer> instanceCounts = new HashMap<>();
        for (String service : services) {
            List<ServiceInstance> instances = discoveryClient.getInstances(service);
            instanceCounts.put(service, instances.size());
        }
        statistics.put("instanceCounts", instanceCounts);
        statistics.put("timestamp", LocalDateTime.now());
        
        return Result.success(statistics);
    }
}
