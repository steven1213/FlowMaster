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
  processDefinition: ProcessDefinition
}

export enum WorkflowStatus {
  DRAFT = 'DRAFT',
  ACTIVE = 'ACTIVE',
  SUSPENDED = 'SUSPENDED',
  ARCHIVED = 'ARCHIVED'
}

export interface ProcessDefinition {
  id: string
  name: string
  key: string
  version: number
  resourceName: string
  deploymentId: string
  diagramResourceName: string
  description?: string
  category?: string
  tenantId?: string
  versionTag?: string
  historyTimeToLive?: number
  startableInTasklist: boolean
  startedByForm: boolean
  taskDefinition: TaskDefinition[]
}

export interface TaskDefinition {
  id: string
  name: string
  assignee?: string
  candidateUsers?: string[]
  candidateGroups?: string[]
  dueDate?: string
  followUpDate?: string
  priority: number
  formKey?: string
  description?: string
}

export interface WorkflowInstance {
  id: string
  processDefinitionId: string
  processDefinitionKey: string
  processDefinitionName: string
  processDefinitionVersion: number
  businessKey?: string
  startTime: string
  endTime?: string
  durationInMillis?: number
  startUserId: string
  startActivityId: string
  endActivityId?: string
  deleteReason?: string
  tenantId?: string
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
  followUp?: string
  delegationState?: string
  description?: string
  executionId: string
  parentTaskId?: string
  priority: number
  processDefinitionId: string
  processInstanceId: string
  taskDefinitionKey: string
  caseExecutionId?: string
  caseInstanceId?: string
  caseDefinitionId?: string
  suspended: boolean
  formKey?: string
  tenantId?: string
  candidateUsers?: string[]
  candidateGroups?: string[]
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

// 监控相关类型
export interface SystemMetrics {
  cpu: number
  memory: number
  disk: number
  network: NetworkMetrics
  jvm: JVMMetrics
}

export interface NetworkMetrics {
  bytesReceived: number
  bytesSent: number
  packetsReceived: number
  packetsSent: number
}

export interface JVMMetrics {
  heapUsed: number
  heapMax: number
  nonHeapUsed: number
  nonHeapMax: number
  threads: number
  gcCount: number
  gcTime: number
}

export interface ApplicationMetrics {
  totalRequests: number
  successfulRequests: number
  failedRequests: number
  averageResponseTime: number
  activeUsers: number
  activeSessions: number
  activeWorkflows: number
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

// 通用类型
export interface SelectOption {
  label: string
  value: string | number
  disabled?: boolean
}

export interface TableColumn {
  title: string
  dataIndex: string
  key?: string
  width?: number
  align?: 'left' | 'center' | 'right'
  sorter?: boolean
  render?: (value: any, record: any, index: number) => React.ReactNode
}

export interface SearchParams {
  page?: number
  size?: number
  sort?: string
  order?: 'asc' | 'desc'
  keyword?: string
  [key: string]: any
}
