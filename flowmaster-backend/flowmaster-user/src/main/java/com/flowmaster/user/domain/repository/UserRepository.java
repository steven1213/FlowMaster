package com.flowmaster.user.domain.repository;

import com.flowmaster.user.domain.model.aggregate.User;
import com.flowmaster.user.domain.model.valueobject.Email;
import com.flowmaster.user.domain.model.valueobject.Phone;
import com.flowmaster.user.domain.model.valueobject.UserId;
import com.flowmaster.user.domain.model.valueobject.Username;
import com.flowmaster.user.domain.model.valueobject.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 用户领域仓储接口
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
public interface UserRepository {

    /**
     * 保存用户
     *
     * @param user 用户聚合根
     * @return 保存后的用户
     */
    User save(User user);

    /**
     * 根据ID查找用户
     *
     * @param userId 用户ID
     * @return 用户
     */
    Optional<User> findById(UserId userId);

    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return 用户
     */
    Optional<User> findByUsername(Username username);

    /**
     * 根据邮箱查找用户
     *
     * @param email 邮箱
     * @return 用户
     */
    Optional<User> findByEmail(Email email);

    /**
     * 根据手机号查找用户
     *
     * @param phone 手机号
     * @return 用户
     */
    Optional<User> findByPhone(Phone phone);

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(Username username);

    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     * @return 是否存在
     */
    boolean existsByEmail(Email email);

    /**
     * 检查手机号是否存在
     *
     * @param phone 手机号
     * @return 是否存在
     */
    boolean existsByPhone(Phone phone);

    /**
     * 检查邮箱是否被其他用户使用
     *
     * @param email  邮箱
     * @param userId 用户ID
     * @return 是否存在
     */
    boolean existsByEmailAndUserIdNot(Email email, Long userId);

    /**
     * 检查手机号是否被其他用户使用
     *
     * @param phone  手机号
     * @param userId 用户ID
     * @return 是否存在
     */
    boolean existsByPhoneAndUserIdNot(Phone phone, Long userId);

    /**
     * 根据状态查找用户列表
     *
     * @param status 用户状态
     * @return 用户列表
     */
    List<User> findByStatus(UserStatus status);

    /**
     * 分页查询用户
     *
     * @param pageable 分页参数
     * @return 用户分页结果
     */
    Page<User> findAll(Pageable pageable);

    /**
     * 根据状态分页查询用户
     *
     * @param status   用户状态
     * @param pageable 分页参数
     * @return 用户分页结果
     */
    Page<User> findByStatus(UserStatus status, Pageable pageable);

    /**
     * 根据用户名模糊查询用户
     *
     * @param username 用户名关键字
     * @param pageable 分页参数
     * @return 用户分页结果
     */
    Page<User> findByUsernameContaining(String username, Pageable pageable);

    /**
     * 根据邮箱模糊查询用户
     *
     * @param email    邮箱关键字
     * @param pageable 分页参数
     * @return 用户分页结果
     */
    Page<User> findByEmailContaining(String email, Pageable pageable);

    /**
     * 删除用户
     *
     * @param userId 用户ID
     */
    void deleteById(UserId userId);

    /**
     * 统计用户总数
     *
     * @return 用户总数
     */
    long count();

    /**
     * 根据状态统计用户数量
     *
     * @param status 用户状态
     * @return 用户数量
     */
    long countByStatus(UserStatus status);
}
