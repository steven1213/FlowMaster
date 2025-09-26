// 用户相关类型
export interface User {
  id: string
  username: string
  email: string
  phone?: string
  avatar?: string
  role: UserRole
  department?: string
  position?: string
  status: UserStatus
  createdAt: string
  updatedAt: string
}

export enum UserRole {
  ADMIN = 'ADMIN',
  MANAGER = 'MANAGER',
  USER = 'USER'
}

export enum UserStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  SUSPENDED = 'SUSPENDED'
}

// 认证相关类型
export interface LoginRequest {
  username: string
  password: string
}

export interface LoginResponse {
  token: string
  user: User
  expiresIn: number
}

export interface AuthState {
  isAuthenticated: boolean
  user: User | null
  token: string | null
  isLoading: boolean
}

// 工作流相关类型
export interface WorkflowDefinition {
  id: string
  name: string
  description?: string
  version: string
  status: WorkflowStatus
  category: string
  createdBy: string
  createdAt: string
  updatedAt: string
}

export enum WorkflowStatus {
  DRAFT = 'DRAFT',
  ACTIVE = 'ACTIVE',
  SUSPENDED = 'SUSPENDED',
  ARCHIVED = 'ARCHIVED'
}

export interface WorkflowInstance {
  id: string
  processDefinitionId: string
  processDefinitionKey: string
  processDefinitionName: string
  businessKey?: string
  startTime: string
  endTime?: string
  startUserId: string
  state: WorkflowInstanceState
  tasks: Task[]
}

export enum WorkflowInstanceState {
  ACTIVE = 'ACTIVE',
  SUSPENDED = 'SUSPENDED',
  COMPLETED = 'COMPLETED',
  TERMINATED = 'TERMINATED'
}

export interface Task {
  id: string
  name: string
  assignee?: string
  owner?: string
  created: string
  due?: string
  description?: string
  executionId: string
  processInstanceId: string
  taskDefinitionKey: string
  priority: number
  suspended: boolean
  formKey?: string
}

// 审批相关类型
export interface ApprovalRequest {
  id: string
  title: string
  description?: string
  requesterId: string
  requesterName: string
  workflowInstanceId: string
  currentTaskId?: string
  status: ApprovalStatus
  priority: ApprovalPriority
  category: string
  attachments?: Attachment[]
  comments: Comment[]
  createdAt: string
  updatedAt: string
}

export enum ApprovalStatus {
  PENDING = 'PENDING',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
  CANCELLED = 'CANCELLED'
}

export enum ApprovalPriority {
  LOW = 'LOW',
  NORMAL = 'NORMAL',
  HIGH = 'HIGH',
  URGENT = 'URGENT'
}

export interface Attachment {
  id: string
  name: string
  url: string
  size: number
  type: string
  uploadedBy: string
  uploadedAt: string
}

export interface Comment {
  id: string
  content: string
  authorId: string
  authorName: string
  createdAt: string
  type: CommentType
}

export enum CommentType {
  COMMENT = 'COMMENT',
  APPROVAL = 'APPROVAL',
  REJECTION = 'REJECTION'
}

// API响应类型
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
  timestamp: string
  traceId: string
}

export interface PageResponse<T = any> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
  first: boolean
  last: boolean
  numberOfElements: number
}

// 导航相关类型
export type RootStackParamList = {
  Login: undefined
  Main: undefined
  WorkflowDetail: { workflowId: string }
  ApprovalDetail: { approvalId: string }
  Profile: undefined
  Settings: undefined
}

export type MainTabParamList = {
  Dashboard: undefined
  Workflow: undefined
  Approval: undefined
  Profile: undefined
}

// 通用类型
export interface SelectOption {
  label: string
  value: string | number
  disabled?: boolean
}

// 推送通知类型
export interface PushNotification {
  id: string
  title: string
  body: string
  data?: any
  type: NotificationType
  createdAt: string
  read: boolean
}

export enum NotificationType {
  APPROVAL_REQUEST = 'APPROVAL_REQUEST',
  APPROVAL_RESULT = 'APPROVAL_RESULT',
  WORKFLOW_UPDATE = 'WORKFLOW_UPDATE',
  SYSTEM_NOTICE = 'SYSTEM_NOTICE'
}
