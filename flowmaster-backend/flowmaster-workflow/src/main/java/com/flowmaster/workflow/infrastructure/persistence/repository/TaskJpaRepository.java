package com.flowmaster.workflow.infrastructure.persistence.repository;

import com.flowmaster.workflow.domain.model.entity.Task;
import com.flowmaster.workflow.infrastructure.persistence.entity.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 任务JPA仓储接口
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Repository
public interface TaskJpaRepository extends JpaRepository<TaskEntity, Long>, 
                                          JpaSpecificationExecutor<TaskEntity> {

    /**
     * 根据任务ID查找
     *
     * @param taskId 任务ID
     * @param deleted 是否删除
     * @return 任务实体
     */
    Optional<TaskEntity> findByTaskIdAndDeletedFalse(String taskId);

    /**
     * 根据流程实例ID查找任务
     *
     * @param processInstanceId 流程实例ID
     * @param deleted 是否删除
     * @return 任务列表
     */
    List<TaskEntity> findByProcessInstanceIdAndDeletedFalse(String processInstanceId);

    /**
     * 根据指派人查找任务
     *
     * @param assignee 指派人
     * @param deleted 是否删除
     * @param pageable 分页参数
     * @return 任务分页结果
     */
    Page<TaskEntity> findByAssigneeAndDeletedFalse(Long assignee, Pageable pageable);

    /**
     * 查找用户的待办任务
     *
     * @param assignee 指派人
     * @param status 任务状态
     * @param deleted 是否删除
     * @param pageable 分页参数
     * @return 任务分页结果
     */
    Page<TaskEntity> findByAssigneeAndStatusAndDeletedFalse(Long assignee, Task.TaskStatus status, Pageable pageable);

    /**
     * 查找用户的候选任务
     *
     * @param userId 用户ID
     * @param status 任务状态
     * @param deleted 是否删除
     * @param pageable 分页参数
     * @return 任务分页结果
     */
    @Query("SELECT t FROM TaskEntity t WHERE (t.candidateUsers LIKE %:userId% OR t.candidateGroups LIKE %:userId%) " +
           "AND t.status = :status AND t.deleted = false")
    Page<TaskEntity> findCandidateTasksByUser(@Param("userId") String userId, 
                                             @Param("status") Task.TaskStatus status, 
                                             Pageable pageable);

    /**
     * 查找已过期的任务
     *
     * @param currentTime 当前时间
     * @param status 任务状态
     * @param deleted 是否删除
     * @return 任务列表
     */
    List<TaskEntity> findByDueDateBeforeAndStatusAndDeletedFalse(LocalDateTime currentTime, 
                                                                Task.TaskStatus status);

    /**
     * 查找所有未删除的任务
     *
     * @param deleted 是否删除
     * @param pageable 分页参数
     * @return 任务分页结果
     */
    Page<TaskEntity> findByDeletedFalse(Pageable pageable);

    /**
     * 统计未删除的任务数量
     *
     * @param deleted 是否删除
     * @return 任务数量
     */
    long countByDeletedFalse();

    /**
     * 软删除任务
     *
     * @param id 任务ID
     */
    @Modifying
    @Query("UPDATE TaskEntity t SET t.deleted = true WHERE t.id = :id")
    void softDeleteById(@Param("id") Long id);

    /**
     * 恢复软删除的任务
     *
     * @param id 任务ID
     */
    @Modifying
    @Query("UPDATE TaskEntity t SET t.deleted = false WHERE t.id = :id")
    void restoreById(@Param("id") Long id);

    /**
     * 更新任务状态
     *
     * @param id 任务ID
     * @param status 新状态
     */
    @Modifying
    @Query("UPDATE TaskEntity t SET t.status = :status WHERE t.id = :id")
    void updateStatusById(@Param("id") Long id, @Param("status") Task.TaskStatus status);

    /**
     * 更新任务指派人
     *
     * @param id 任务ID
     * @param assignee 指派人
     */
    @Modifying
    @Query("UPDATE TaskEntity t SET t.assignee = :assignee WHERE t.id = :id")
    void updateAssigneeById(@Param("id") Long id, @Param("assignee") Long assignee);

    /**
     * 更新任务完成时间
     *
     * @param id 任务ID
     * @param completeTime 完成时间
     */
    @Modifying
    @Query("UPDATE TaskEntity t SET t.completeTime = :completeTime WHERE t.id = :id")
    void updateCompleteTimeById(@Param("id") Long id, @Param("completeTime") LocalDateTime completeTime);

    /**
     * 更新任务删除原因
     *
     * @param id 任务ID
     * @param deleteReason 删除原因
     */
    @Modifying
    @Query("UPDATE TaskEntity t SET t.deleteReason = :deleteReason WHERE t.id = :id")
    void updateDeleteReasonById(@Param("id") Long id, @Param("deleteReason") String deleteReason);
}
