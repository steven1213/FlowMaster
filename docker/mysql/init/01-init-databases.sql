-- FlowMaster 数据库初始化脚本
-- 创建数据库和用户

-- 创建 Nacos 数据库
CREATE DATABASE IF NOT EXISTS nacos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建 Nacos 用户
CREATE USER IF NOT EXISTS 'nacos'@'%' IDENTIFIED BY 'nacos123';
GRANT ALL PRIVILEGES ON nacos.* TO 'nacos'@'%';

-- 创建 FlowMaster 用户数据库
CREATE DATABASE IF NOT EXISTS flowmaster_user CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
GRANT ALL PRIVILEGES ON flowmaster_user.* TO 'flowmaster'@'%';

-- 创建 FlowMaster 认证数据库
CREATE DATABASE IF NOT EXISTS flowmaster_auth CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
GRANT ALL PRIVILEGES ON flowmaster_auth.* TO 'flowmaster'@'%';

-- 创建 FlowMaster 工作流数据库
CREATE DATABASE IF NOT EXISTS flowmaster_workflow CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
GRANT ALL PRIVILEGES ON flowmaster_workflow.* TO 'flowmaster'@'%';

-- 创建 FlowMaster 网关数据库
CREATE DATABASE IF NOT EXISTS flowmaster_gateway CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
GRANT ALL PRIVILEGES ON flowmaster_gateway.* TO 'flowmaster'@'%';

-- 创建 FlowMaster 监控数据库
CREATE DATABASE IF NOT EXISTS flowmaster_monitoring CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
GRANT ALL PRIVILEGES ON flowmaster_monitoring.* TO 'flowmaster'@'%';

-- 刷新权限
FLUSH PRIVILEGES;
