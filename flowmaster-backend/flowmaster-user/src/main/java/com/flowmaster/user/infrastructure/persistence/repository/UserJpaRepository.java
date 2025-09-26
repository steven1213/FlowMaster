package com.flowmaster.user.infrastructure.persistence.repository;

import com.flowmaster.user.infrastructure.persistence.entity.UserEntity;
import com.flowmaster.user.infrastructure.persistence.entity.UserProfileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户JPA Repository
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return 用户实体
     */
    Optional<UserEntity> findByUsernameAndDeletedFalse(String username);

    /**
     * 根据邮箱查找用户
     *
     * @param email 邮箱
     * @return 用户实体
     */
    Optional<UserEntity> findByEmailAndDeletedFalse(String email);

    /**
     * 根据手机号查找用户
     *
     * @param phone 手机号
     * @return 用户实体
     */
    Optional<UserEntity> findByPhoneAndDeletedFalse(String phone);

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsernameAndDeletedFalse(String username);

    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     * @return 是否存在
     */
    boolean existsByEmailAndDeletedFalse(String email);

    /**
     * 检查手机号是否存在
     *
     * @param phone 手机号
     * @return 是否存在
     */
    boolean existsByPhoneAndDeletedFalse(String phone);

    /**
     * 根据状态查找用户
     *
     * @param status 用户状态
     * @param pageable 分页参数
     * @return 用户分页列表
     */
    Page<UserEntity> findByStatusAndDeletedFalse(String status, Pageable pageable);

    /**
     * 根据创建时间范围查找用户
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param pageable 分页参数
     * @return 用户分页列表
     */
    Page<UserEntity> findByCreatedAtBetweenAndDeletedFalse(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    /**
     * 根据用户名模糊查询
     *
     * @param username 用户名关键字
     * @param pageable 分页参数
     * @return 用户分页列表
     */
    Page<UserEntity> findByUsernameContainingAndDeletedFalse(String username, Pageable pageable);

    /**
     * 逻辑删除用户
     *
     * @param id 用户ID
     * @param updatedBy 更新人ID
     * @return 影响行数
     */
    @Modifying
    @Query("UPDATE UserEntity u SET u.deleted = true, u.updatedAt = :updatedAt, u.updatedBy = :updatedBy WHERE u.id = :id")
    int softDeleteById(@Param("id") Long id, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") Long updatedBy);

    /**
     * 恢复删除的用户
     *
     * @param id 用户ID
     * @param updatedBy 更新人ID
     * @return 影响行数
     */
    @Modifying
    @Query("UPDATE UserEntity u SET u.deleted = false, u.updatedAt = :updatedAt, u.updatedBy = :updatedBy WHERE u.id = :id")
    int restoreById(@Param("id") Long id, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") Long updatedBy);

    /**
     * 更新用户状态
     *
     * @param id 用户ID
     * @param status 新状态
     * @param updatedBy 更新人ID
     * @return 影响行数
     */
    @Modifying
    @Query("UPDATE UserEntity u SET u.status = :status, u.updatedAt = :updatedAt, u.updatedBy = :updatedBy WHERE u.id = :id")
    int updateStatusById(@Param("id") Long id, @Param("status") String status, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") Long updatedBy);

    /**
     * 更新用户密码
     *
     * @param id 用户ID
     * @param passwordHash 新密码哈希
     * @param updatedBy 更新人ID
     * @return 影响行数
     */
    @Modifying
    @Query("UPDATE UserEntity u SET u.passwordHash = :passwordHash, u.updatedAt = :updatedAt, u.updatedBy = :updatedBy WHERE u.id = :id")
    int updatePasswordById(@Param("id") Long id, @Param("passwordHash") String passwordHash, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") Long updatedBy);

    /**
     * 批量更新用户状态
     *
     * @param ids 用户ID列表
     * @param status 新状态
     * @param updatedBy 更新人ID
     * @return 影响行数
     */
    @Modifying
    @Query("UPDATE UserEntity u SET u.status = :status, u.updatedAt = :updatedAt, u.updatedBy = :updatedBy WHERE u.id IN :ids")
    int batchUpdateStatusByIds(@Param("ids") List<Long> ids, @Param("status") String status, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") Long updatedBy);

    /**
     * 统计用户数量
     *
     * @return 用户总数
     */
    @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.deleted = false")
    long countActiveUsers();

    /**
     * 根据状态统计用户数量
     *
     * @param status 用户状态
     * @return 用户数量
     */
    @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.status = :status AND u.deleted = false")
    long countByStatus(@Param("status") String status);
}

