package com.flowmaster.workflow.infrastructure.persistence.entity;

import com.flowmaster.common.infrastructure.persistence.BaseJpaEntity;
import com.flowmaster.workflow.domain.model.aggregate.ProcessDefinition;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

/**
 * 流程定义JPA实体
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Entity
@Table(name = "wf_process_definition", indexes = {
    @Index(name = "idx_process_def_key", columnList = "process_key"),
    @Index(name = "idx_process_def_category", columnList = "category"),
    @Index(name = "idx_process_def_status", columnList = "status"),
    @Index(name = "idx_process_def_deleted", columnList = "deleted")
})
@Getter
@Setter
@Slf4j
@SQLDelete(sql = "UPDATE wf_process_definition SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class ProcessDefinitionEntity extends BaseJpaEntity {

    /**
     * 流程定义ID
     */
    @Column(name = "process_definition_id", nullable = false, unique = true, length = 64)
    private String processDefinitionId;

    /**
     * 流程定义名称
     */
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    /**
     * 流程定义Key
     */
    @Column(name = "process_key", nullable = false, length = 64)
    private String key;

    /**
     * 流程定义版本
     */
    @Column(name = "version", nullable = false)
    private Integer version;

    /**
     * 流程定义分类
     */
    @Column(name = "category", length = 64)
    private String category;

    /**
     * 流程定义描述
     */
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * 流程定义状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ProcessDefinition.DefinitionStatus status;

    /**
     * 流程定义XML内容
     */
    @Lob
    @Column(name = "xml_content", columnDefinition = "LONGTEXT")
    private String xmlContent;

    /**
     * 流程定义图片内容
     */
    @Lob
    @Column(name = "image_content", columnDefinition = "LONGBLOB")
    private String imageContent;

    /**
     * 流程定义部署时间
     */
    @Column(name = "deployment_time")
    private LocalDateTime deploymentTime;

    /**
     * 流程定义部署人
     */
    @Column(name = "deployed_by")
    private Long deployedBy;

    /**
     * 流程定义标签
     */
    @Column(name = "tags", length = 500)
    private String tags;

    /**
     * 是否启用
     */
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    /**
     * 转换为领域对象
     *
     * @return 流程定义聚合根
     */
    public ProcessDefinition toDomain() {
        ProcessDefinition definition = new ProcessDefinition();
        definition.setId(this.getId());
        definition.setProcessDefinitionId(com.flowmaster.workflow.domain.model.valueobject.ProcessDefinitionId.of(this.processDefinitionId));
        definition.setName(this.name);
        definition.setKey(this.key);
        definition.setVersion(this.version);
        definition.setCategory(this.category);
        definition.setDescription(this.description);
        definition.setStatus(this.status);
        definition.setXmlContent(this.xmlContent);
        definition.setImageContent(this.imageContent);
        definition.setDeploymentTime(this.deploymentTime);
        definition.setDeployedBy(this.deployedBy);
        definition.setTags(this.tags);
        definition.setEnabled(this.enabled);
        definition.setCreatedAt(this.getCreatedAt());
        definition.setUpdatedAt(this.getUpdatedAt());
        definition.setCreatedBy(this.getCreatedBy());
        definition.setUpdatedBy(this.getUpdatedBy());
        definition.setVersion(this.getVersion());
        definition.setDeleted(this.getDeleted());
        return definition;
    }

    /**
     * 从领域对象创建
     *
     * @param definition 流程定义聚合根
     * @return JPA实体
     */
    public static ProcessDefinitionEntity fromDomain(ProcessDefinition definition) {
        ProcessDefinitionEntity entity = new ProcessDefinitionEntity();
        entity.setId(definition.getId());
        entity.processDefinitionId = definition.getProcessDefinitionId().getValue();
        entity.name = definition.getName();
        entity.key = definition.getKey();
        entity.version = definition.getVersion();
        entity.category = definition.getCategory();
        entity.description = definition.getDescription();
        entity.status = definition.getStatus();
        entity.xmlContent = definition.getXmlContent();
        entity.imageContent = definition.getImageContent();
        entity.deploymentTime = definition.getDeploymentTime();
        entity.deployedBy = definition.getDeployedBy();
        entity.tags = definition.getTags();
        entity.enabled = definition.getEnabled();
        entity.setCreatedAt(definition.getCreatedAt());
        entity.setUpdatedAt(definition.getUpdatedAt());
        entity.setCreatedBy(definition.getCreatedBy());
        entity.setUpdatedBy(definition.getUpdatedBy());
        entity.setVersion(definition.getVersion());
        entity.setDeleted(definition.getDeleted());
        return entity;
    }
}
