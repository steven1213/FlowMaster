# FlowMaster Designer 工作流设计器

FlowMaster Designer是一个强大的可视化工作流设计器，基于React + TypeScript + AntV G6构建，支持拖拽式设计、实时预览和BPMN标准。

## 技术栈

- **React 18** - 用户界面库
- **TypeScript 5** - 类型安全的JavaScript
- **Vite 5** - 快速构建工具
- **Ant Design 5** - UI组件库
- **AntV G6** - 图可视化引擎
- **AntV X6** - 图编辑引擎
- **Redux Toolkit** - 状态管理
- **Monaco Editor** - 代码编辑器
- **React DnD** - 拖拽功能

## 功能特性

### ✅ 已实现功能
- **可视化设计** - 拖拽式工作流设计
- **节点管理** - 支持多种BPMN节点类型
- **连线管理** - 智能连线和建议
- **属性配置** - 实时属性编辑
- **撤销重做** - 完整的历史记录管理
- **缩放平移** - 灵活的视图操作
- **小地图** - 快速导航和定位
- **网格对齐** - 精确的节点定位

### 🚧 开发中功能
- **工作流预览** - 实时预览和模拟
- **模板库** - 预设工作流模板
- **组件库** - 自定义节点组件
- **导入导出** - BPMN格式支持
- **协作编辑** - 多人实时协作

## 项目结构

```
flowmaster-designer/
├── src/
│   ├── components/        # 通用组件
│   │   ├── DesignerLayout.tsx
│   │   └── LoadingSpinner.tsx
│   ├── pages/            # 页面组件
│   │   ├── DesignerPage.tsx
│   │   ├── TemplatePage.tsx
│   │   └── LibraryPage.tsx
│   ├── designer/         # 设计器核心组件
│   │   └── components/
│   │       ├── FlowCanvas.tsx
│   │       ├── NodePalette.tsx
│   │       ├── PropertyPanel.tsx
│   │       └── Minimap.tsx
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

## 支持的节点类型

### 基础节点
- **开始节点** - 工作流起始点
- **结束节点** - 工作流终止点

### 任务节点
- **用户任务** - 需要人工处理的任务
- **服务任务** - 自动执行的服务调用
- **脚本任务** - 执行脚本代码

### 网关节点
- **排他网关** - 条件分支
- **并行网关** - 并行执行
- **包容网关** - 多条件分支

### 事件节点
- **定时事件** - 时间触发
- **消息事件** - 消息触发
- **信号事件** - 信号触发

### 其他节点
- **子流程** - 嵌套工作流
- **泳道** - 组织架构划分

## 环境要求

- Node.js >= 18.0.0
- npm >= 9.0.0

## 安装和运行

### 1. 安装依赖
```bash
# 在flowmaster-frontend目录下
npm run install:designer

# 或者直接进入designer目录
cd flowmaster-designer
npm install
```

### 2. 启动开发服务器
```bash
npm run dev
```

### 3. 访问应用
- 开发环境: http://localhost:3001

## 构建生产版本
```bash
npm run build
```

## 使用指南

### 基本操作

#### 1. 添加节点
- 从左侧节点面板拖拽节点到画布
- 或点击节点面板中的节点按钮

#### 2. 连接节点
- 鼠标悬停在节点边缘显示连接点
- 拖拽连接点到目标节点

#### 3. 编辑属性
- 选择节点或连线
- 在右侧属性面板编辑属性

#### 4. 视图操作
- **缩放**: 鼠标滚轮或工具栏按钮
- **平移**: 拖拽画布空白区域
- **重置**: 点击重置视图按钮

#### 5. 历史操作
- **撤销**: Ctrl+Z 或工具栏撤销按钮
- **重做**: Ctrl+Y 或工具栏重做按钮

### 高级功能

#### 1. 批量操作
- 框选多个节点
- 批量设置属性
- 批量删除

#### 2. 对齐工具
- 网格对齐
- 节点对齐
- 自动布局

#### 3. 导入导出
- 支持BPMN 2.0格式
- JSON格式导入导出
- 图片导出

## API集成

### 工作流定义格式
```typescript
interface WorkflowDefinition {
  id: string
  name: string
  description?: string
  version: string
  category: string
  status: WorkflowStatus
  nodes: WorkflowNode[]
  edges: WorkflowEdge[]
  variables: WorkflowVariable[]
  properties: WorkflowProperties
  createdBy: string
  createdAt: string
  updatedAt: string
}
```

### 节点定义格式
```typescript
interface WorkflowNode {
  id: string
  type: NodeType
  name: string
  description?: string
  position: { x: number; y: number }
  size: { width: number; height: number }
  data: NodeData
  style?: NodeStyle
}
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

## 扩展开发

### 自定义节点
1. 在`src/designer/components/`下创建节点组件
2. 在`NodePalette.tsx`中注册节点类型
3. 在`PropertyPanel.tsx`中添加属性配置

### 自定义主题
1. 修改`src/styles/index.css`中的CSS变量
2. 更新Ant Design主题配置
3. 调整节点和连线的默认样式

### 插件开发
1. 创建插件目录结构
2. 实现插件接口
3. 在应用中注册插件

## 性能优化

### 渲染优化
- 虚拟化大型工作流
- 按需渲染节点
- 优化SVG性能

### 内存管理
- 及时清理事件监听
- 避免内存泄漏
- 优化数据结构

### 加载优化
- 代码分割
- 懒加载组件
- 资源压缩

## 常见问题

### Q: 如何添加自定义节点类型？
A: 
1. 在`types/index.ts`中定义新的节点类型
2. 在`NodePalette.tsx`中添加节点按钮
3. 在`PropertyPanel.tsx`中添加属性配置
4. 在`FlowCanvas.tsx`中添加渲染逻辑

### Q: 如何自定义节点样式？
A: 
1. 修改`NodeStyle`接口定义
2. 在节点渲染时应用样式
3. 在属性面板中添加样式配置

### Q: 如何实现节点验证？
A: 
1. 在节点数据中添加验证规则
2. 在保存时执行验证
3. 显示验证错误信息

### Q: 如何支持键盘快捷键？
A: 
1. 添加键盘事件监听
2. 实现快捷键处理逻辑
3. 在UI中显示快捷键提示

## 贡献指南

1. Fork项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建Pull Request

## 许可证

MIT License - 详见 [LICENSE](LICENSE) 文件
