package com.flowmaster.common.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * JPA基础实体
 * 定义所有JPA实体的公共属性和注解
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Getter
@Setter
@Slf4j
@MappedSuperclass
public abstract class BaseJpaEntity {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    protected Long id;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    protected LocalDateTime updatedAt;

    /**
     * 创建人ID
     */
    @Column(name = "created_by")
    protected Long createdBy;

    /**
     * 更新人ID
     */
    @Column(name = "updated_by")
    protected Long updatedBy;

    /**
     * 版本号（乐观锁）
     */
    @Version
    @Column(name = "version", nullable = false)
    protected Integer version = 1;

    /**
     * 是否删除（逻辑删除）
     */
    @Column(name = "deleted", nullable = false)
    protected Boolean deleted = false;

    /**
     * 构造函数
     */
    protected BaseJpaEntity() {
        this.version = 1;
        this.deleted = false;
    }

    /**
     * 更新实体
     *
     * @param updatedBy 更新人ID
     */
    protected void update(Long updatedBy) {
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
        return String.format("%s{id=%d, createdAt=%s, updatedAt=%s, createdBy=%d, updatedBy=%d, version=%d, deleted=%s}",
                this.getClass().getSimpleName(),
                id,
                createdAt,
                updatedAt,
                createdBy,
                updatedBy,
                version,
                deleted);
    }
}
