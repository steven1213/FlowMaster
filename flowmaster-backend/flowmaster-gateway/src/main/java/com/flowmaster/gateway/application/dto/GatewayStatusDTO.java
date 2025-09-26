package com.flowmaster.gateway.application.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 网关状态DTO
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Data
public class GatewayStatusDTO {
    
    private String serviceName;
    private String version;
    private String status;
    private LocalDateTime timestamp;
    private Map<String, Object> details;
    
    public GatewayStatusDTO() {
        this.serviceName = "FlowMaster API Gateway";
        this.version = "1.0.0";
        this.status = "UP";
        this.timestamp = LocalDateTime.now();
    }
}
