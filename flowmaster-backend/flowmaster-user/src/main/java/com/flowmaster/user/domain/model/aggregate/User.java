package com.flowmaster.user.domain.model.aggregate;

import com.flowmaster.common.domain.BaseDomainEntity;
import com.flowmaster.user.domain.model.entity.UserProfile;
import com.flowmaster.user.domain.model.valueobject.UserId;
import com.flowmaster.user.domain.model.valueobject.Username;
import com.flowmaster.user.domain.model.valueobject.Email;
import com.flowmaster.user.domain.model.valueobject.Phone;
import com.flowmaster.user.domain.model.valueobject.Password;
import com.flowmaster.user.domain.model.valueobject.UserStatus;
import com.flowmaster.user.domain.model.event.UserCreatedEvent;
import com.flowmaster.user.domain.model.event.UserUpdatedEvent;
import com.flowmaster.user.domain.model.event.UserStatusChangedEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 用户聚合根
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Getter
@Setter
@Slf4j
public class User extends BaseDomainEntity {

    /**
     * 用户ID
     */
    private UserId userId;

    /**
     * 用户名
     */
    private Username username;

    /**
     * 邮箱
     */
    private Email email;

    /**
     * 手机号
     */
    private Phone phone;

    /**
     * 密码
     */
    private Password password;

    /**
     * 用户状态
     */
    private UserStatus status;

    /**
     * 用户资料
     */
    private UserProfile profile;

    /**
     * 领域事件列表
     */
    private final List<Object> domainEvents = new ArrayList<>();

    /**
     * 私有构造函数，用于JPA
     */
    protected User() {
        super();
    }

    /**
     * 创建新用户
     *
     * @param username 用户名
     * @param email    邮箱
     * @param phone    手机号
     * @param password 密码
     * @return 用户聚合根
     */
    public static User create(Username username, Email email, Phone phone, Password password) {
        User user = new User();
        user.userId = UserId.generate();
        user.username = username;
        user.email = email;
        user.phone = phone;
        user.password = password;
        user.status = UserStatus.ACTIVE;
        
        // 调用父类的创建方法
        user.create(user.userId.getValue());
        
        // 添加领域事件
        user.addDomainEvent(new UserCreatedEvent(user.userId, user.username, user.email));
        
        log.info("创建新用户: userId={}, username={}", user.userId.getValue(), user.username.getValue());
        return user;
    }

    /**
     * 更新用户基本信息
     *
     * @param email 邮箱
     * @param phone 手机号
     * @param updatedBy 更新人ID
     */
    public void updateBasicInfo(Email email, Phone phone, Long updatedBy) {
        this.email = email;
        this.phone = phone;
        
        // 调用父类的更新方法
        this.update(updatedBy);
        
        // 添加领域事件
        addDomainEvent(new UserUpdatedEvent(this.userId, this.username, this.email));
        
        log.info("更新用户基本信息: userId={}, email={}, phone={}", 
                this.userId.getValue(), email.getValue(), phone.getValue());
    }

    /**
     * 更新用户资料
     *
     * @param profile 用户资料
     * @param updatedBy 更新人ID
     */
    public void updateProfile(UserProfile profile, Long updatedBy) {
        this.profile = profile;
        
        // 调用父类的更新方法
        this.update(updatedBy);
        
        // 添加领域事件
        addDomainEvent(new UserUpdatedEvent(this.userId, this.username, this.email));
        
        log.info("更新用户资料: userId={}, profile={}", this.userId.getValue(), profile);
    }

    /**
     * 修改密码
     *
     * @param newPassword 新密码
     * @param updatedBy 更新人ID
     */
    public void changePassword(Password newPassword, Long updatedBy) {
        this.password = newPassword;
        
        // 调用父类的更新方法
        this.update(updatedBy);
        
        log.info("修改用户密码: userId={}", this.userId.getValue());
    }

    /**
     * 激活用户
     *
     * @param updatedBy 更新人ID
     */
    public void activate(Long updatedBy) {
        if (this.status == UserStatus.ACTIVE) {
            throw new IllegalStateException("用户已经是激活状态");
        }
        
        this.status = UserStatus.ACTIVE;
        
        // 调用父类的更新方法
        this.update(updatedBy);
        
        // 添加领域事件
        addDomainEvent(new UserStatusChangedEvent(this.userId, UserStatus.INACTIVE, UserStatus.ACTIVE));
        
        log.info("激活用户: userId={}", this.userId.getValue());
    }

    /**
     * 停用用户
     *
     * @param updatedBy 更新人ID
     */
    public void deactivate(Long updatedBy) {
        if (this.status == UserStatus.INACTIVE) {
            throw new IllegalStateException("用户已经是停用状态");
        }
        
        UserStatus oldStatus = this.status;
        this.status = UserStatus.INACTIVE;
        
        // 调用父类的更新方法
        this.update(updatedBy);
        
        // 添加领域事件
        addDomainEvent(new UserStatusChangedEvent(this.userId, oldStatus, UserStatus.INACTIVE));
        
        log.info("停用用户: userId={}", this.userId.getValue());
    }

    /**
     * 锁定用户
     *
     * @param updatedBy 更新人ID
     */
    public void lock(Long updatedBy) {
        if (this.status == UserStatus.LOCKED) {
            throw new IllegalStateException("用户已经是锁定状态");
        }
        
        UserStatus oldStatus = this.status;
        this.status = UserStatus.LOCKED;
        
        // 调用父类的更新方法
        this.update(updatedBy);
        
        // 添加领域事件
        addDomainEvent(new UserStatusChangedEvent(this.userId, oldStatus, UserStatus.LOCKED));
        
        log.info("锁定用户: userId={}", this.userId.getValue());
    }

    /**
     * 解锁用户
     *
     * @param updatedBy 更新人ID
     */
    public void unlock(Long updatedBy) {
        if (this.status != UserStatus.LOCKED) {
            throw new IllegalStateException("用户不是锁定状态");
        }
        
        this.status = UserStatus.ACTIVE;
        
        // 调用父类的更新方法
        this.update(updatedBy);
        
        // 添加领域事件
        addDomainEvent(new UserStatusChangedEvent(this.userId, UserStatus.LOCKED, UserStatus.ACTIVE));
        
        log.info("解锁用户: userId={}", this.userId.getValue());
    }

    /**
     * 验证密码
     *
     * @param inputPassword 输入密码
     * @return 是否匹配
     */
    public boolean verifyPassword(String inputPassword) {
        return this.password.matches(inputPassword);
    }

    /**
     * 检查用户是否可用
     *
     * @return 是否可用
     */
    public boolean isAvailable() {
        return this.status == UserStatus.ACTIVE;
    }

    /**
     * 添加领域事件
     *
     * @param event 领域事件
     */
    private void addDomainEvent(Object event) {
        this.domainEvents.add(event);
    }

    /**
     * 获取领域事件
     *
     * @return 领域事件列表
     */
    @DomainEvents
    public List<Object> getDomainEvents() {
        return Collections.unmodifiableList(this.domainEvents);
    }

    /**
     * 清除领域事件
     */
    @AfterDomainEventPublication
    public void clearDomainEvents() {
        this.domainEvents.clear();
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username=" + username +
                ", email=" + email +
                ", phone=" + phone +
                ", status=" + status +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }
}
