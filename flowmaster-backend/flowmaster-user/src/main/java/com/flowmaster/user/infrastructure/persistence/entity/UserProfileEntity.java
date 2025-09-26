package com.flowmaster.user.infrastructure.persistence.entity;

import com.flowmaster.user.domain.model.entity.UserProfile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户资料JPA实体
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Entity
@Table(name = "user_profiles", indexes = {
    @Index(name = "idx_user_profiles_user_id", columnList = "user_id", unique = true),
    @Index(name = "idx_user_profiles_nickname", columnList = "nickname"),
    @Index(name = "idx_user_profiles_real_name", columnList = "real_name"),
    @Index(name = "idx_user_profiles_created_at", columnList = "created_at"),
    @Index(name = "idx_user_profiles_updated_at", columnList = "updated_at")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class UserProfileEntity {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 昵称
     */
    @Column(name = "nickname", length = 50)
    private String nickname;

    /**
     * 真实姓名
     */
    @Column(name = "real_name", length = 50)
    private String realName;

    /**
     * 头像URL
     */
    @Column(name = "avatar", length = 500)
    private String avatar;

    /**
     * 性别
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 20)
    private UserProfile.Gender gender;

    /**
     * 生日
     */
    @Column(name = "birthday")
    private LocalDate birthday;

    /**
     * 个人简介
     */
    @Column(name = "bio", length = 1000)
    private String bio;

    /**
     * 地址
     */
    @Column(name = "address", length = 200)
    private String address;

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
     * 关联的用户实体
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UserEntity user;

    /**
     * 默认构造函数
     */
    public UserProfileEntity() {
    }

    /**
     * 构造函数
     */
    public UserProfileEntity(Long userId, String nickname, String realName, 
                           String avatar, UserProfile.Gender gender, LocalDate birthday, 
                           String bio, String address) {
        this.userId = userId;
        this.nickname = nickname;
        this.realName = realName;
        this.avatar = avatar;
        this.gender = gender;
        this.birthday = birthday;
        this.bio = bio;
        this.address = address;
        this.deleted = false;
    }

    /**
     * 转换为领域对象
     */
    public UserProfile toDomain() {
        UserProfile profile = UserProfile.create(this.nickname, this.realName, this.createdBy);
        
        // 设置其他属性
        profile.setAvatar(this.avatar);
        profile.setGender(this.gender);
        profile.setBirthday(this.birthday);
        profile.setBio(this.bio);
        profile.setAddress(this.address);
        
        // 设置基础属性
        profile.setCreatedAt(this.createdAt);
        profile.setUpdatedAt(this.updatedAt);
        profile.setCreatedBy(this.createdBy);
        profile.setUpdatedBy(this.updatedBy);
        profile.setVersion(this.version);
        profile.setDeleted(this.deleted);
        
        return profile;
    }

    /**
     * 从领域对象创建
     */
    public static UserProfileEntity fromDomain(UserProfile profile, Long userId) {
        UserProfileEntity entity = new UserProfileEntity();
        entity.userId = userId;
        entity.nickname = profile.getNickname();
        entity.realName = profile.getRealName();
        entity.avatar = profile.getAvatar();
        entity.gender = profile.getGender();
        entity.birthday = profile.getBirthday();
        entity.bio = profile.getBio();
        entity.address = profile.getAddress();
        entity.createdAt = profile.getCreatedAt();
        entity.updatedAt = profile.getUpdatedAt();
        entity.createdBy = profile.getCreatedBy();
        entity.updatedBy = profile.getUpdatedBy();
        entity.version = profile.getVersion();
        entity.deleted = profile.getDeleted();
        
        return entity;
    }

    @Override
    public String toString() {
        return "UserProfileEntity{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", realName='" + realName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", gender=" + gender +
                ", birthday=" + birthday +
                ", bio='" + bio + '\'' +
                ", address='" + address + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", createdBy=" + createdBy +
                ", updatedBy=" + updatedBy +
                ", version=" + version +
                ", deleted=" + deleted +
                '}';
    }
}
