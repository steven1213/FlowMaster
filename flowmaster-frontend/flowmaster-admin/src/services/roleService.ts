import { Role, RoleForm, RoleQuery, PageResult, ApiResponse } from '@/types'
import { apiClient } from './apiClient'

export const roleService = {
  // 获取角色列表
  getRoles: (query: RoleQuery): Promise<ApiResponse<PageResult<Role>>> => {
    return apiClient.get('/admin/roles', { params: query })
  },

  // 获取角色详情
  getRoleById: (id: string): Promise<ApiResponse<Role>> => {
    return apiClient.get(`/admin/roles/${id}`)
  },

  // 创建角色
  createRole: (roleForm: RoleForm): Promise<ApiResponse<Role>> => {
    return apiClient.post('/admin/roles', roleForm)
  },

  // 更新角色
  updateRole: (id: string, roleForm: RoleForm): Promise<ApiResponse<Role>> => {
    return apiClient.put(`/admin/roles/${id}`, roleForm)
  },

  // 删除角色
  deleteRole: (id: string): Promise<ApiResponse<void>> => {
    return apiClient.delete(`/admin/roles/${id}`)
  },

  // 分配权限
  assignPermissions: (id: string, permissionIds: string[]): Promise<ApiResponse<Role>> => {
    return apiClient.post(`/admin/roles/${id}/permissions`, { permissionIds })
  },

  // 获取角色权限
  getRolePermissions: (id: string): Promise<ApiResponse<string[]>> => {
    return apiClient.get(`/admin/roles/${id}/permissions`)
  }
}
