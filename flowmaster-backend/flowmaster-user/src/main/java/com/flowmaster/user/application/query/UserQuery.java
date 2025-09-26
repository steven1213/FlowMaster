package com.flowmaster.user.application.query;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户查询对象
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class UserQuery {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户状态
     */
    private String status;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 创建时间开始
     */
    private LocalDateTime createdAtStart;

    /**
     * 创建时间结束
     */
    private LocalDateTime createdAtEnd;

    /**
     * 更新时间开始
     */
    private LocalDateTime updatedAtStart;

    /**
     * 更新时间结束
     */
    private LocalDateTime updatedAtEnd;

    /**
     * 页码（从0开始）
     */
    private Integer page = 0;

    /**
     * 每页大小
     */
    private Integer size = 20;

    /**
     * 排序字段
     */
    private String sortBy = "createdAt";

    /**
     * 排序方向（asc/desc）
     */
    private String sortDirection = "desc";
}
