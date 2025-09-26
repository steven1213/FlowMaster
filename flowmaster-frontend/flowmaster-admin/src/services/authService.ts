import axios from 'axios'
import { LoginForm, User, ApiResponse } from '@/types'

// 创建axios实例
const apiClient = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
apiClient.interceptors.response.use(
  (response) => {
    return response
  },
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export const authService = {
  // 登录
  login: (loginForm: LoginForm): Promise<ApiResponse<{ user: User; token: string }>> => {
    return apiClient.post('/auth/login', loginForm)
  },

  // 退出登录
  logout: (): Promise<ApiResponse<void>> => {
    return apiClient.post('/auth/logout')
  },

  // 获取当前用户信息
  getCurrentUser: (): Promise<ApiResponse<User>> => {
    return apiClient.get('/auth/me')
  },

  // 刷新token
  refreshToken: (): Promise<ApiResponse<{ token: string }>> => {
    return apiClient.post('/auth/refresh')
  },

  // 修改密码
  changePassword: (oldPassword: string, newPassword: string): Promise<ApiResponse<void>> => {
    return apiClient.post('/auth/change-password', { oldPassword, newPassword })
  }
}
