# FlowMaster Admin - 管理后台应用

FlowMaster Admin 是 FlowMaster 工作流管理系统的管理后台应用，提供完整的系统管理功能。

## 功能特性

### 🎯 核心功能
- **用户管理**: 用户账号管理、角色分配、权限控制
- **角色管理**: 角色定义、权限分配、用户关联
- **权限管理**: 权限树管理、菜单配置、API权限控制
- **工作流管理**: 工作流定义管理、实例监控、任务处理
- **审计日志**: 操作日志记录、审计追踪、日志导出
- **系统配置**: 系统参数配置、环境设置、功能开关
- **系统监控**: 性能监控、指标统计、告警管理

### 🎨 界面特性
- **现代化设计**: 基于 Ant Design 5.x 的现代化界面设计
- **响应式布局**: 支持桌面端和移动端适配
- **主题定制**: 支持主题切换和个性化配置
- **国际化**: 支持多语言切换
- **无障碍**: 符合 WCAG 2.1 无障碍标准

### 🔧 技术特性
- **TypeScript**: 完整的类型安全支持
- **React 18**: 最新的 React 特性和性能优化
- **Redux Toolkit**: 现代化的状态管理
- **React Query**: 强大的数据获取和缓存
- **Vite**: 快速的构建和开发体验
- **ESLint**: 代码质量检查和规范

## 技术栈

### 前端框架
- **React 18.2.0**: 用户界面框架
- **TypeScript 5.0.0**: 类型安全的 JavaScript
- **Vite 5.0.0**: 构建工具和开发服务器

### UI 组件库
- **Ant Design 5.12.0**: 企业级 UI 组件库
- **Ant Design Icons 5.2.0**: 图标组件库

### 状态管理
- **Redux Toolkit 2.0.0**: 状态管理工具
- **React Redux 9.0.0**: React Redux 绑定
- **Redux Persist 6.0.0**: 状态持久化

### 数据获取
- **React Query 3.39.0**: 数据获取和缓存
- **Axios 1.6.0**: HTTP 客户端

### 路由和导航
- **React Router DOM 6.20.0**: 客户端路由

### 图表和可视化
- **ECharts 5.4.0**: 图表库
- **ECharts for React 3.0.0**: React ECharts 组件

### 工具库
- **Day.js 1.11.0**: 日期处理
- **Lodash-es 4.17.0**: 实用工具库
- **Classnames 2.3.0**: CSS 类名处理

### 开发工具
- **ESLint**: 代码检查
- **TypeScript ESLint**: TypeScript 代码检查
- **Vitest**: 单元测试
- **@vitest/ui**: 测试界面

## 项目结构

```
flowmaster-admin/
├── public/                 # 静态资源
├── src/                    # 源代码
│   ├── components/         # 通用组件
│   │   └── AdminLayout.tsx # 管理后台布局
│   ├── pages/              # 页面组件
│   │   ├── LoginPage.tsx   # 登录页面
│   │   ├── DashboardPage.tsx # 仪表板
│   │   ├── UserManagementPage.tsx # 用户管理
│   │   ├── RoleManagementPage.tsx # 角色管理
│   │   ├── PermissionManagementPage.tsx # 权限管理
│   │   ├── WorkflowManagementPage.tsx # 工作流管理
│   │   ├── AuditLogPage.tsx # 审计日志
│   │   ├── SystemConfigPage.tsx # 系统配置
│   │   ├── MonitoringPage.tsx # 系统监控
│   │   └── ProfilePage.tsx # 个人资料
│   ├── store/              # Redux 状态管理
│   │   ├── index.ts        # Store 配置
│   │   └── slices/         # Redux Slices
│   │       ├── authSlice.ts # 认证状态
│   │       ├── userSlice.ts # 用户状态
│   │       ├── roleSlice.ts # 角色状态
│   │       ├── permissionSlice.ts # 权限状态
│   │       ├── workflowSlice.ts # 工作流状态
│   │       ├── auditSlice.ts # 审计状态
│   │       ├── configSlice.ts # 配置状态
│   │       └── dashboardSlice.ts # 仪表板状态
│   ├── services/           # API 服务
│   │   ├── apiClient.ts    # API 客户端
│   │   ├── authService.ts  # 认证服务
│   │   ├── userService.ts  # 用户服务
│   │   ├── roleService.ts  # 角色服务
│   │   ├── permissionService.ts # 权限服务
│   │   ├── workflowService.ts # 工作流服务
│   │   ├── auditService.ts # 审计服务
│   │   ├── configService.ts # 配置服务
│   │   └── dashboardService.ts # 仪表板服务
│   ├── hooks/              # 自定义 Hooks
│   │   └── useAuth.ts      # 认证 Hook
│   ├── types/              # 类型定义
│   │   └── index.ts        # 通用类型
│   ├── utils/              # 工具函数
│   ├── assets/             # 静态资源
│   ├── App.tsx             # 应用根组件
│   ├── main.tsx            # 应用入口
│   └── index.css           # 全局样式
├── package.json            # 项目配置
├── vite.config.ts         # Vite 配置
├── tsconfig.json          # TypeScript 配置
├── eslint.config.js       # ESLint 配置
└── README.md              # 项目说明
```

## 快速开始

### 环境要求
- Node.js >= 18.0.0
- npm >= 9.0.0

### 安装依赖
```bash
npm install
```

### 开发模式
```bash
npm run dev
```
应用将在 http://localhost:3003 启动

### 构建生产版本
```bash
npm run build
```

### 预览生产版本
```bash
npm run preview
```

### 代码检查
```bash
npm run lint
```

### 代码修复
```bash
npm run lint:fix
```

### 类型检查
```bash
npm run type-check
```

### 运行测试
```bash
npm run test
```

### 测试界面
```bash
npm run test:ui
```

## 开发指南

### 代码规范
- 使用 TypeScript 进行类型安全开发
- 遵循 ESLint 代码规范
- 使用 Prettier 进行代码格式化
- 组件使用函数式组件和 Hooks
- 使用 Redux Toolkit 进行状态管理

### 组件开发
- 组件放在 `src/components` 目录
- 页面组件放在 `src/pages` 目录
- 使用 Ant Design 组件库
- 遵循组件命名规范

### 状态管理
- 使用 Redux Toolkit 创建 Slice
- 异步操作使用 createAsyncThunk
- 状态持久化使用 Redux Persist
- 自定义 Hooks 封装状态逻辑

### API 集成
- API 服务放在 `src/services` 目录
- 使用 Axios 进行 HTTP 请求
- 统一错误处理和响应拦截
- 使用 React Query 进行数据缓存

### 路由配置
- 使用 React Router DOM 进行路由管理
- 路由配置在 `App.tsx` 中
- 支持路由守卫和权限控制

## 部署说明

### 构建配置
- 使用 Vite 进行构建
- 支持代码分割和懒加载
- 生产环境优化配置

### 环境变量
```bash
# API 基础地址
VITE_API_BASE_URL=http://localhost:8080

# 应用标题
VITE_APP_TITLE=FlowMaster Admin

# 版本号
VITE_APP_VERSION=1.0.0
```

### 部署步骤
1. 构建生产版本: `npm run build`
2. 将 `dist` 目录部署到 Web 服务器
3. 配置反向代理到后端 API
4. 配置 HTTPS 和安全头

## 功能模块

### 认证模块
- 用户登录/退出
- Token 管理和刷新
- 权限验证和路由守卫

### 用户管理
- 用户列表和搜索
- 用户创建和编辑
- 角色分配和权限管理
- 用户状态管理

### 角色管理
- 角色定义和管理
- 权限分配
- 用户关联管理

### 权限管理
- 权限树结构
- 菜单权限配置
- API 权限控制
- 数据权限管理

### 工作流管理
- 工作流定义管理
- 工作流实例监控
- 任务处理和分配
- 流程监控和统计

### 审计日志
- 操作日志记录
- 日志查询和过滤
- 日志导出和统计
- 安全审计追踪

### 系统配置
- 系统参数配置
- 环境设置管理
- 功能开关控制
- 配置版本管理

### 系统监控
- 性能指标监控
- 系统状态监控
- 告警和通知
- 监控图表展示

## 安全特性

### 认证安全
- JWT Token 认证
- Token 自动刷新
- 登录状态持久化
- 安全退出机制

### 权限控制
- 基于角色的访问控制 (RBAC)
- 基于属性的访问控制 (ABAC)
- 菜单权限控制
- API 权限验证

### 数据安全
- 敏感数据加密
- 输入验证和过滤
- XSS 防护
- CSRF 防护

### 审计安全
- 操作日志记录
- 安全事件追踪
- 异常行为监控
- 合规性审计

## 性能优化

### 前端优化
- 代码分割和懒加载
- 组件缓存和记忆化
- 虚拟滚动和分页
- 图片懒加载和压缩

### 网络优化
- API 请求缓存
- 请求去重和合并
- 离线数据支持
- 网络状态监控

### 渲染优化
- React 18 并发特性
- Suspense 和错误边界
- 组件优化和重构
- 内存泄漏防护

## 监控和调试

### 开发工具
- React DevTools
- Redux DevTools
- Vite DevTools
- ESLint 和 TypeScript

### 性能监控
- 页面加载时间
- 组件渲染时间
- API 响应时间
- 内存使用情况

### 错误监控
- 错误边界和捕获
- 错误日志记录
- 用户反馈收集
- 异常报告机制

## 更新日志

### v1.0.0 (2024-01-01)
- 初始版本发布
- 基础管理功能
- 用户和权限管理
- 系统配置和监控

## 贡献指南

1. Fork 项目
2. 创建功能分支
3. 提交更改
4. 推送到分支
5. 创建 Pull Request

## 许可证

MIT License

## 联系方式

- 项目主页: https://github.com/your-org/flowmaster
- 问题反馈: https://github.com/your-org/flowmaster/issues
- 邮箱: flowmaster@example.com

---

**FlowMaster Admin** - 让工作流管理更简单、更高效！
