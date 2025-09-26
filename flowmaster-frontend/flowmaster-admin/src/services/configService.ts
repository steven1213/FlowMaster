import { SystemConfig, SystemConfigForm, SystemMetrics, ApiResponse } from '@/types'
import { apiClient } from './apiClient'

export const configService = {
  // 获取系统配置列表
  getSystemConfigs: (): Promise<ApiResponse<SystemConfig[]>> => {
    return apiClient.get('/admin/config')
  },

  // 获取系统配置详情
  getSystemConfigById: (id: string): Promise<ApiResponse<SystemConfig>> => {
    return apiClient.get(`/admin/config/${id}`)
  },

  // 更新系统配置
  updateSystemConfig: (id: string, configForm: SystemConfigForm): Promise<ApiResponse<SystemConfig>> => {
    return apiClient.put(`/admin/config/${id}`, configForm)
  },

  // 重置系统配置
  resetSystemConfig: (key: string): Promise<ApiResponse<SystemConfig>> => {
    return apiClient.post(`/admin/config/${key}/reset`)
  },

  // 获取系统指标
  getSystemMetrics: (): Promise<ApiResponse<SystemMetrics>> => {
    return apiClient.get('/admin/metrics')
  },

  // 获取系统健康状态
  getSystemHealth: (): Promise<ApiResponse<{
    status: string
    components: Array<{
      name: string
      status: string
      details?: any
    }>
  }>> => {
    return apiClient.get('/admin/health')
  },

  // 获取系统信息
  getSystemInfo: (): Promise<ApiResponse<{
    version: string
    buildTime: string
    javaVersion: string
    osName: string
    osVersion: string
    uptime: number
  }>> => {
    return apiClient.get('/admin/info')
  }
}
