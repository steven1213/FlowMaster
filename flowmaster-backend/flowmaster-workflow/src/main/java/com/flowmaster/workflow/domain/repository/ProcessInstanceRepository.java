package com.flowmaster.workflow.domain.repository;

import com.flowmaster.workflow.domain.model.aggregate.ProcessInstance;
import com.flowmaster.workflow.domain.model.valueobject.ProcessDefinitionId;
import com.flowmaster.workflow.domain.model.valueobject.ProcessInstanceId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 流程实例仓储接口
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
public interface ProcessInstanceRepository {

    /**
     * 保存流程实例
     *
     * @param processInstance 流程实例
     * @return 保存后的流程实例
     */
    ProcessInstance save(ProcessInstance processInstance);

    /**
     * 根据ID查找流程实例
     *
     * @param processInstanceId 流程实例ID
     * @return 流程实例
     */
    Optional<ProcessInstance> findById(ProcessInstanceId processInstanceId);

    /**
     * 根据业务Key查找流程实例
     *
     * @param businessKey 业务Key
     * @return 流程实例列表
     */
    List<ProcessInstance> findByBusinessKey(String businessKey);

    /**
     * 根据流程定义ID查找流程实例
     *
     * @param processDefinitionId 流程定义ID
     * @param pageable 分页参数
     * @return 流程实例分页结果
     */
    Page<ProcessInstance> findByProcessDefinitionId(ProcessDefinitionId processDefinitionId, Pageable pageable);

    /**
     * 根据启动人查找流程实例
     *
     * @param startedBy 启动人
     * @param pageable 分页参数
     * @return 流程实例分页结果
     */
    Page<ProcessInstance> findByStartedBy(Long startedBy, Pageable pageable);

    /**
     * 查找运行中的流程实例
     *
     * @param pageable 分页参数
     * @return 流程实例分页结果
     */
    Page<ProcessInstance> findRunningInstances(Pageable pageable);

    /**
     * 查找已完成的流程实例
     *
     * @param pageable 分页参数
     * @return 流程实例分页结果
     */
    Page<ProcessInstance> findCompletedInstances(Pageable pageable);

    /**
     * 删除流程实例
     *
     * @param processInstanceId 流程实例ID
     */
    void deleteById(ProcessInstanceId processInstanceId);

    /**
     * 查找所有未删除的流程实例
     *
     * @param pageable 分页参数
     * @return 流程实例分页结果
     */
    Page<ProcessInstance> findByDeletedFalse(Pageable pageable);

    /**
     * 统计流程实例数量
     *
     * @return 流程实例数量
     */
    long count();
}
