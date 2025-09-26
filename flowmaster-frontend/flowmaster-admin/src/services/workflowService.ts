import { WorkflowDefinition, WorkflowInstance, WorkflowQuery, PageResult, ApiResponse } from '@/types'
import { apiClient } from './apiClient'

export const workflowService = {
  // 获取工作流定义列表
  getWorkflowDefinitions: (query: WorkflowQuery): Promise<ApiResponse<PageResult<WorkflowDefinition>>> => {
    return apiClient.get('/admin/workflows/definitions', { params: query })
  },

  // 获取工作流实例列表
  getWorkflowInstances: (query: WorkflowQuery): Promise<ApiResponse<PageResult<WorkflowInstance>>> => {
    return apiClient.get('/admin/workflows/instances', { params: query })
  },

  // 获取工作流定义详情
  getWorkflowDefinitionById: (id: string): Promise<ApiResponse<WorkflowDefinition>> => {
    return apiClient.get(`/admin/workflows/definitions/${id}`)
  },

  // 获取工作流实例详情
  getWorkflowInstanceById: (id: string): Promise<ApiResponse<WorkflowInstance>> => {
    return apiClient.get(`/admin/workflows/instances/${id}`)
  },

  // 部署工作流
  deployWorkflow: (definitionId: string): Promise<ApiResponse<WorkflowDefinition>> => {
    return apiClient.post(`/admin/workflows/definitions/${definitionId}/deploy`)
  },

  // 挂起工作流实例
  suspendWorkflow: (instanceId: string): Promise<ApiResponse<WorkflowInstance>> => {
    return apiClient.post(`/admin/workflows/instances/${instanceId}/suspend`)
  },

  // 激活工作流实例
  activateWorkflow: (instanceId: string): Promise<ApiResponse<WorkflowInstance>> => {
    return apiClient.post(`/admin/workflows/instances/${instanceId}/activate`)
  },

  // 终止工作流实例
  terminateWorkflow: (instanceId: string): Promise<ApiResponse<WorkflowInstance>> => {
    return apiClient.post(`/admin/workflows/instances/${instanceId}/terminate`)
  },

  // 获取工作流历史
  getWorkflowHistory: (instanceId: string): Promise<ApiResponse<any[]>> => {
    return apiClient.get(`/admin/workflows/instances/${instanceId}/history`)
  },

  // 获取工作流任务
  getWorkflowTasks: (instanceId: string): Promise<ApiResponse<any[]>> => {
    return apiClient.get(`/admin/workflows/instances/${instanceId}/tasks`)
  }
}
