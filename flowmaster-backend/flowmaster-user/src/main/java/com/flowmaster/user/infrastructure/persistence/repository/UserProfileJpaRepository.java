package com.flowmaster.user.infrastructure.persistence.repository;

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
import java.util.Optional;

/**
 * 用户资料JPA Repository
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Repository
public interface UserProfileJpaRepository extends JpaRepository<UserProfileEntity, Long>, JpaSpecificationExecutor<UserProfileEntity> {

    /**
     * 根据用户ID查找用户资料
     *
     * @param userId 用户ID
     * @return 用户资料实体
     */
    Optional<UserProfileEntity> findByUserIdAndDeletedFalse(Long userId);

    /**
     * 根据昵称查找用户资料
     *
     * @param nickname 昵称
     * @param pageable 分页参数
     * @return 用户资料分页列表
     */
    Page<UserProfileEntity> findByNicknameContainingAndDeletedFalse(String nickname, Pageable pageable);

    /**
     * 根据真实姓名查找用户资料
     *
     * @param realName 真实姓名
     * @param pageable 分页参数
     * @return 用户资料分页列表
     */
    Page<UserProfileEntity> findByRealNameContainingAndDeletedFalse(String realName, Pageable pageable);

    /**
     * 根据性别查找用户资料
     *
     * @param gender 性别
     * @param pageable 分页参数
     * @return 用户资料分页列表
     */
    Page<UserProfileEntity> findByGenderAndDeletedFalse(String gender, Pageable pageable);

    /**
     * 检查昵称是否存在
     *
     * @param nickname 昵称
     * @return 是否存在
     */
    boolean existsByNicknameAndDeletedFalse(String nickname);

    /**
     * 逻辑删除用户资料
     *
     * @param userId 用户ID
     * @param updatedBy 更新人ID
     * @return 影响行数
     */
    @Modifying
    @Query("UPDATE UserProfileEntity up SET up.deleted = true, up.updatedAt = :updatedAt, up.updatedBy = :updatedBy WHERE up.userId = :userId")
    int softDeleteByUserId(@Param("userId") Long userId, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") Long updatedBy);

    /**
     * 恢复删除的用户资料
     *
     * @param userId 用户ID
     * @param updatedBy 更新人ID
     * @return 影响行数
     */
    @Modifying
    @Query("UPDATE UserProfileEntity up SET up.deleted = false, up.updatedAt = :updatedAt, up.updatedBy = :updatedBy WHERE up.userId = :userId")
    int restoreByUserId(@Param("userId") Long userId, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") Long updatedBy);
}
