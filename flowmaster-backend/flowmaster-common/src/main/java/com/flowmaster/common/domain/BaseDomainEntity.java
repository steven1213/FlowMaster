package com.flowmaster.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * 基础领域实体
 * 定义所有领域实体的公共属性
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Getter
@Setter
@Slf4j
public abstract class BaseDomainEntity {

    /**
     * 主键ID
     */
    protected Long id;

    /**
     * 创建时间
     */
    protected LocalDateTime createdAt;

    /**
     * 更新时间
     */
    protected LocalDateTime updatedAt;

    /**
     * 创建人ID
     */
    protected Long createdBy;

    /**
     * 更新人ID
     */
    protected Long updatedBy;

    /**
     * 版本号（乐观锁）
     */
    protected Integer version;

    /**
     * 是否删除（逻辑删除）
     */
    protected Boolean deleted;

    /**
     * 构造函数
     */
    protected BaseDomainEntity() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.version = 1;
        this.deleted = false;
    }

    /**
     * 更新实体
     *
     * @param updatedBy 更新人ID
     */
    protected void update(Long updatedBy) {
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = updatedBy;
        this.version++;
        
        log.debug("实体更新: entity={}, updatedBy={}, version={}", 
                this.getClass().getSimpleName(), updatedBy, this.version);
    }

    /**
     * 创建实体
     *
     * @param createdBy 创建人ID
     */
    protected void create(Long createdBy) {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.createdBy = createdBy;
        this.updatedBy = createdBy;
        this.version = 1;
        this.deleted = false;
        
        log.debug("实体创建: entity={}, createdBy={}", 
                this.getClass().getSimpleName(), createdBy);
    }

    /**
     * 逻辑删除
     *
     * @param deletedBy 删除人ID
     */
    protected void delete(Long deletedBy) {
        this.deleted = true;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = deletedBy;
        this.version++;
        
        log.debug("实体删除: entity={}, deletedBy={}", 
                this.getClass().getSimpleName(), deletedBy);
    }

    /**
     * 恢复删除
     *
     * @param restoredBy 恢复人ID
     */
    protected void restore(Long restoredBy) {
        this.deleted = false;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = restoredBy;
        this.version++;
        
        log.debug("实体恢复: entity={}, restoredBy={}", 
                this.getClass().getSimpleName(), restoredBy);
    }

    /**
     * 检查实体是否已删除
     *
     * @return 是否已删除
     */
    public boolean isDeleted() {
        return Boolean.TRUE.equals(this.deleted);
    }

    /**
     * 检查实体是否有效
     *
     * @return 是否有效
     */
    public boolean isValid() {
        return !isDeleted();
    }

    /**
     * 获取实体年龄（从创建到现在的时间）
     *
     * @return 实体年龄（秒）
     */
    public long getAgeInSeconds() {
        if (createdAt == null) {
            return 0;
        }
        return java.time.Duration.between(createdAt, LocalDateTime.now()).getSeconds();
    }

    /**
     * 获取最后更新时间距离现在的时间
     *
     * @return 最后更新时间距离现在的时间（秒）
     */
    public long getLastUpdateAgeInSeconds() {
        if (updatedAt == null) {
            return 0;
        }
        return java.time.Duration.between(updatedAt, LocalDateTime.now()).getSeconds();
    }

    @Override
    public String toString() {
        return String.format("%s{createdAt=%s, updatedAt=%s, createdBy=%d, updatedBy=%d, version=%d, deleted=%s}",
                this.getClass().getSimpleName(),
                createdAt,
                updatedAt,
                createdBy,
                updatedBy,
                version,
                deleted);
    }
}
