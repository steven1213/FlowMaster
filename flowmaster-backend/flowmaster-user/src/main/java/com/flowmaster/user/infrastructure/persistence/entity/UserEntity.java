package com.flowmaster.user.infrastructure.persistence.entity;

import com.flowmaster.user.domain.model.valueobject.UserId;
import com.flowmaster.user.domain.model.valueobject.Username;
import com.flowmaster.user.domain.model.valueobject.Email;
import com.flowmaster.user.domain.model.valueobject.Phone;
import com.flowmaster.user.domain.model.valueobject.Password;
import com.flowmaster.user.domain.model.valueobject.UserStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户JPA实体
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_users_username", columnList = "username", unique = true),
    @Index(name = "idx_users_email", columnList = "email", unique = true),
    @Index(name = "idx_users_phone", columnList = "phone", unique = true),
    @Index(name = "idx_users_status", columnList = "status"),
    @Index(name = "idx_users_created_at", columnList = "created_at"),
    @Index(name = "idx_users_updated_at", columnList = "updated_at")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class UserEntity {

    /**
     * 用户ID
     */
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * 用户名
     */
    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;

    /**
     * 邮箱
     */
    @Column(name = "email", length = 100, unique = true)
    private String email;

    /**
     * 手机号
     */
    @Column(name = "phone", length = 20, unique = true)
    private String phone;

    /**
     * 密码哈希
     */
    @Column(name = "password_hash", length = 255, nullable = false)
    private String passwordHash;

    /**
     * 用户状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private UserStatus status;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 创建人ID
     */
    @Column(name = "created_by")
    private Long createdBy;

    /**
     * 更新人ID
     */
    @Column(name = "updated_by")
    private Long updatedBy;

    /**
     * 版本号（乐观锁）
     */
    @Version
    @Column(name = "version")
    private Integer version;

    /**
     * 是否删除（逻辑删除）
     */
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    /**
     * 用户资料
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfileEntity profile;

    /**
     * 默认构造函数
     */
    public UserEntity() {
    }

    /**
     * 构造函数
     */
    public UserEntity(Long id, String username, String email, String phone, 
                     String passwordHash, UserStatus status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.status = status;
        this.deleted = false;
    }

    /**
     * 转换为领域对象
     */
    public com.flowmaster.user.domain.model.aggregate.User toDomain() {
        com.flowmaster.user.domain.model.aggregate.User user = 
            com.flowmaster.user.domain.model.aggregate.User.create(
                Username.of(this.username),
                this.email != null ? Email.of(this.email) : null,
                this.phone != null ? Phone.of(this.phone) : null,
                Password.ofEncoded(this.passwordHash)
            );
        
        // 设置ID和其他属性
        user.setUserId(UserId.of(this.id));
        user.setStatus(this.status);
        
        // 设置基础属性
        user.setCreatedAt(this.createdAt);
        user.setUpdatedAt(this.updatedAt);
        user.setCreatedBy(this.createdBy);
        user.setUpdatedBy(this.updatedBy);
        user.setVersion(this.version);
        user.setDeleted(this.deleted);
        
        return user;
    }

    /**
     * 从领域对象创建
     */
    public static UserEntity fromDomain(com.flowmaster.user.domain.model.aggregate.User user) {
        UserEntity entity = new UserEntity();
        entity.id = user.getUserId().getValue();
        entity.username = user.getUsername().getValue();
        entity.email = user.getEmail() != null ? user.getEmail().getValue() : null;
        entity.phone = user.getPhone() != null ? user.getPhone().getValue() : null;
        entity.passwordHash = user.getPassword().getHashedPassword();
        entity.status = user.getStatus();
        entity.createdAt = user.getCreatedAt();
        entity.updatedAt = user.getUpdatedAt();
        entity.createdBy = user.getCreatedBy();
        entity.updatedBy = user.getUpdatedBy();
        entity.version = user.getVersion();
        entity.deleted = user.getDeleted();
        
        return entity;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", createdBy=" + createdBy +
                ", updatedBy=" + updatedBy +
                ", version=" + version +
                ", deleted=" + deleted +
                '}';
    }
}
