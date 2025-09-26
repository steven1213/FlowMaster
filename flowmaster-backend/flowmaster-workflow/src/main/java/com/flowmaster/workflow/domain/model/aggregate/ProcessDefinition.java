package com.flowmaster.workflow.domain.model.aggregate;

import com.flowmaster.common.domain.BaseDomainEntity;
import com.flowmaster.workflow.domain.model.valueobject.ProcessDefinitionId;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 流程定义聚合根
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Getter
@Setter
@Slf4j
public class ProcessDefinition extends BaseDomainEntity {

    /**
     * 流程定义ID
     */
    private ProcessDefinitionId processDefinitionId;

    /**
     * 流程定义名称
     */
    private String name;

    /**
     * 流程定义Key
     */
    private String key;

    /**
     * 流程定义版本
     */
    private Integer version;

    /**
     * 流程定义分类
     */
    private String category;

    /**
     * 流程定义描述
     */
    private String description;

    /**
     * 流程定义状态
     */
    private DefinitionStatus status;

    /**
     * 流程定义XML内容
     */
    private String xmlContent;

    /**
     * 流程定义图片内容
     */
    private String imageContent;

    /**
     * 流程定义部署时间
     */
    private LocalDateTime deploymentTime;

    /**
     * 流程定义部署人
     */
    private Long deployedBy;

    /**
     * 流程定义标签
     */
    private String tags;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 领域事件列表
     */
    private final List<Object> domainEvents = new ArrayList<>();

    /**
     * 流程定义状态枚举
     */
    public enum DefinitionStatus {
        DRAFT("草稿"),
        ACTIVE("激活"),
        SUSPENDED("暂停"),
        DEPRECATED("废弃");

        private final String description;

        DefinitionStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 创建流程定义
     *
     * @param processDefinitionId 流程定义ID
     * @param name 流程定义名称
     * @param key 流程定义Key
     * @param version 流程定义版本
     * @param category 流程定义分类
     * @param description 流程定义描述
     * @param xmlContent 流程定义XML内容
     * @param deployedBy 部署人
     * @return 流程定义
     */
    public static ProcessDefinition create(ProcessDefinitionId processDefinitionId, String name, String key, 
                                         Integer version, String category, String description, 
                                         String xmlContent, Long deployedBy) {
        ProcessDefinition definition = new ProcessDefinition();
        definition.processDefinitionId = processDefinitionId;
        definition.name = name;
        definition.key = key;
        definition.version = version;
        definition.category = category;
        definition.description = description;
        definition.xmlContent = xmlContent;
        definition.status = DefinitionStatus.DRAFT;
        definition.deploymentTime = LocalDateTime.now();
        definition.deployedBy = deployedBy;
        definition.enabled = true;
        
        definition.addDomainEvent(new ProcessDefinitionCreatedEvent(definition));
        return definition;
    }

    /**
     * 激活流程定义
     */
    public void activate() {
        this.status = DefinitionStatus.ACTIVE;
        this.enabled = true;
        
        addDomainEvent(new ProcessDefinitionActivatedEvent(this));
    }

    /**
     * 暂停流程定义
     */
    public void suspend() {
        this.status = DefinitionStatus.SUSPENDED;
        this.enabled = false;
        
        addDomainEvent(new ProcessDefinitionSuspendedEvent(this));
    }

    /**
     * 废弃流程定义
     */
    public void deprecate() {
        this.status = DefinitionStatus.DEPRECATED;
        this.enabled = false;
        
        addDomainEvent(new ProcessDefinitionDeprecatedEvent(this));
    }

    /**
     * 更新流程定义
     *
     * @param name 流程定义名称
     * @param description 流程定义描述
     * @param category 流程定义分类
     * @param tags 流程定义标签
     */
    public void update(String name, String description, String category, String tags) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.tags = tags;
        
        addDomainEvent(new ProcessDefinitionUpdatedEvent(this));
    }

    /**
     * 更新流程定义内容
     *
     * @param xmlContent 流程定义XML内容
     * @param imageContent 流程定义图片内容
     */
    public void updateContent(String xmlContent, String imageContent) {
        this.xmlContent = xmlContent;
        this.imageContent = imageContent;
        
        addDomainEvent(new ProcessDefinitionContentUpdatedEvent(this));
    }

    /**
     * 检查流程定义是否可用
     *
     * @return 是否可用
     */
    public boolean isAvailable() {
        return status == DefinitionStatus.ACTIVE && enabled;
    }

    /**
     * 检查流程定义是否可以启动
     *
     * @return 是否可以启动
     */
    public boolean canStart() {
        return isAvailable();
    }

    /**
     * 添加领域事件
     *
     * @param event 领域事件
     */
    private void addDomainEvent(Object event) {
        domainEvents.add(event);
    }

    /**
     * 获取领域事件
     *
     * @return 领域事件列表
     */
    @DomainEvents
    public List<Object> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    /**
     * 清理领域事件
     */
    @AfterDomainEventPublication
    public void clearDomainEvents() {
        domainEvents.clear();
    }

    // 领域事件类
    public static class ProcessDefinitionCreatedEvent {
        private final ProcessDefinition processDefinition;
        
        public ProcessDefinitionCreatedEvent(ProcessDefinition processDefinition) {
            this.processDefinition = processDefinition;
        }
        
        public ProcessDefinition getProcessDefinition() {
            return processDefinition;
        }
    }

    public static class ProcessDefinitionActivatedEvent {
        private final ProcessDefinition processDefinition;
        
        public ProcessDefinitionActivatedEvent(ProcessDefinition processDefinition) {
            this.processDefinition = processDefinition;
        }
        
        public ProcessDefinition getProcessDefinition() {
            return processDefinition;
        }
    }

    public static class ProcessDefinitionSuspendedEvent {
        private final ProcessDefinition processDefinition;
        
        public ProcessDefinitionSuspendedEvent(ProcessDefinition processDefinition) {
            this.processDefinition = processDefinition;
        }
        
        public ProcessDefinition getProcessDefinition() {
            return processDefinition;
        }
    }

    public static class ProcessDefinitionDeprecatedEvent {
        private final ProcessDefinition processDefinition;
        
        public ProcessDefinitionDeprecatedEvent(ProcessDefinition processDefinition) {
            this.processDefinition = processDefinition;
        }
        
        public ProcessDefinition getProcessDefinition() {
            return processDefinition;
        }
    }

    public static class ProcessDefinitionUpdatedEvent {
        private final ProcessDefinition processDefinition;
        
        public ProcessDefinitionUpdatedEvent(ProcessDefinition processDefinition) {
            this.processDefinition = processDefinition;
        }
        
        public ProcessDefinition getProcessDefinition() {
            return processDefinition;
        }
    }

    public static class ProcessDefinitionContentUpdatedEvent {
        private final ProcessDefinition processDefinition;
        
        public ProcessDefinitionContentUpdatedEvent(ProcessDefinition processDefinition) {
            this.processDefinition = processDefinition;
        }
        
        public ProcessDefinition getProcessDefinition() {
            return processDefinition;
        }
    }
}
