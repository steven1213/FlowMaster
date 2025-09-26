import { AuditLog, AuditLogQuery, PageResult, ApiResponse } from '@/types'
import { apiClient } from './apiClient'

export const auditService = {
  // 获取审计日志列表
  getAuditLogs: (query: AuditLogQuery): Promise<ApiResponse<PageResult<AuditLog>>> => {
    return apiClient.get('/admin/audit/logs', { params: query })
  },

  // 获取审计日志详情
  getAuditLogById: (id: string): Promise<ApiResponse<AuditLog>> => {
    return apiClient.get(`/admin/audit/logs/${id}`)
  },

  // 导出审计日志
  exportAuditLogs: (query: AuditLogQuery): Promise<Blob> => {
    return apiClient.get('/admin/audit/logs/export', { 
      params: query,
      responseType: 'blob'
    })
  },

  // 获取审计统计
  getAuditStats: (): Promise<ApiResponse<{
    totalLogs: number
    successLogs: number
    failureLogs: number
    todayLogs: number
    topOperations: Array<{ operation: string; count: number }>
    topUsers: Array<{ username: string; count: number }>
  }>> => {
    return apiClient.get('/admin/audit/stats')
  }
}
