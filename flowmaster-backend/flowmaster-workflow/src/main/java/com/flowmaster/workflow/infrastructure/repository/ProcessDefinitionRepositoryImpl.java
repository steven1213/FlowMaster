package com.flowmaster.workflow.infrastructure.repository;

import com.flowmaster.workflow.domain.model.aggregate.ProcessDefinition;
import com.flowmaster.workflow.domain.model.valueobject.ProcessDefinitionId;
import com.flowmaster.workflow.domain.repository.ProcessDefinitionRepository;
import com.flowmaster.workflow.infrastructure.persistence.entity.ProcessDefinitionEntity;
import com.flowmaster.workflow.infrastructure.persistence.repository.ProcessDefinitionJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 流程定义仓储实现
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class ProcessDefinitionRepositoryImpl implements ProcessDefinitionRepository {

    private final ProcessDefinitionJpaRepository jpaRepository;

    @Override
    public ProcessDefinition save(ProcessDefinition processDefinition) {
        log.debug("保存流程定义: processDefinitionId={}", processDefinition.getProcessDefinitionId().getValue());
        try {
            ProcessDefinitionEntity entity = ProcessDefinitionEntity.fromDomain(processDefinition);
            ProcessDefinitionEntity savedEntity = jpaRepository.save(entity);
            ProcessDefinition savedDefinition = savedEntity.toDomain();
            log.debug("流程定义保存成功: processDefinitionId={}", savedDefinition.getProcessDefinitionId().getValue());
            return savedDefinition;
        } catch (Exception e) {
            log.error("流程定义保存失败: processDefinitionId={}, error={}", 
                     processDefinition.getProcessDefinitionId().getValue(), e.getMessage(), e);
            throw new RuntimeException("流程定义保存失败", e);
        }
    }

    @Override
    public Optional<ProcessDefinition> findById(ProcessDefinitionId processDefinitionId) {
        log.debug("根据ID查找流程定义: processDefinitionId={}", processDefinitionId.getValue());
        try {
            Optional<ProcessDefinitionEntity> entityOpt = jpaRepository.findByProcessDefinitionIdAndDeletedFalse(processDefinitionId.getValue());
            if (entityOpt.isPresent()) {
                ProcessDefinition definition = entityOpt.get().toDomain();
                log.debug("流程定义查找成功: processDefinitionId={}", processDefinitionId.getValue());
                return Optional.of(definition);
            } else {
                log.debug("流程定义未找到: processDefinitionId={}", processDefinitionId.getValue());
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("流程定义查找失败: processDefinitionId={}, error={}", 
                     processDefinitionId.getValue(), e.getMessage(), e);
            throw new RuntimeException("流程定义查找失败", e);
        }
    }

    @Override
    public List<ProcessDefinition> findByKey(String key) {
        log.debug("根据Key查找流程定义: key={}", key);
        try {
            List<ProcessDefinitionEntity> entities = jpaRepository.findByKeyAndDeletedFalse(key);
            List<ProcessDefinition> definitions = entities.stream()
                .map(ProcessDefinitionEntity::toDomain)
                .collect(Collectors.toList());
            log.debug("流程定义查找成功: key={}, count={}", key, definitions.size());
            return definitions;
        } catch (Exception e) {
            log.error("流程定义查找失败: key={}, error={}", key, e.getMessage(), e);
            throw new RuntimeException("流程定义查找失败", e);
        }
    }

    @Override
    public Optional<ProcessDefinition> findByKeyAndVersion(String key, Integer version) {
        log.debug("根据Key和版本查找流程定义: key={}, version={}", key, version);
        try {
            Optional<ProcessDefinitionEntity> entityOpt = jpaRepository.findByKeyAndVersionAndDeletedFalse(key, version);
            if (entityOpt.isPresent()) {
                ProcessDefinition definition = entityOpt.get().toDomain();
                log.debug("流程定义查找成功: key={}, version={}", key, version);
                return Optional.of(definition);
            } else {
                log.debug("流程定义未找到: key={}, version={}", key, version);
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("流程定义查找失败: key={}, version={}, error={}", key, version, e.getMessage(), e);
            throw new RuntimeException("流程定义查找失败", e);
        }
    }

    @Override
    public Optional<ProcessDefinition> findLatestByKey(String key) {
        log.debug("查找最新流程定义: key={}", key);
        try {
            Optional<ProcessDefinitionEntity> entityOpt = jpaRepository.findLatestByKeyAndDeletedFalse(key);
            if (entityOpt.isPresent()) {
                ProcessDefinition definition = entityOpt.get().toDomain();
                log.debug("最新流程定义查找成功: key={}", key);
                return Optional.of(definition);
            } else {
                log.debug("最新流程定义未找到: key={}", key);
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("最新流程定义查找失败: key={}, error={}", key, e.getMessage(), e);
            throw new RuntimeException("最新流程定义查找失败", e);
        }
    }

    @Override
    public Page<ProcessDefinition> findAll(Pageable pageable) {
        log.debug("分页查询流程定义: pageable={}", pageable);
        try {
            Page<ProcessDefinitionEntity> entityPage = jpaRepository.findByDeletedFalse(pageable);
            Page<ProcessDefinition> definitionPage = entityPage.map(ProcessDefinitionEntity::toDomain);
            log.debug("流程定义分页查询成功: totalElements={}", definitionPage.getTotalElements());
            return definitionPage;
        } catch (Exception e) {
            log.error("流程定义分页查询失败: error={}", e.getMessage(), e);
            throw new RuntimeException("流程定义分页查询失败", e);
        }
    }

    @Override
    public Page<ProcessDefinition> findByCategory(String category, Pageable pageable) {
        log.debug("根据分类查找流程定义: category={}, pageable={}", category, pageable);
        try {
            Page<ProcessDefinitionEntity> entityPage = jpaRepository.findByCategoryAndDeletedFalse(category, pageable);
            Page<ProcessDefinition> definitionPage = entityPage.map(ProcessDefinitionEntity::toDomain);
            log.debug("流程定义分类查询成功: category={}, totalElements={}", category, definitionPage.getTotalElements());
            return definitionPage;
        } catch (Exception e) {
            log.error("流程定义分类查询失败: category={}, error={}", category, e.getMessage(), e);
            throw new RuntimeException("流程定义分类查询失败", e);
        }
    }

    @Override
    public Page<ProcessDefinition> findByStatus(ProcessDefinition.DefinitionStatus status, Pageable pageable) {
        log.debug("根据状态查找流程定义: status={}, pageable={}", status, pageable);
        try {
            Page<ProcessDefinitionEntity> entityPage = jpaRepository.findByStatusAndDeletedFalse(status, pageable);
            Page<ProcessDefinition> definitionPage = entityPage.map(ProcessDefinitionEntity::toDomain);
            log.debug("流程定义状态查询成功: status={}, totalElements={}", status, definitionPage.getTotalElements());
            return definitionPage;
        } catch (Exception e) {
            log.error("流程定义状态查询失败: status={}, error={}", status, e.getMessage(), e);
            throw new RuntimeException("流程定义状态查询失败", e);
        }
    }

    @Override
    public void deleteById(ProcessDefinitionId processDefinitionId) {
        log.info("删除流程定义: processDefinitionId={}", processDefinitionId.getValue());
        try {
            Optional<ProcessDefinitionEntity> entityOpt = jpaRepository.findByProcessDefinitionIdAndDeletedFalse(processDefinitionId.getValue());
            if (entityOpt.isPresent()) {
                jpaRepository.softDeleteById(entityOpt.get().getId());
                log.info("流程定义删除成功: processDefinitionId={}", processDefinitionId.getValue());
            } else {
                log.warn("流程定义未找到，无法删除: processDefinitionId={}", processDefinitionId.getValue());
            }
        } catch (Exception e) {
            log.error("流程定义删除失败: processDefinitionId={}, error={}", 
                     processDefinitionId.getValue(), e.getMessage(), e);
            throw new RuntimeException("流程定义删除失败", e);
        }
    }

    @Override
    public long count() {
        log.debug("统计流程定义数量");
        try {
            long count = jpaRepository.countByDeletedFalse();
            log.debug("流程定义数量统计成功: count={}", count);
            return count;
        } catch (Exception e) {
            log.error("流程定义数量统计失败: error={}", e.getMessage(), e);
            throw new RuntimeException("流程定义数量统计失败", e);
        }
    }
}
