package com.flowmaster.workflow.infrastructure.persistence.repository;

import com.flowmaster.workflow.domain.model.aggregate.ProcessDefinition;
import com.flowmaster.workflow.infrastructure.persistence.entity.ProcessDefinitionEntity;
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
 * 流程定义JPA仓储接口
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Repository
public interface ProcessDefinitionJpaRepository extends JpaRepository<ProcessDefinitionEntity, Long>, 
                                                      JpaSpecificationExecutor<ProcessDefinitionEntity> {

    /**
     * 根据流程定义ID查找
     *
     * @param processDefinitionId 流程定义ID
     * @param deleted 是否删除
     * @return 流程定义实体
     */
    Optional<ProcessDefinitionEntity> findByProcessDefinitionIdAndDeletedFalse(String processDefinitionId);

    /**
     * 根据Key查找流程定义
     *
     * @param key 流程定义Key
     * @param deleted 是否删除
     * @return 流程定义列表
     */
    List<ProcessDefinitionEntity> findByKeyAndDeletedFalse(String key);

    /**
     * 根据Key和版本查找流程定义
     *
     * @param key 流程定义Key
     * @param version 版本
     * @param deleted 是否删除
     * @return 流程定义实体
     */
    Optional<ProcessDefinitionEntity> findByKeyAndVersionAndDeletedFalse(String key, Integer version);

    /**
     * 查找最新的流程定义
     *
     * @param key 流程定义Key
     * @param deleted 是否删除
     * @return 流程定义实体
     */
    @Query("SELECT pd FROM ProcessDefinitionEntity pd WHERE pd.key = :key AND pd.deleted = false ORDER BY pd.version DESC")
    Optional<ProcessDefinitionEntity> findLatestByKeyAndDeletedFalse(@Param("key") String key);

    /**
     * 根据分类查找流程定义
     *
     * @param category 分类
     * @param deleted 是否删除
     * @param pageable 分页参数
     * @return 流程定义分页结果
     */
    Page<ProcessDefinitionEntity> findByCategoryAndDeletedFalse(String category, Pageable pageable);

    /**
     * 根据状态查找流程定义
     *
     * @param status 状态
     * @param deleted 是否删除
     * @param pageable 分页参数
     * @return 流程定义分页结果
     */
    Page<ProcessDefinitionEntity> findByStatusAndDeletedFalse(ProcessDefinition.DefinitionStatus status, Pageable pageable);

    /**
     * 查找所有未删除的流程定义
     *
     * @param deleted 是否删除
     * @param pageable 分页参数
     * @return 流程定义分页结果
     */
    Page<ProcessDefinitionEntity> findByDeletedFalse(Pageable pageable);

    /**
     * 统计未删除的流程定义数量
     *
     * @param deleted 是否删除
     * @return 流程定义数量
     */
    long countByDeletedFalse();

    /**
     * 软删除流程定义
     *
     * @param id 流程定义ID
     */
    @Modifying
    @Query("UPDATE ProcessDefinitionEntity pd SET pd.deleted = true WHERE pd.id = :id")
    void softDeleteById(@Param("id") Long id);

    /**
     * 恢复软删除的流程定义
     *
     * @param id 流程定义ID
     */
    @Modifying
    @Query("UPDATE ProcessDefinitionEntity pd SET pd.deleted = false WHERE pd.id = :id")
    void restoreById(@Param("id") Long id);

    /**
     * 更新流程定义状态
     *
     * @param id 流程定义ID
     * @param status 新状态
     */
    @Modifying
    @Query("UPDATE ProcessDefinitionEntity pd SET pd.status = :status WHERE pd.id = :id")
    void updateStatusById(@Param("id") Long id, @Param("status") ProcessDefinition.DefinitionStatus status);

    /**
     * 更新流程定义启用状态
     *
     * @param id 流程定义ID
     * @param enabled 是否启用
     */
    @Modifying
    @Query("UPDATE ProcessDefinitionEntity pd SET pd.enabled = :enabled WHERE pd.id = :id")
    void updateEnabledById(@Param("id") Long id, @Param("enabled") Boolean enabled);
}
