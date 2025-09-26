package com.flowmaster.user.infrastructure.repository;

import com.flowmaster.user.domain.model.aggregate.User;
import com.flowmaster.user.domain.model.valueobject.Email;
import com.flowmaster.user.domain.model.valueobject.Phone;
import com.flowmaster.user.domain.model.valueobject.UserId;
import com.flowmaster.user.domain.model.valueobject.Username;
import com.flowmaster.user.domain.model.valueobject.UserStatus;
import com.flowmaster.user.domain.repository.UserRepository;
import com.flowmaster.user.infrastructure.persistence.entity.UserEntity;
import com.flowmaster.user.infrastructure.persistence.entity.UserProfileEntity;
import com.flowmaster.user.infrastructure.persistence.repository.UserJpaRepository;
import com.flowmaster.user.infrastructure.persistence.repository.UserProfileJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户仓储实现
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserProfileJpaRepository userProfileJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(UserId userId) {
        log.debug("根据用户ID查找用户: userId={}", userId.getValue());
        
        return userJpaRepository.findById(userId.getValue())
                .map(this::convertToDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(Username username) {
        log.debug("根据用户名查找用户: username={}", username.getValue());
        
        return userJpaRepository.findByUsernameAndDeletedFalse(username.getValue())
                .map(this::convertToDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(Email email) {
        log.debug("根据邮箱查找用户: email={}", email.getValue());
        
        return userJpaRepository.findByEmailAndDeletedFalse(email.getValue())
                .map(this::convertToDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByPhone(Phone phone) {
        log.debug("根据手机号查找用户: phone={}", phone.getValue());
        
        return userJpaRepository.findByPhoneAndDeletedFalse(phone.getValue())
                .map(this::convertToDomain);
    }

    @Override
    public User save(User user) {
        log.debug("保存用户: userId={}, username={}", user.getUserId().getValue(), user.getUsername().getValue());
        
        // 保存用户基本信息
        UserEntity userEntity = UserEntity.fromDomain(user);
        UserEntity savedUserEntity = userJpaRepository.save(userEntity);
        
        // 保存用户资料
        if (user.getProfile() != null) {
            UserProfileEntity profileEntity = UserProfileEntity.fromDomain(user.getProfile(), user.getUserId().getValue());
            userProfileJpaRepository.save(profileEntity);
        }
        
        // 转换为领域对象返回
        User savedUser = convertToDomain(savedUserEntity);
        
        log.info("用户保存成功: userId={}, username={}", savedUser.getUserId().getValue(), savedUser.getUsername().getValue());
        return savedUser;
    }

    @Override
    public void deleteById(UserId userId) {
        log.debug("删除用户: userId={}", userId.getValue());
        
        // 逻辑删除用户
        userJpaRepository.softDeleteById(
            userId.getValue(), 
            LocalDateTime.now(), 
            1L // TODO: 从认证上下文获取当前用户ID
        );
        
        // 逻辑删除用户资料
        userProfileJpaRepository.softDeleteByUserId(
            userId.getValue(), 
            LocalDateTime.now(), 
            1L // TODO: 从认证上下文获取当前用户ID
        );
        
        log.info("用户删除成功: userId={}", userId.getValue());
    }

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    @Transactional(readOnly = true)
    public boolean existsByUsername(Username username) {
        return userJpaRepository.existsByUsernameAndDeletedFalse(username.getValue());
    }

    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     * @return 是否存在
     */
    @Transactional(readOnly = true)
    public boolean existsByEmail(Email email) {
        return userJpaRepository.existsByEmailAndDeletedFalse(email.getValue());
    }

    /**
     * 检查手机号是否存在
     *
     * @param phone 手机号
     * @return 是否存在
     */
    @Transactional(readOnly = true)
    public boolean existsByPhone(Phone phone) {
        return userJpaRepository.existsByPhoneAndDeletedFalse(phone.getValue());
    }

    /**
     * 根据状态统计用户数量
     *
     * @param status 用户状态
     * @return 用户数量
     */
    @Transactional(readOnly = true)
    public long countByStatus(UserStatus status) {
        return userJpaRepository.countByStatus(status.name());
    }

    /**
     * 统计用户总数
     *
     * @return 用户总数
     */
    @Transactional(readOnly = true)
    public long count() {
        return userJpaRepository.countActiveUsers();
    }

    /**
     * 根据邮箱模糊查询用户
     *
     * @param email    邮箱关键字
     * @param pageable 分页参数
     * @return 用户分页结果
     */
    @Transactional(readOnly = true)
    public Page<User> findByEmailContaining(String email, Pageable pageable) {
        // TODO: 实现邮箱模糊查询逻辑
        return Page.empty();
    }

    /**
     * 根据用户名模糊查询用户
     *
     * @param username 用户名关键字
     * @param pageable 分页参数
     * @return 用户分页结果
     */
    @Transactional(readOnly = true)
    public Page<User> findByUsernameContaining(String username, Pageable pageable) {
        // TODO: 实现用户名模糊查询逻辑
        return Page.empty();
    }

    /**
     * 根据状态分页查询用户
     *
     * @param status   用户状态
     * @param pageable 分页参数
     * @return 用户分页结果
     */
    @Transactional(readOnly = true)
    public Page<User> findByStatus(UserStatus status, Pageable pageable) {
        // TODO: 实现状态分页查询逻辑
        return Page.empty();
    }

    /**
     * 分页查询用户
     *
     * @param pageable 分页参数
     * @return 用户分页结果
     */
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        // TODO: 实现分页查询逻辑
        return Page.empty();
    }

    /**
     * 根据状态查找用户列表
     *
     * @param status 用户状态
     * @return 用户列表
     */
    @Transactional(readOnly = true)
    public List<User> findByStatus(UserStatus status) {
        // TODO: 实现状态查询逻辑
        return List.of();
    }

    /**
     * 检查邮箱是否被其他用户使用
     *
     * @param email  邮箱
     * @param userId 用户ID
     * @return 是否存在
     */
    @Transactional(readOnly = true)
    public boolean existsByEmailAndUserIdNot(Email email, Long userId) {
        // TODO: 实现邮箱检查逻辑
        return false;
    }

    /**
     * 检查手机号是否被其他用户使用
     *
     * @param phone  手机号
     * @param userId 用户ID
     * @return 是否存在
     */
    @Transactional(readOnly = true)
    public boolean existsByPhoneAndUserIdNot(Phone phone, Long userId) {
        // TODO: 实现手机号检查逻辑
        return false;
    }

    /**
     * 将JPA实体转换为领域对象
     *
     * @param userEntity 用户JPA实体
     * @return 用户领域对象
     */
    private User convertToDomain(UserEntity userEntity) {
        User user = userEntity.toDomain();
        
        // 加载用户资料
        Optional<UserProfileEntity> profileEntity = userProfileJpaRepository.findByUserIdAndDeletedFalse(userEntity.getId());
        if (profileEntity.isPresent()) {
            user.setProfile(profileEntity.get().toDomain());
        }
        
        return user;
    }
}
