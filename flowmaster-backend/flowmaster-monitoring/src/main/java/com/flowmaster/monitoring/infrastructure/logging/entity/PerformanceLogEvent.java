package com.flowmaster.monitoring.infrastructure.logging.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 性能日志事件
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Data
@Document(indexName = "flowmaster-performance-logs")
public class PerformanceLogEvent {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String serviceName;

    @Field(type = FieldType.Keyword)
    private String operation;

    @Field(type = FieldType.Long)
    private Long duration;

    @Field(type = FieldType.Long)
    private Long memoryUsed;

    @Field(type = FieldType.Long)
    private Long cpuUsage;

    @Field(type = FieldType.Keyword)
    private String traceId;

    @Field(type = FieldType.Keyword)
    private String userId;

    @Field(type = FieldType.Keyword)
    private String status;

    @Field(type = FieldType.Object)
    private Map<String, Object> metrics;

    @Field(type = FieldType.Date)
    private LocalDateTime timestamp;

    @Field(type = FieldType.Keyword)
    private String hostname;

    @Field(type = FieldType.Keyword)
    private String ipAddress;

    @Field(type = FieldType.Text)
    private String description;

    public PerformanceLogEvent() {
        this.timestamp = LocalDateTime.now();
    }
}
