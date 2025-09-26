# FlowMaster 项目结构说明

## 📁 项目结构

```
flowmaster/
├── README.md                           # 项目主文档
│
├── flowmaster-backend/                # 后端微服务 (Java Maven项目)
│   ├── pom.xml                        # 后端父POM (独立管理)
│   ├── flowmaster-common/             # 公共组件库
│   ├── flowmaster-gateway/            # API网关服务
│   ├── flowmaster-auth/               # 认证授权服务
│   ├── flowmaster-user/               # 用户管理服务
│   ├── flowmaster-workflow/           # 工作流引擎服务
│   ├── flowmaster-approval/           # 审批业务服务
│   ├── flowmaster-notification/        # 通知服务
│   ├── flowmaster-integration/        # 集成服务
│   ├── flowmaster-file/               # 文件服务
│   ├── flowmaster-search/             # 搜索服务
│   └── flowmaster-monitor/            # 监控服务
│
├── flowmaster-frontend/               # 前端应用 (独立管理)
│   ├── package.json                   # 前端项目根package.json
│   ├── flowmaster-web/                # Web端应用
│   │   ├── package.json
│   │   ├── vite.config.ts
│   │   └── src/
│   ├── flowmaster-mobile/             # 移动端应用
│   │   ├── package.json
│   │   └── src/
│   ├── flowmaster-designer/           # 流程设计器
│   │   ├── package.json
│   │   └── src/
│   └── flowmaster-admin/              # 管理后台
│       ├── package.json
│       └── src/
│
├── flowmaster-infrastructure/         # 基础设施 (Java Maven项目)
│   ├── pom.xml
│   ├── docker/                        # Docker配置
│   ├── k8s/                          # Kubernetes配置
│   └── helm/                         # Helm Charts
│
├── flowmaster-sdk/                   # SDK工具 (Java Maven项目)
│   ├── pom.xml
│   ├── java-sdk/                     # Java SDK
│   ├── node-sdk/                     # Node.js SDK
│   └── python-sdk/                   # Python SDK
│
├── flowmaster-database/              # 数据库脚本
│   ├── mysql/                        # MySQL脚本
│   ├── redis/                        # Redis配置
│   └── elasticsearch/                # Elasticsearch配置
│
├── flowmaster-tests/                 # 测试相关
│   ├── unit-tests/                   # 单元测试
│   ├── integration-tests/            # 集成测试
│   ├── e2e-tests/                    # 端到端测试
│   └── load-tests/                   # 负载测试
│
├── scripts/                          # 项目脚本
│   ├── docker-compose.dev.yml        # 开发环境Docker Compose
│   ├── docker-compose.prod.yml       # 生产环境Docker Compose
│   ├── env.example                   # 环境变量示例
│   ├── start.sh                      # Linux启动脚本
│   ├── start.bat                     # Windows启动脚本
│   ├── build.sh                      # 构建脚本
│   └── deploy.sh                     # 部署脚本
│
├── docs/                             # 项目文档
│   ├── README.md                     # 项目主文档
│   ├── DEVELOPMENT_PLAN.md            # 开发计划
│   ├── DEVELOPMENT_STORIES.md        # 开发Story
│   ├── SPRINT_PLAN.md                # Sprint计划
│   ├── TASK_BREAKDOWN.md             # 任务分解
│   ├── PROJECT_OVERVIEW.md           # 项目概览
│   ├── ARCHITECTURE_DESIGN.md        # 架构设计
│   ├── ARCHITECTURE_BEST_PRACTICES.md # 架构最佳实践
│   ├── api/                          # API文档
│   ├── architecture/                 # 架构文档
│   ├── development/                   # 开发文档
│   └── deployment/                   # 部署文档
│
└── .github/                          # GitHub配置
    └── workflows/                    # CI/CD工作流
        └── ci-cd.yml                 # CI/CD配置
```

## 🎯 项目管理策略

### 后端项目 (Java Maven)
- **管理方式**: Maven多模块项目
- **父POM**: `flowmaster-backend/pom.xml` (独立管理)
- **子模块**: 所有后端微服务模块
- **构建工具**: Maven
- **语言**: Java 17

### 前端项目 (Node.js)
- **管理方式**: 独立管理，每个应用有自己的package.json
- **构建工具**: Vite, Webpack
- **语言**: TypeScript, JavaScript
- **包管理**: npm/yarn

### 基础设施
- **管理方式**: Maven项目
- **内容**: Docker, Kubernetes, Helm配置
- **构建**: Maven (用于Java相关配置)

### SDK工具
- **管理方式**: Maven项目
- **内容**: Java SDK, Node.js SDK, Python SDK
- **构建**: Maven (Java部分), npm (Node.js部分)

## 🔧 开发工作流

### 后端开发
```bash
# 构建所有后端模块
cd flowmaster-backend
mvn clean install

# 构建特定模块
cd flowmaster-backend/flowmaster-user
mvn clean install
```

### 前端开发
```bash
# 安装依赖
cd flowmaster-frontend/flowmaster-web
npm install

# 启动开发服务器
npm run dev
```

### 全栈开发
```bash
# 启动基础设施
cd scripts
docker-compose -f docker-compose.dev.yml up -d

# 启动后端服务
cd flowmaster-backend
mvn spring-boot:run

# 启动前端应用
cd flowmaster-frontend/flowmaster-web
npm run dev
```

## 📋 项目特点

1. **分离关注点**: 前端和后端完全分离管理
2. **独立构建**: 前端和后端可以独立构建和部署
3. **技术栈适配**: 每个技术栈使用最适合的构建工具
4. **统一文档**: 所有文档集中在docs目录
5. **脚本集中**: 所有脚本集中在scripts目录
6. **配置分离**: 不同环境的配置分离管理

## 🚀 快速开始

### 1. 环境准备
- Java 17+
- Node.js 18+
- Docker & Docker Compose
- Maven 3.8+

### 2. 启动项目
```bash
# Linux/Mac
cd scripts
chmod +x start.sh
./start.sh

# Windows
cd scripts
start.bat
```

### 3. 访问应用
- 后端API: http://localhost:8080
- 前端Web: http://localhost:3000
- 数据库: localhost:3306
- Redis: localhost:6379
- Elasticsearch: http://localhost:9200
- Kibana: http://localhost:5601
- Kafka: localhost:9092
- RabbitMQ: http://localhost:15672
- MinIO: http://localhost:9000

## 📝 注意事项

1. **后端独立管理**: 后端项目使用独立的Maven父POM管理，位于`flowmaster-backend/pom.xml`
2. **前端独立管理**: 前端项目使用npm workspace管理，有自己的package.json
3. **Docker配置**: 开发和生产环境使用不同的Docker Compose文件
4. **环境变量**: 使用`.env`文件管理环境变量，不要提交到版本控制
5. **文档管理**: 所有文档集中在`docs`目录，便于维护和查找
6. **构建分离**: 前后端使用不同的构建工具，避免冲突