import { Permission, PermissionForm, PermissionQuery, PageResult, ApiResponse } from '@/types'
import { apiClient } from './apiClient'

export const permissionService = {
  // 获取权限列表
  getPermissions: (query: PermissionQuery): Promise<ApiResponse<PageResult<Permission>>> => {
    return apiClient.get('/admin/permissions', { params: query })
  },

  // 获取权限树
  getPermissionTree: (): Promise<ApiResponse<Permission[]>> => {
    return apiClient.get('/admin/permissions/tree')
  },

  // 获取权限详情
  getPermissionById: (id: string): Promise<ApiResponse<Permission>> => {
    return apiClient.get(`/admin/permissions/${id}`)
  },

  // 创建权限
  createPermission: (permissionForm: PermissionForm): Promise<ApiResponse<Permission>> => {
    return apiClient.post('/admin/permissions', permissionForm)
  },

  // 更新权限
  updatePermission: (id: string, permissionForm: PermissionForm): Promise<ApiResponse<Permission>> => {
    return apiClient.put(`/admin/permissions/${id}`, permissionForm)
  },

  // 删除权限
  deletePermission: (id: string): Promise<ApiResponse<void>> => {
    return apiClient.delete(`/admin/permissions/${id}`)
  },

  // 获取子权限
  getChildPermissions: (parentId: string): Promise<ApiResponse<Permission[]>> => {
    return apiClient.get(`/admin/permissions/${parentId}/children`)
  }
}
