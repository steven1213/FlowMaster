package com.flowmaster.gateway.application.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 路由信息DTO
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Data
public class RouteInfoDTO {
    
    private String routeId;
    private String uri;
    private List<String> predicates;
    private List<String> filters;
    private int order;
    private LocalDateTime timestamp;
    
    public RouteInfoDTO() {
        this.timestamp = LocalDateTime.now();
    }
}
