package com.flowmaster.user.domain.service;

import com.flowmaster.user.domain.model.aggregate.User;
import com.flowmaster.user.domain.model.valueobject.Email;
import com.flowmaster.user.domain.model.valueobject.Phone;
import com.flowmaster.user.domain.model.valueobject.Username;
import com.flowmaster.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户领域服务
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserDomainService {

    private final UserRepository userRepository;

    /**
     * 检查用户名是否唯一
     *
     * @param username 用户名
     * @return 是否唯一
     */
    public boolean isUsernameUnique(Username username) {
        boolean exists = userRepository.existsByUsername(username);
        log.debug("检查用户名唯一性: username={}, exists={}", username.getValue(), exists);
        return !exists;
    }

    /**
     * 检查邮箱是否唯一
     *
     * @param email 邮箱
     * @return 是否唯一
     */
    public boolean isEmailUnique(Email email) {
        boolean exists = userRepository.existsByEmail(email);
        log.debug("检查邮箱唯一性: email={}, exists={}", email.getValue(), exists);
        return !exists;
    }

    /**
     * 检查手机号是否唯一
     *
     * @param phone 手机号
     * @return 是否唯一
     */
    public boolean isPhoneUnique(Phone phone) {
        boolean exists = userRepository.existsByPhone(phone);
        log.debug("检查手机号唯一性: phone={}, exists={}", phone.getValue(), exists);
        return !exists;
    }

    /**
     * 验证用户创建条件
     *
     * @param username 用户名
     * @param email    邮箱
     * @param phone    手机号
     * @throws IllegalArgumentException 如果验证失败
     */
    public void validateUserCreation(Username username, Email email, Phone phone) {
        if (!isUsernameUnique(username)) {
            throw new IllegalArgumentException("用户名已存在: " + username.getValue());
        }
        
        if (!isEmailUnique(email)) {
            throw new IllegalArgumentException("邮箱已存在: " + email.getValue());
        }
        
        if (!isPhoneUnique(phone)) {
            throw new IllegalArgumentException("手机号已存在: " + phone.getValue());
        }
        
        log.info("用户创建条件验证通过: username={}, email={}, phone={}", 
                username.getValue(), email.getValue(), phone.getValue());
    }

    /**
     * 验证用户更新条件
     *
     * @param userId 用户ID
     * @param email  邮箱
     * @param phone  手机号
     * @throws IllegalArgumentException 如果验证失败
     */
    public void validateUserUpdate(Long userId, Email email, Phone phone) {
        // 检查邮箱是否被其他用户使用
        if (userRepository.existsByEmailAndUserIdNot(email, userId)) {
            throw new IllegalArgumentException("邮箱已被其他用户使用: " + email.getValue());
        }
        
        // 检查手机号是否被其他用户使用
        if (userRepository.existsByPhoneAndUserIdNot(phone, userId)) {
            throw new IllegalArgumentException("手机号已被其他用户使用: " + phone.getValue());
        }
        
        log.info("用户更新条件验证通过: userId={}, email={}, phone={}", 
                userId, email.getValue(), phone.getValue());
    }

    /**
     * 检查用户是否可以登录
     *
     * @param user 用户
     * @return 是否可以登录
     */
    public boolean canUserLogin(User user) {
        boolean canLogin = user.isAvailable();
        log.debug("检查用户登录权限: userId={}, canLogin={}", 
                user.getUserId().getValue(), canLogin);
        return canLogin;
    }

    /**
     * 检查用户是否可以执行操作
     *
     * @param user 用户
     * @return 是否可以执行操作
     */
    public boolean canUserOperate(User user) {
        boolean canOperate = user.getStatus().canOperate();
        log.debug("检查用户操作权限: userId={}, canOperate={}", 
                user.getUserId().getValue(), canOperate);
        return canOperate;
    }
}
