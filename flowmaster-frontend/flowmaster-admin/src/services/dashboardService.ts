import { DashboardStats, ChartData, TimeSeriesData, ApiResponse } from '@/types'
import { apiClient } from './apiClient'

export const dashboardService = {
  // 获取仪表板统计
  getDashboardStats: (): Promise<ApiResponse<DashboardStats>> => {
    return apiClient.get('/admin/dashboard/stats')
  },

  // 获取用户图表数据
  getUserChartData: (): Promise<ApiResponse<ChartData[]>> => {
    return apiClient.get('/admin/dashboard/users/chart')
  },

  // 获取工作流图表数据
  getWorkflowChartData: (): Promise<ApiResponse<ChartData[]>> => {
    return apiClient.get('/admin/dashboard/workflows/chart')
  },

  // 获取系统指标
  getSystemMetrics: (): Promise<ApiResponse<TimeSeriesData[]>> => {
    return apiClient.get('/admin/dashboard/metrics')
  },

  // 获取实时数据
  getRealtimeData: (): Promise<ApiResponse<{
    onlineUsers: number
    activeWorkflows: number
    systemLoad: number
    memoryUsage: number
  }>> => {
    return apiClient.get('/admin/dashboard/realtime')
  }
}
