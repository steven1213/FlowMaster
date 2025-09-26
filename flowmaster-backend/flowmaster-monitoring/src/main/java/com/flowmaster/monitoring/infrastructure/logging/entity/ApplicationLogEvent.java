package com.flowmaster.monitoring.infrastructure.logging.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 应用日志事件
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Data
@Document(indexName = "flowmaster-app-logs")
public class ApplicationLogEvent {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String serviceName;

    @Field(type = FieldType.Keyword)
    private String level;

    @Field(type = FieldType.Text)
    private String message;

    @Field(type = FieldType.Text)
    private String logger;

    @Field(type = FieldType.Keyword)
    private String threadName;

    @Field(type = FieldType.Keyword)
    private String traceId;

    @Field(type = FieldType.Keyword)
    private String userId;

    @Field(type = FieldType.Keyword)
    private String operation;

    @Field(type = FieldType.Object)
    private Map<String, Object> metadata;

    @Field(type = FieldType.Date)
    private LocalDateTime timestamp;

    @Field(type = FieldType.Keyword)
    private String hostname;

    @Field(type = FieldType.Keyword)
    private String ipAddress;

    public ApplicationLogEvent() {
        this.timestamp = LocalDateTime.now();
    }
}
