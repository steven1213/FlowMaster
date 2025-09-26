// 通用类型定义
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
  timestamp: number
  traceId: string
}

export interface PageResult<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
  first: boolean
  last: boolean
}

export interface BaseEntity {
  id: string
  createdAt: string
  updatedAt: string
  createdBy: string
  updatedBy: string
  version: number
  deleted: boolean
}

// 用户相关类型
export interface User extends BaseEntity {
  username: string
  email: string
  phone: string
  realName: string
  avatar?: string
  status: UserStatus
  roles: Role[]
  lastLoginAt?: string
  lastLoginIp?: string
}

export enum UserStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  LOCKED = 'LOCKED',
  PENDING = 'PENDING'
}

// 角色相关类型
export interface Role extends BaseEntity {
  name: string
  code: string
  description?: string
  status: RoleStatus
  permissions: Permission[]
  userCount: number
}

export enum RoleStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE'
}

// 权限相关类型
export interface Permission extends BaseEntity {
  name: string
  code: string
  type: PermissionType
  resource: string
  action: string
  description?: string
  parentId?: string
  children?: Permission[]
  level: number
  sort: number
}

export enum PermissionType {
  MENU = 'MENU',
  BUTTON = 'BUTTON',
  API = 'API',
  DATA = 'DATA'
}

// 工作流相关类型
export interface WorkflowDefinition extends BaseEntity {
  name: string
  key: string
  version: number
  description?: string
  category: string
  status: WorkflowStatus
  definition: string
  deployedAt?: string
  deployedBy?: string
}

export enum WorkflowStatus {
  DRAFT = 'DRAFT',
  PUBLISHED = 'PUBLISHED',
  SUSPENDED = 'SUSPENDED',
  ARCHIVED = 'ARCHIVED'
}

export interface WorkflowInstance extends BaseEntity {
  processInstanceId: string
  processDefinitionId: string
  processDefinitionKey: string
  processDefinitionName: string
  businessKey?: string
  status: ProcessStatus
  startTime: string
  endTime?: string
  duration?: number
  startUserId: string
  startUserName: string
  variables: Record<string, any>
}

export enum ProcessStatus {
  RUNNING = 'RUNNING',
  COMPLETED = 'COMPLETED',
  SUSPENDED = 'SUSPENDED',
  TERMINATED = 'TERMINATED',
  CANCELLED = 'CANCELLED'
}

// 审计日志类型
export interface AuditLog extends BaseEntity {
  userId: string
  username: string
  operation: string
  method: string
  requestUrl: string
  requestParams: string
  requestBody: string
  responseBody: string
  ip: string
  userAgent: string
  executionTime: number
  status: AuditStatus
}

export enum AuditStatus {
  SUCCESS = 'SUCCESS',
  FAILURE = 'FAILURE'
}

// 系统配置类型
export interface SystemConfig extends BaseEntity {
  key: string
  value: string
  description?: string
  category: string
  type: ConfigType
  encrypted: boolean
}

export enum ConfigType {
  STRING = 'STRING',
  NUMBER = 'NUMBER',
  BOOLEAN = 'BOOLEAN',
  JSON = 'JSON'
}

// 监控指标类型
export interface SystemMetrics {
  timestamp: string
  cpu: {
    usage: number
    cores: number
  }
  memory: {
    total: number
    used: number
    free: number
    usage: number
  }
  disk: {
    total: number
    used: number
    free: number
    usage: number
  }
  network: {
    bytesReceived: number
    bytesSent: number
    packetsReceived: number
    packetsSent: number
  }
  jvm: {
    heapUsed: number
    heapMax: number
    nonHeapUsed: number
    nonHeapMax: number
    gcCount: number
    gcTime: number
  }
}

// 表单相关类型
export interface LoginForm {
  username: string
  password: string
  captcha?: string
  remember: boolean
}

export interface UserForm {
  username: string
  email: string
  phone: string
  realName: string
  password?: string
  status: UserStatus
  roleIds: string[]
}

export interface RoleForm {
  name: string
  code: string
  description?: string
  status: RoleStatus
  permissionIds: string[]
}

export interface PermissionForm {
  name: string
  code: string
  type: PermissionType
  resource: string
  action: string
  description?: string
  parentId?: string
  sort: number
}

export interface SystemConfigForm {
  key: string
  value: string
  description?: string
  category: string
  type: ConfigType
  encrypted: boolean
}

// 查询参数类型
export interface UserQuery {
  username?: string
  email?: string
  phone?: string
  realName?: string
  status?: UserStatus
  roleId?: string
  page?: number
  size?: number
  sort?: string
}

export interface RoleQuery {
  name?: string
  code?: string
  status?: RoleStatus
  page?: number
  size?: number
  sort?: string
}

export interface PermissionQuery {
  name?: string
  code?: string
  type?: PermissionType
  resource?: string
  page?: number
  size?: number
  sort?: string
}

export interface AuditLogQuery {
  userId?: string
  username?: string
  operation?: string
  method?: string
  requestUrl?: string
  ip?: string
  status?: AuditStatus
  startTime?: string
  endTime?: string
  page?: number
  size?: number
  sort?: string
}

export interface WorkflowQuery {
  name?: string
  key?: string
  category?: string
  status?: WorkflowStatus
  page?: number
  size?: number
  sort?: string
}

// 菜单类型
export interface MenuItem {
  key: string
  title: string
  icon?: string
  path?: string
  children?: MenuItem[]
  permission?: string
}

// 统计数据类型
export interface DashboardStats {
  totalUsers: number
  activeUsers: number
  totalRoles: number
  totalPermissions: number
  totalWorkflows: number
  runningWorkflows: number
  totalAuditLogs: number
  systemUptime: number
}

// 图表数据类型
export interface ChartData {
  name: string
  value: number
  date?: string
}

export interface TimeSeriesData {
  timestamp: string
  value: number
  label?: string
}
