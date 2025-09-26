# FlowMaster Web 前端应用

FlowMaster Web应用是基于React 18 + TypeScript + Vite构建的现代化工作流管理系统前端。

## 技术栈

- **React 18** - 用户界面库
- **TypeScript 5** - 类型安全的JavaScript
- **Vite 5** - 快速构建工具
- **Ant Design 5** - UI组件库
- **Redux Toolkit** - 状态管理
- **React Router 6** - 路由管理
- **Axios** - HTTP客户端
- **ECharts** - 图表库
- **AntV G6** - 图可视化

## 项目结构

```
flowmaster-web/
├── public/                 # 静态资源
├── src/
│   ├── components/        # 通用组件
│   │   ├── AppLayout.tsx  # 应用布局
│   │   └── LoadingSpinner.tsx
│   ├── pages/            # 页面组件
│   │   ├── LoginPage.tsx
│   │   ├── DashboardPage.tsx
│   │   ├── WorkflowPage.tsx
│   │   ├── ApprovalPage.tsx
│   │   ├── MonitorPage.tsx
│   │   └── ProfilePage.tsx
│   ├── hooks/            # 自定义Hooks
│   │   └── useAuth.ts
│   ├── services/         # API服务
│   │   ├── apiClient.ts
│   │   └── authService.ts
│   ├── store/           # Redux状态管理
│   │   ├── index.ts
│   │   └── slices/
│   ├── types/           # TypeScript类型定义
│   │   └── index.ts
│   ├── styles/          # 样式文件
│   │   └── index.css
│   ├── App.tsx          # 主应用组件
│   └── main.tsx         # 应用入口
├── package.json
├── vite.config.ts
├── tsconfig.json
└── README.md
```

## 功能特性

### ✅ 已实现功能
- **用户认证系统** - 登录/登出，token管理
- **响应式布局** - 适配桌面和移动端
- **路由管理** - 基于React Router的SPA路由
- **状态管理** - Redux Toolkit + Redux Persist
- **API集成** - Axios + 拦截器 + 错误处理
- **UI组件** - Ant Design组件库
- **仪表盘** - 基础数据展示

### 🚧 开发中功能
- **工作流管理** - 工作流定义、实例管理
- **审批流程** - 审批任务处理
- **系统监控** - 实时监控数据展示
- **个人资料** - 用户信息管理

## 开发指南

### 环境要求
- Node.js >= 18.0.0
- npm >= 9.0.0

### 安装依赖
```bash
# 在flowmaster-frontend目录下
npm run install:web

# 或者直接进入web目录
cd flowmaster-web
npm install
```

### 开发模式
```bash
# 启动开发服务器
npm run dev

# 访问 http://localhost:3000
```

### 构建生产版本
```bash
npm run build
```

### 代码检查
```bash
# ESLint检查
npm run lint

# ESLint自动修复
npm run lint:fix

# TypeScript类型检查
npm run type-check
```

### 测试
```bash
# 运行测试
npm run test

# 测试UI界面
npm run test:ui
```

## API集成

### 后端服务地址
- 开发环境: `http://localhost:8080`
- 生产环境: 通过环境变量配置

### 认证流程
1. 用户登录获取JWT token
2. Token存储在localStorage中
3. 每次请求自动添加Authorization header
4. Token过期自动刷新或跳转登录

### API响应格式
```typescript
interface ApiResponse<T> {
  code: number
  message: string
  data: T
  timestamp: string
  traceId: string
}
```

## 部署说明

### 构建优化
- 代码分割 - 按路由和功能模块分割
- 资源压缩 - CSS/JS/图片自动压缩
- 缓存策略 - 静态资源长期缓存
- CDN支持 - 可配置CDN加速

### 环境变量
```bash
# .env.development
VITE_API_BASE_URL=http://localhost:8080
VITE_APP_TITLE=FlowMaster

# .env.production
VITE_API_BASE_URL=https://api.flowmaster.com
VITE_APP_TITLE=FlowMaster
```

## 开发规范

### 代码风格
- 使用TypeScript严格模式
- 遵循ESLint规则
- 组件使用函数式组件 + Hooks
- 状态管理使用Redux Toolkit

### 命名规范
- 组件文件: PascalCase (如: `UserProfile.tsx`)
- Hook文件: camelCase (如: `useAuth.ts`)
- 服务文件: camelCase (如: `authService.ts`)
- 类型文件: camelCase (如: `userTypes.ts`)

### Git提交规范
```
feat: 新功能
fix: 修复bug
docs: 文档更新
style: 代码格式调整
refactor: 代码重构
test: 测试相关
chore: 构建/工具相关
```

## 常见问题

### Q: 如何添加新的页面？
A: 
1. 在`src/pages/`下创建页面组件
2. 在`src/App.tsx`中添加路由
3. 在`src/components/AppLayout.tsx`中添加菜单项

### Q: 如何调用API？
A: 
1. 在`src/services/`下创建服务文件
2. 使用`apiClient`发送请求
3. 在组件中使用Hook或直接调用

### Q: 如何添加新的状态管理？
A: 
1. 在`src/store/slices/`下创建slice
2. 在`src/store/index.ts`中注册
3. 在组件中使用`useSelector`和`useDispatch`

## 贡献指南

1. Fork项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建Pull Request

## 许可证

MIT License - 详见 [LICENSE](LICENSE) 文件
