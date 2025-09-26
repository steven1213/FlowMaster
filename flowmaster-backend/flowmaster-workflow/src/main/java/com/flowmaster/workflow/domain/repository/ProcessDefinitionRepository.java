package com.flowmaster.workflow.domain.repository;

import com.flowmaster.workflow.domain.model.aggregate.ProcessDefinition;
import com.flowmaster.workflow.domain.model.valueobject.ProcessDefinitionId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 流程定义仓储接口
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
public interface ProcessDefinitionRepository {

    /**
     * 保存流程定义
     *
     * @param processDefinition 流程定义
     * @return 保存后的流程定义
     */
    ProcessDefinition save(ProcessDefinition processDefinition);

    /**
     * 根据ID查找流程定义
     *
     * @param processDefinitionId 流程定义ID
     * @return 流程定义
     */
    Optional<ProcessDefinition> findById(ProcessDefinitionId processDefinitionId);

    /**
     * 根据Key查找流程定义
     *
     * @param key 流程定义Key
     * @return 流程定义列表
     */
    List<ProcessDefinition> findByKey(String key);

    /**
     * 根据Key和版本查找流程定义
     *
     * @param key 流程定义Key
     * @param version 版本
     * @return 流程定义
     */
    Optional<ProcessDefinition> findByKeyAndVersion(String key, Integer version);

    /**
     * 查找最新的流程定义
     *
     * @param key 流程定义Key
     * @return 流程定义
     */
    Optional<ProcessDefinition> findLatestByKey(String key);

    /**
     * 分页查询流程定义
     *
     * @param pageable 分页参数
     * @return 流程定义分页结果
     */
    Page<ProcessDefinition> findAll(Pageable pageable);

    /**
     * 根据分类查找流程定义
     *
     * @param category 分类
     * @param pageable 分页参数
     * @return 流程定义分页结果
     */
    Page<ProcessDefinition> findByCategory(String category, Pageable pageable);

    /**
     * 根据状态查找流程定义
     *
     * @param status 状态
     * @param pageable 分页参数
     * @return 流程定义分页结果
     */
    Page<ProcessDefinition> findByStatus(ProcessDefinition.DefinitionStatus status, Pageable pageable);

    /**
     * 删除流程定义
     *
     * @param processDefinitionId 流程定义ID
     */
    void deleteById(ProcessDefinitionId processDefinitionId);

    /**
     * 统计流程定义数量
     *
     * @return 流程定义数量
     */
    long count();
}