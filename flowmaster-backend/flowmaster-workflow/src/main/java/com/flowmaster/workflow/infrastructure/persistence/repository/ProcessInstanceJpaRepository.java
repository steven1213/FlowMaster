package com.flowmaster.workflow.infrastructure.persistence.repository;

import com.flowmaster.workflow.domain.model.valueobject.ProcessStatus;
import com.flowmaster.workflow.infrastructure.persistence.entity.ProcessInstanceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 流程实例JPA仓储接口
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Repository
public interface ProcessInstanceJpaRepository extends JpaRepository<ProcessInstanceEntity, Long>, 
                                                     JpaSpecificationExecutor<ProcessInstanceEntity> {

    /**
     * 根据流程实例ID查找
     *
     * @param processInstanceId 流程实例ID
     * @param deleted 是否删除
     * @return 流程实例实体
     */
    Optional<ProcessInstanceEntity> findByProcessInstanceIdAndDeletedFalse(String processInstanceId);

    /**
     * 根据业务Key查找流程实例
     *
     * @param businessKey 业务Key
     * @param deleted 是否删除
     * @return 流程实例列表
     */
    List<ProcessInstanceEntity> findByBusinessKeyAndDeletedFalse(String businessKey);

    /**
     * 根据流程定义ID查找流程实例
     *
     * @param processDefinitionId 流程定义ID
     * @param deleted 是否删除
     * @param pageable 分页参数
     * @return 流程实例分页结果
     */
    Page<ProcessInstanceEntity> findByProcessDefinitionIdAndDeletedFalse(String processDefinitionId, Pageable pageable);

    /**
     * 根据启动人查找流程实例
     *
     * @param startedBy 启动人
     * @param deleted 是否删除
     * @param pageable 分页参数
     * @return 流程实例分页结果
     */
    Page<ProcessInstanceEntity> findByStartedByAndDeletedFalse(Long startedBy, Pageable pageable);

    /**
     * 查找运行中的流程实例
     *
     * @param status 状态
     * @param deleted 是否删除
     * @param pageable 分页参数
     * @return 流程实例分页结果
     */
    Page<ProcessInstanceEntity> findByStatusAndDeletedFalse(ProcessStatus.Status status, Pageable pageable);

    /**
     * 查找所有未删除的流程实例
     *
     * @param deleted 是否删除
     * @param pageable 分页参数
     * @return 流程实例分页结果
     */
    Page<ProcessInstanceEntity> findByDeletedFalse(Pageable pageable);

    /**
     * 统计未删除的流程实例数量
     *
     * @param deleted 是否删除
     * @return 流程实例数量
     */
    long countByDeletedFalse();

    /**
     * 软删除流程实例
     *
     * @param id 流程实例ID
     */
    @Modifying
    @Query("UPDATE ProcessInstanceEntity pi SET pi.deleted = true WHERE pi.id = :id")
    void softDeleteById(@Param("id") Long id);

    /**
     * 恢复软删除的流程实例
     *
     * @param id 流程实例ID
     */
    @Modifying
    @Query("UPDATE ProcessInstanceEntity pi SET pi.deleted = false WHERE pi.id = :id")
    void restoreById(@Param("id") Long id);

    /**
     * 更新流程实例状态
     *
     * @param id 流程实例ID
     * @param status 新状态
     */
    @Modifying
    @Query("UPDATE ProcessInstanceEntity pi SET pi.status = :status WHERE pi.id = :id")
    void updateStatusById(@Param("id") Long id, @Param("status") ProcessStatus.Status status);

    /**
     * 更新流程实例结束时间
     *
     * @param id 流程实例ID
     * @param endTime 结束时间
     */
    @Modifying
    @Query("UPDATE ProcessInstanceEntity pi SET pi.endTime = :endTime WHERE pi.id = :id")
    void updateEndTimeById(@Param("id") Long id, @Param("endTime") java.time.LocalDateTime endTime);

    /**
     * 更新流程实例删除原因
     *
     * @param id 流程实例ID
     * @param deleteReason 删除原因
     */
    @Modifying
    @Query("UPDATE ProcessInstanceEntity pi SET pi.deleteReason = :deleteReason WHERE pi.id = :id")
    void updateDeleteReasonById(@Param("id") Long id, @Param("deleteReason") String deleteReason);
}
