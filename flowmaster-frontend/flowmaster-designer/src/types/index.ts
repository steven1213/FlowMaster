// 工作流节点类型
export interface WorkflowNode {
  id: string
  type: NodeType
  name: string
  description?: string
  position: { x: number; y: number }
  size: { width: number; height: number }
  data: NodeData
  style?: NodeStyle
}

export enum NodeType {
  START = 'start',
  END = 'end',
  TASK = 'task',
  USER_TASK = 'userTask',
  SERVICE_TASK = 'serviceTask',
  SCRIPT_TASK = 'scriptTask',
  GATEWAY = 'gateway',
  EXCLUSIVE_GATEWAY = 'exclusiveGateway',
  PARALLEL_GATEWAY = 'parallelGateway',
  INCLUSIVE_GATEWAY = 'inclusiveGateway',
  EVENT = 'event',
  TIMER_EVENT = 'timerEvent',
  MESSAGE_EVENT = 'messageEvent',
  SIGNAL_EVENT = 'signalEvent',
  SUBPROCESS = 'subprocess',
  POOL = 'pool',
  LANE = 'lane'
}

export interface NodeData {
  assignee?: string
  candidateUsers?: string[]
  candidateGroups?: string[]
  dueDate?: string
  priority?: number
  formKey?: string
  script?: string
  serviceClass?: string
  method?: string
  variables?: Record<string, any>
  conditions?: string[]
  [key: string]: any
}

export interface NodeStyle {
  fill?: string
  stroke?: string
  strokeWidth?: number
  fontSize?: number
  fontColor?: string
  borderRadius?: number
  [key: string]: any
}

// 工作流连线类型
export interface WorkflowEdge {
  id: string
  source: string
  target: string
  sourceAnchor?: string
  targetAnchor?: string
  label?: string
  condition?: string
  style?: EdgeStyle
  data?: EdgeData
}

export interface EdgeStyle {
  stroke?: string
  strokeWidth?: number
  strokeDasharray?: string
  fontSize?: number
  fontColor?: string
  [key: string]: any
}

export interface EdgeData {
  condition?: string
  priority?: number
  [key: string]: any
}

// 工作流定义类型
export interface WorkflowDefinition {
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

export enum WorkflowStatus {
  DRAFT = 'DRAFT',
  ACTIVE = 'ACTIVE',
  SUSPENDED = 'SUSPENDED',
  ARCHIVED = 'ARCHIVED'
}

export interface WorkflowVariable {
  name: string
  type: VariableType
  defaultValue?: any
  required: boolean
  description?: string
}

export enum VariableType {
  STRING = 'string',
  NUMBER = 'number',
  BOOLEAN = 'boolean',
  DATE = 'date',
  OBJECT = 'object',
  ARRAY = 'array'
}

export interface WorkflowProperties {
  executionListeners?: ExecutionListener[]
  taskListeners?: TaskListener[]
  eventListeners?: EventListener[]
  [key: string]: any
}

export interface ExecutionListener {
  event: string
  class: string
  properties?: Record<string, any>
}

export interface TaskListener {
  event: string
  class: string
  properties?: Record<string, any>
}

export interface EventListener {
  event: string
  class: string
  properties?: Record<string, any>
}

// 设计器状态类型
export interface DesignerState {
  currentWorkflow: WorkflowDefinition | null
  selectedNodes: string[]
  selectedEdges: string[]
  clipboard: ClipboardData | null
  history: HistoryState
  viewport: ViewportState
  settings: DesignerSettings
}

export interface ClipboardData {
  nodes: WorkflowNode[]
  edges: WorkflowEdge[]
}

export interface HistoryState {
  past: WorkflowDefinition[]
  present: WorkflowDefinition | null
  future: WorkflowDefinition[]
}

export interface ViewportState {
  x: number
  y: number
  zoom: number
}

export interface DesignerSettings {
  snapToGrid: boolean
  gridSize: number
  showGrid: boolean
  showMinimap: boolean
  autoSave: boolean
  theme: 'light' | 'dark'
}

// 模板类型
export interface WorkflowTemplate {
  id: string
  name: string
  description: string
  category: string
  tags: string[]
  thumbnail: string
  definition: WorkflowDefinition
  usageCount: number
  rating: number
  createdBy: string
  createdAt: string
}

// 组件库类型
export interface ComponentLibrary {
  id: string
  name: string
  description: string
  category: string
  components: ComponentDefinition[]
  version: string
  author: string
  createdAt: string
}

export interface ComponentDefinition {
  id: string
  name: string
  type: NodeType
  icon: string
  description: string
  defaultData: NodeData
  defaultStyle: NodeStyle
  properties: ComponentProperty[]
}

export interface ComponentProperty {
  name: string
  type: PropertyType
  label: string
  required: boolean
  defaultValue?: any
  options?: any[]
  description?: string
}

export enum PropertyType {
  STRING = 'string',
  NUMBER = 'number',
  BOOLEAN = 'boolean',
  SELECT = 'select',
  MULTI_SELECT = 'multiSelect',
  TEXTAREA = 'textarea',
  DATE = 'date',
  COLOR = 'color',
  FILE = 'file'
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

export interface SearchParams {
  page?: number
  size?: number
  sort?: string
  order?: 'asc' | 'desc'
  keyword?: string
  category?: string
  [key: string]: any
}
