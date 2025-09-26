-- FlowMaster数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS flowmaster DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE flowmaster;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    email VARCHAR(100) NOT NULL UNIQUE COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    real_name VARCHAR(50) COMMENT '真实姓名',
    avatar VARCHAR(255) COMMENT '头像URL',
    status TINYINT DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    department_id BIGINT COMMENT '部门ID',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by BIGINT COMMENT '创建人',
    updated_by BIGINT COMMENT '更新人',
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_phone (phone),
    INDEX idx_department_id (department_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 部门表
CREATE TABLE IF NOT EXISTS departments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '部门ID',
    name VARCHAR(100) NOT NULL COMMENT '部门名称',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '部门编码',
    parent_id BIGINT COMMENT '父部门ID',
    level INT DEFAULT 1 COMMENT '部门层级',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by BIGINT COMMENT '创建人',
    updated_by BIGINT COMMENT '更新人',
    INDEX idx_code (code),
    INDEX idx_parent_id (parent_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门表';

-- 角色表
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    name VARCHAR(50) NOT NULL COMMENT '角色名称',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码',
    description VARCHAR(255) COMMENT '角色描述',
    status TINYINT DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by BIGINT COMMENT '创建人',
    updated_by BIGINT COMMENT '更新人',
    INDEX idx_code (code),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 权限表
CREATE TABLE IF NOT EXISTS permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '权限ID',
    name VARCHAR(50) NOT NULL COMMENT '权限名称',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '权限编码',
    type TINYINT DEFAULT 1 COMMENT '权限类型：1-菜单，2-按钮，3-接口',
    parent_id BIGINT COMMENT '父权限ID',
    path VARCHAR(255) COMMENT '权限路径',
    method VARCHAR(10) COMMENT 'HTTP方法',
    description VARCHAR(255) COMMENT '权限描述',
    status TINYINT DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by BIGINT COMMENT '创建人',
    updated_by BIGINT COMMENT '更新人',
    INDEX idx_code (code),
    INDEX idx_parent_id (parent_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS user_roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    created_by BIGINT COMMENT '创建人',
    UNIQUE KEY uk_user_role (user_id, role_id),
    INDEX idx_user_id (user_id),
    INDEX idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS role_permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    created_by BIGINT COMMENT '创建人',
    UNIQUE KEY uk_role_permission (role_id, permission_id),
    INDEX idx_role_id (role_id),
    INDEX idx_permission_id (permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- 流程定义表
CREATE TABLE IF NOT EXISTS process_definitions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '流程定义ID',
    name VARCHAR(100) NOT NULL COMMENT '流程名称',
    key VARCHAR(50) NOT NULL UNIQUE COMMENT '流程标识',
    version INT DEFAULT 1 COMMENT '版本号',
    description TEXT COMMENT '流程描述',
    category VARCHAR(50) COMMENT '流程分类',
    bpmn_xml LONGTEXT COMMENT 'BPMN XML内容',
    status TINYINT DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by BIGINT COMMENT '创建人',
    updated_by BIGINT COMMENT '更新人',
    INDEX idx_key (key),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='流程定义表';

-- 流程实例表
CREATE TABLE IF NOT EXISTS process_instances (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '流程实例ID',
    process_definition_id BIGINT NOT NULL COMMENT '流程定义ID',
    business_key VARCHAR(100) COMMENT '业务键',
    name VARCHAR(100) COMMENT '实例名称',
    status VARCHAR(20) DEFAULT 'RUNNING' COMMENT '状态：RUNNING-运行中，COMPLETED-已完成，SUSPENDED-暂停，CANCELLED-取消',
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
    end_time TIMESTAMP NULL COMMENT '结束时间',
    start_user_id BIGINT COMMENT '启动用户ID',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_process_definition_id (process_definition_id),
    INDEX idx_business_key (business_key),
    INDEX idx_status (status),
    INDEX idx_start_user_id (start_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='流程实例表';

-- 任务表
CREATE TABLE IF NOT EXISTS tasks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '任务ID',
    process_instance_id BIGINT NOT NULL COMMENT '流程实例ID',
    name VARCHAR(100) NOT NULL COMMENT '任务名称',
    assignee BIGINT COMMENT '指派人ID',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态：PENDING-待处理，COMPLETED-已完成，CANCELLED-取消',
    due_date TIMESTAMP NULL COMMENT '到期时间',
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
    end_time TIMESTAMP NULL COMMENT '结束时间',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_process_instance_id (process_instance_id),
    INDEX idx_assignee (assignee),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务表';

-- 审批规则表
CREATE TABLE IF NOT EXISTS approval_rules (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '审批规则ID',
    name VARCHAR(100) NOT NULL COMMENT '规则名称',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '规则编码',
    conditions JSON COMMENT '规则条件',
    actions JSON COMMENT '规则动作',
    priority INT DEFAULT 0 COMMENT '优先级',
    status TINYINT DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by BIGINT COMMENT '创建人',
    updated_by BIGINT COMMENT '更新人',
    INDEX idx_code (code),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='审批规则表';

-- 审计日志表
CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    user_id BIGINT COMMENT '用户ID',
    operation VARCHAR(100) NOT NULL COMMENT '操作类型',
    resource_type VARCHAR(50) COMMENT '资源类型',
    resource_id VARCHAR(100) COMMENT '资源ID',
    details TEXT COMMENT '详细信息',
    ip_address VARCHAR(50) COMMENT 'IP地址',
    user_agent TEXT COMMENT '用户代理',
    result VARCHAR(20) DEFAULT 'SUCCESS' COMMENT '结果：SUCCESS-成功，FAILED-失败',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_operation (operation),
    INDEX idx_resource_type (resource_type),
    INDEX idx_result (result),
    INDEX idx_created_time (created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='审计日志表';

-- 插入初始数据
-- 插入默认部门
INSERT INTO departments (id, name, code, parent_id, level, sort_order, status) VALUES
(1, '总公司', 'ROOT', NULL, 1, 1, 1),
(2, '技术部', 'TECH', 1, 2, 1, 1),
(3, '人事部', 'HR', 1, 2, 2, 1),
(4, '财务部', 'FINANCE', 1, 2, 3, 1);

-- 插入默认角色
INSERT INTO roles (id, name, code, description, status) VALUES
(1, '超级管理员', 'SUPER_ADMIN', '系统超级管理员', 1),
(2, '管理员', 'ADMIN', '系统管理员', 1),
(3, '普通用户', 'USER', '普通用户', 1),
(4, '审批员', 'APPROVER', '审批员', 1);

-- 插入默认权限
INSERT INTO permissions (id, name, code, type, parent_id, path, method, description, status) VALUES
(1, '系统管理', 'SYSTEM_MANAGE', 1, NULL, '/system', 'GET', '系统管理菜单', 1),
(2, '用户管理', 'USER_MANAGE', 1, 1, '/system/users', 'GET', '用户管理', 1),
(3, '角色管理', 'ROLE_MANAGE', 1, 1, '/system/roles', 'GET', '角色管理', 1),
(4, '权限管理', 'PERMISSION_MANAGE', 1, 1, '/system/permissions', 'GET', '权限管理', 1),
(5, '工作流管理', 'WORKFLOW_MANAGE', 1, NULL, '/workflow', 'GET', '工作流管理菜单', 1),
(6, '流程设计', 'PROCESS_DESIGN', 1, 5, '/workflow/design', 'GET', '流程设计', 1),
(7, '流程实例', 'PROCESS_INSTANCE', 1, 5, '/workflow/instances', 'GET', '流程实例', 1),
(8, '任务管理', 'TASK_MANAGE', 1, 5, '/workflow/tasks', 'GET', '任务管理', 1);

-- 插入默认用户
INSERT INTO users (id, username, password, email, phone, real_name, status, department_id) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKyVqjJ8Kj8Kj8Kj8Kj8Kj8Kj8Kj', 'admin@flowmaster.com', '13800138000', '系统管理员', 1, 1);

-- 插入用户角色关联
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);

-- 插入角色权限关联
INSERT INTO role_permissions (role_id, permission_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8);
