package com.flowmaster.monitoring.infrastructure.logging.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 错误日志事件
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Data
@Document(indexName = "flowmaster-error-logs")
public class ErrorLogEvent {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String serviceName;

    @Field(type = FieldType.Keyword)
    private String errorType;

    @Field(type = FieldType.Text)
    private String errorMessage;

    @Field(type = FieldType.Text)
    private String stackTrace;

    @Field(type = FieldType.Keyword)
    private String exceptionClass;

    @Field(type = FieldType.Keyword)
    private String traceId;

    @Field(type = FieldType.Keyword)
    private String userId;

    @Field(type = FieldType.Keyword)
    private String operation;

    @Field(type = FieldType.Object)
    private Map<String, Object> context;

    @Field(type = FieldType.Date)
    private LocalDateTime timestamp;

    @Field(type = FieldType.Keyword)
    private String hostname;

    @Field(type = FieldType.Keyword)
    private String ipAddress;

    @Field(type = FieldType.Keyword)
    private String severity;

    public ErrorLogEvent() {
        this.timestamp = LocalDateTime.now();
        this.severity = "ERROR";
    }
}
