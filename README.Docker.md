# FlowMaster Docker 部署指南

FlowMaster 工作流管理系统 Docker 部署方案，支持快速部署和完整部署两种模式。

## 🚀 快速开始

### 环境要求
- Docker 20.10+
- Docker Compose 2.0+
- 至少 4GB 可用内存
- 至少 10GB 可用磁盘空间

### 一键部署
```bash
# 克隆项目
git clone https://github.com/your-org/flowmaster.git
cd flowmaster

# 快速启动
make -f Makefile.docker quick-start

# 查看状态
make -f Makefile.docker docker-status

# 健康检查
make -f Makefile.docker docker-health
```

## 📋 部署模式

### 1. 核心服务部署 (推荐)
包含核心业务功能，资源占用较少：

```bash
# 构建并启动核心服务
make -f Makefile.docker docker-build
make -f Makefile.docker docker-start

# 或者使用脚本
./docker-deploy.sh start
```

**包含服务：**
- MySQL 8.0 (数据库)
- Redis 7 (缓存)
- Nacos (服务注册中心)
- 5个后端微服务
- 3个前端应用

### 2. 完整服务部署
包含监控、日志、消息队列等完整功能：

```bash
# 构建并启动完整服务
make -f Makefile.docker docker-build
make -f Makefile.docker docker-start-full

# 或者使用脚本
./docker-deploy.sh start-full
```

**额外包含：**
- MongoDB (日志存储)
- Elasticsearch + Kibana (日志分析)
- Kafka + Zookeeper (消息队列)
- RabbitMQ (可靠消息)
- MinIO (对象存储)
- Zipkin (链路追踪)
- Prometheus + Grafana (监控)

## 🔧 管理命令

### 使用 Makefile
```bash
# 查看所有命令
make -f Makefile.docker help

# 构建镜像
make -f Makefile.docker docker-build

# 启动服务
make -f Makefile.docker docker-start

# 查看状态
make -f Makefile.docker docker-status

# 查看日志
make -f Makefile.docker docker-logs

# 健康检查
make -f Makefile.docker docker-health

# 停止服务
make -f Makefile.docker docker-stop

# 清理资源
make -f Makefile.docker docker-clean
```

### 使用部署脚本
```bash
# 查看帮助
./docker-deploy.sh help

# 构建镜像
./docker-deploy.sh build

# 启动核心服务
./docker-deploy.sh start

# 启动完整服务
./docker-deploy.sh start-full

# 查看状态
./docker-deploy.sh status

# 查看日志
./docker-deploy.sh logs
./docker-deploy.sh logs user  # 查看特定服务日志

# 健康检查
./docker-deploy.sh health-check

# 备份数据
./docker-deploy.sh backup

# 恢复数据
./docker-deploy.sh restore ./backups/20240101_120000

# 停止服务
./docker-deploy.sh stop

# 清理资源
./docker-deploy.sh cleanup
```

## 🌐 服务访问

### 应用服务
- **Web应用**: http://localhost:3000
- **管理后台**: http://localhost:3003
- **工作流设计器**: http://localhost:3001
- **API网关**: http://localhost:8083

### 基础设施服务
- **MySQL**: localhost:3306 (root/flowmaster123)
- **Redis**: localhost:6379 (flowmaster123)
- **Nacos控制台**: http://localhost:8848/nacos (nacos/nacos123)

### 监控服务 (完整部署)
- **Grafana**: http://localhost:3000 (admin/flowmaster123)
- **Prometheus**: http://localhost:9090
- **Zipkin**: http://localhost:9411
- **Kibana**: http://localhost:5601
- **RabbitMQ管理**: http://localhost:15672 (flowmaster/flowmaster123)
- **MinIO控制台**: http://localhost:9001 (flowmaster/flowmaster123)

## 📊 服务架构

### 核心服务架构
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Web应用       │    │   管理后台      │    │   工作流设计器   │
│   (3000)        │    │   (3003)        │    │   (3001)        │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                    ┌─────────────────┐
                    │   API网关       │
                    │   (8083)        │
                    └─────────────────┘
                                 │
         ┌───────────────────────┼───────────────────────┐
         │                       │                       │
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   用户服务      │    │   认证服务      │    │   工作流服务    │
│   (8080)        │    │   (8081)        │    │   (8082)        │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                    ┌─────────────────┐
                    │   监控服务      │
                    │   (8084)        │
                    └─────────────────┘
                                 │
         ┌───────────────────────┼───────────────────────┐
         │                       │                       │
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│     MySQL       │    │     Redis       │    │     Nacos       │
│   (3306)        │    │   (6379)        │    │   (8848)        │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### 完整服务架构
```
┌─────────────────────────────────────────────────────────────────┐
│                        前端应用层                                │
├─────────────────┬─────────────────┬─────────────────────────────┤
│   Web应用       │   管理后台      │   工作流设计器               │
│   (3000)        │   (3003)        │   (3001)                    │
└─────────────────┴─────────────────┴─────────────────────────────┘
                                 │
                    ┌─────────────────┐
                    │   API网关       │
                    │   (8083)        │
                    └─────────────────┘
                                 │
┌─────────────────────────────────────────────────────────────────┐
│                        微服务层                                 │
├─────────────────┬─────────────────┬─────────────────────────────┤
│   用户服务      │   认证服务      │   工作流服务                │
│   (8080)        │   (8081)        │   (8082)                    │
├─────────────────┼─────────────────┼─────────────────────────────┤
│   监控服务      │                 │                             │
│   (8084)        │                 │                             │
└─────────────────┴─────────────────┴─────────────────────────────┘
                                 │
┌─────────────────────────────────────────────────────────────────┐
│                        基础设施层                                │
├─────────────────┬─────────────────┬─────────────────────────────┤
│     MySQL       │     Redis       │     Nacos                   │
│   (3306)        │   (6379)        │   (8848)                    │
├─────────────────┼─────────────────┼─────────────────────────────┤
│    MongoDB      │ Elasticsearch    │     Kafka                   │
│   (27017)       │   (9200)        │   (9092)                    │
├─────────────────┼─────────────────┼─────────────────────────────┤
│    RabbitMQ     │     MinIO       │     Zipkin                  │
│   (5672)        │   (9000)        │   (9411)                    │
├─────────────────┼─────────────────┼─────────────────────────────┤
│   Prometheus    │    Grafana      │     Kibana                  │
│   (9090)        │   (3000)        │   (5601)                    │
└─────────────────┴─────────────────┴─────────────────────────────┘
```

## 🔧 配置说明

### 环境变量
所有服务都通过环境变量进行配置，主要配置项：

```bash
# 数据库配置
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/flowmaster_user
SPRING_DATASOURCE_USERNAME=flowmaster
SPRING_DATASOURCE_PASSWORD=flowmaster123

# Redis配置
SPRING_REDIS_HOST=redis
SPRING_REDIS_PORT=6379
SPRING_REDIS_PASSWORD=flowmaster123

# Nacos配置
SPRING_CLOUD_NACOS_DISCOVERY_SERVER_ADDR=nacos:8848
SPRING_CLOUD_NACOS_CONFIG_SERVER_ADDR=nacos:8848
SPRING_CLOUD_NACOS_USERNAME=nacos
SPRING_CLOUD_NACOS_PASSWORD=nacos123

# JWT配置
JWT_SECRET=FlowMasterJWTKey1234567890
```

### 端口映射
| 服务 | 端口 | 说明 |
|------|------|------|
| Web应用 | 3000 | 主应用前端 |
| 管理后台 | 3003 | 管理后台前端 |
| 工作流设计器 | 3001 | 工作流设计器前端 |
| 用户服务 | 8080 | 用户管理服务 |
| 认证服务 | 8081 | 认证授权服务 |
| 工作流服务 | 8082 | 工作流引擎服务 |
| API网关 | 8083 | 网关服务 |
| 监控服务 | 8084 | 监控服务 |
| MySQL | 3306 | 数据库 |
| Redis | 6379 | 缓存 |
| Nacos | 8848 | 服务注册中心 |

## 📈 监控和日志

### 健康检查
所有服务都配置了健康检查端点：
- 后端服务: `http://localhost:端口/actuator/health`
- 前端应用: `http://localhost:端口/`

### 日志查看
```bash
# 查看所有服务日志
make -f Makefile.docker docker-logs

# 查看特定服务日志
make -f Makefile.docker docker-logs-user
make -f Makefile.docker docker-logs-auth
make -f Makefile.docker docker-logs-workflow

# 使用脚本查看日志
./docker-deploy.sh logs
./docker-deploy.sh logs user
```

### 监控面板 (完整部署)
- **Grafana**: 系统监控面板
- **Prometheus**: 指标收集
- **Zipkin**: 链路追踪
- **Kibana**: 日志分析

## 🔒 安全配置

### 默认密码
| 服务 | 用户名 | 密码 |
|------|--------|------|
| MySQL | root | flowmaster123 |
| MySQL | flowmaster | flowmaster123 |
| Redis | - | flowmaster123 |
| Nacos | nacos | nacos123 |
| Grafana | admin | flowmaster123 |
| RabbitMQ | flowmaster | flowmaster123 |
| MinIO | flowmaster | flowmaster123 |

### 安全建议
1. **生产环境**: 修改所有默认密码
2. **网络安全**: 配置防火墙规则
3. **数据加密**: 启用TLS加密
4. **访问控制**: 限制管理端口访问

## 🚨 故障排除

### 常见问题

#### 1. 服务启动失败
```bash
# 检查Docker状态
docker ps -a

# 查看服务日志
docker-compose logs service-name

# 重启服务
docker-compose restart service-name
```

#### 2. 数据库连接失败
```bash
# 检查MySQL状态
docker exec flowmaster-mysql mysql -u root -pflowmaster123 -e "SHOW DATABASES;"

# 重启MySQL
docker-compose restart mysql
```

#### 3. 端口冲突
```bash
# 检查端口占用
netstat -tulpn | grep :3306

# 修改docker-compose.yml中的端口映射
```

#### 4. 内存不足
```bash
# 检查Docker资源使用
docker stats

# 清理未使用的资源
docker system prune -f
```

### 日志分析
```bash
# 查看错误日志
docker-compose logs | grep ERROR

# 查看特定时间段的日志
docker-compose logs --since="2024-01-01T00:00:00" --until="2024-01-01T23:59:59"
```

## 📦 数据管理

### 数据备份
```bash
# 备份数据库
make -f Makefile.docker db-backup

# 使用脚本备份
./docker-deploy.sh backup
```

### 数据恢复
```bash
# 恢复数据库
make -f Makefile.docker db-restore BACKUP_FILE=backups/mysql_backup_20240101_120000.sql

# 使用脚本恢复
./docker-deploy.sh restore ./backups/20240101_120000
```

### 数据持久化
所有重要数据都通过Docker volumes持久化：
- `mysql_data`: MySQL数据
- `redis_data`: Redis数据
- `nacos_data`: Nacos配置数据
- `mongodb_data`: MongoDB数据
- `elasticsearch_data`: Elasticsearch数据
- `prometheus_data`: Prometheus数据
- `grafana_data`: Grafana配置数据

## 🔄 更新和维护

### 更新服务
```bash
# 停止服务
make -f Makefile.docker docker-stop

# 重新构建镜像
make -f Makefile.docker docker-build

# 启动服务
make -f Makefile.docker docker-start
```

### 清理维护
```bash
# 清理未使用的镜像
docker image prune -f

# 清理未使用的容器
docker container prune -f

# 清理未使用的网络
docker network prune -f

# 清理未使用的卷
docker volume prune -f

# 全面清理
docker system prune -a -f
```

## 📞 技术支持

### 获取帮助
- 查看日志: `make -f Makefile.docker docker-logs`
- 健康检查: `make -f Makefile.docker docker-health`
- 服务状态: `make -f Makefile.docker docker-status`

### 联系方式
- 项目主页: https://github.com/your-org/flowmaster
- 问题反馈: https://github.com/your-org/flowmaster/issues
- 邮箱: flowmaster@example.com

---

**FlowMaster Docker 部署** - 让工作流管理更简单、更高效！ 🚀
