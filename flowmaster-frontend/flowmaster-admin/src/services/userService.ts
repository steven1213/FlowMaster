import { User, UserForm, UserQuery, PageResult, ApiResponse } from '@/types'
import { apiClient } from './apiClient'

export const userService = {
  // 获取用户列表
  getUsers: (query: UserQuery): Promise<ApiResponse<PageResult<User>>> => {
    return apiClient.get('/admin/users', { params: query })
  },

  // 获取用户详情
  getUserById: (id: string): Promise<ApiResponse<User>> => {
    return apiClient.get(`/admin/users/${id}`)
  },

  // 创建用户
  createUser: (userForm: UserForm): Promise<ApiResponse<User>> => {
    return apiClient.post('/admin/users', userForm)
  },

  // 更新用户
  updateUser: (id: string, userForm: UserForm): Promise<ApiResponse<User>> => {
    return apiClient.put(`/admin/users/${id}`, userForm)
  },

  // 删除用户
  deleteUser: (id: string): Promise<ApiResponse<void>> => {
    return apiClient.delete(`/admin/users/${id}`)
  },

  // 重置密码
  resetPassword: (id: string, newPassword: string): Promise<ApiResponse<void>> => {
    return apiClient.post(`/admin/users/${id}/reset-password`, { newPassword })
  },

  // 修改用户状态
  changeUserStatus: (id: string, status: string): Promise<ApiResponse<User>> => {
    return apiClient.post(`/admin/users/${id}/status`, { status })
  },

  // 分配角色
  assignRoles: (id: string, roleIds: string[]): Promise<ApiResponse<User>> => {
    return apiClient.post(`/admin/users/${id}/roles`, { roleIds })
  },

  // 导出用户
  exportUsers: (query: UserQuery): Promise<Blob> => {
    return apiClient.get('/admin/users/export', { 
      params: query,
      responseType: 'blob'
    })
  },

  // 批量导入用户
  importUsers: (file: File): Promise<ApiResponse<{ success: number; failed: number; errors: string[] }>> => {
    const formData = new FormData()
    formData.append('file', file)
    return apiClient.post('/admin/users/import', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  }
}
