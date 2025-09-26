# FlowMaster Mobile 移动端应用

FlowMaster Mobile应用是基于React Native + TypeScript构建的跨平台移动端工作流管理系统。

## 技术栈

- **React Native 0.72** - 跨平台移动应用框架
- **TypeScript 4.8** - 类型安全的JavaScript
- **React Navigation 6** - 导航管理
- **Redux Toolkit** - 状态管理
- **React Native Paper** - Material Design UI组件库
- **React Native Vector Icons** - 图标库
- **React Native Keychain** - 安全存储
- **Axios** - HTTP客户端
- **React Native Chart Kit** - 图表库

## 项目结构

```
flowmaster-mobile/
├── android/                 # Android原生代码
├── ios/                     # iOS原生代码
├── src/
│   ├── components/         # 通用组件
│   ├── screens/           # 屏幕组件
│   │   ├── LoginScreen.tsx
│   │   ├── DashboardScreen.tsx
│   │   ├── WorkflowScreen.tsx
│   │   ├── ApprovalScreen.tsx
│   │   └── ProfileScreen.tsx
│   ├── navigation/        # 导航配置
│   │   └── AppNavigator.tsx
│   ├── services/         # API服务
│   │   ├── apiClient.ts
│   │   └── authService.ts
│   ├── store/           # Redux状态管理
│   │   ├── index.ts
│   │   └── slices/
│   ├── types/           # TypeScript类型定义
│   │   └── index.ts
│   ├── utils/           # 工具函数
│   │   └── theme.ts
│   └── App.tsx          # 主应用组件
├── package.json
├── tsconfig.json
├── babel.config.js
├── metro.config.js
└── README.md
```

## 功能特性

### ✅ 已实现功能
- **用户认证系统** - 登录/登出，安全token存储
- **跨平台导航** - 基于React Navigation的导航系统
- **状态管理** - Redux Toolkit + Redux Persist
- **API集成** - Axios + 拦截器 + 错误处理
- **Material Design UI** - React Native Paper组件库
- **安全存储** - React Native Keychain
- **仪表盘** - 数据统计和概览
- **响应式设计** - 适配不同屏幕尺寸

### 🚧 开发中功能
- **工作流管理** - 工作流定义、实例管理
- **审批流程** - 审批任务处理
- **推送通知** - 实时消息推送
- **离线支持** - 离线数据同步

## 环境要求

### 开发环境
- Node.js >= 18.0.0
- npm >= 9.0.0
- React Native CLI
- Android Studio (Android开发)
- Xcode (iOS开发，仅macOS)

### 移动端环境
- Android 6.0+ (API level 23+)
- iOS 11.0+

## 安装和运行

### 1. 安装依赖
```bash
# 在flowmaster-frontend目录下
npm run install:mobile

# 或者直接进入mobile目录
cd flowmaster-mobile
npm install
```

### 2. iOS设置 (仅macOS)
```bash
# 安装iOS依赖
cd ios && pod install && cd ..
```

### 3. 启动Metro服务器
```bash
npm start
```

### 4. 运行应用

#### Android
```bash
npm run android
```

#### iOS (仅macOS)
```bash
npm run ios
```

## 构建发布版本

### Android
```bash
npm run build:android
```

### iOS (仅macOS)
```bash
npm run build:ios
```

## 开发指南

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
npm test
```

## API集成

### 后端服务地址
- 开发环境: `http://localhost:8080`
- 生产环境: 通过环境变量配置

### 认证流程
1. 用户登录获取JWT token
2. Token安全存储在Keychain中
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

## 推送通知

### 功能特性
- 审批请求通知
- 审批结果通知
- 工作流更新通知
- 系统公告通知

### 配置说明
1. 配置Firebase Cloud Messaging (FCM)
2. 设置推送权限
3. 处理通知点击事件
4. 本地通知管理

## 部署说明

### Android发布
1. 生成签名密钥
2. 配置gradle签名
3. 构建Release版本
4. 上传到Google Play Store

### iOS发布
1. 配置Apple Developer账号
2. 设置App ID和证书
3. 构建Archive版本
4. 上传到App Store Connect

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

### 文件组织
- 按功能模块组织文件
- 使用路径别名简化导入
- 保持组件单一职责
- 提取可复用组件

## 性能优化

### 图片优化
- 使用适当的图片格式
- 实现图片懒加载
- 压缩图片资源

### 列表优化
- 使用FlatList替代ScrollView
- 实现虚拟化列表
- 优化列表项渲染

### 内存管理
- 及时清理定时器
- 避免内存泄漏
- 使用WeakMap存储引用

## 常见问题

### Q: 如何添加新的屏幕？
A: 
1. 在`src/screens/`下创建屏幕组件
2. 在`src/navigation/AppNavigator.tsx`中添加路由
3. 更新类型定义文件

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

### Q: 如何处理推送通知？
A: 
1. 配置推送服务
2. 注册推送权限
3. 处理通知事件
4. 更新通知状态

## 贡献指南

1. Fork项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建Pull Request

## 许可证

MIT License - 详见 [LICENSE](LICENSE) 文件
