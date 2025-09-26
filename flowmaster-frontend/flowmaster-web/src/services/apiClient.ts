import axios, { AxiosInstance, InternalAxiosRequestConfig, AxiosResponse } from 'axios'
import { message } from 'antd'
import { ApiResponse } from '@/types'

// 创建axios实例
export const apiClient: AxiosInstance = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
apiClient.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // 添加认证token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    
    // 添加请求ID用于追踪
    config.headers['X-Request-ID'] = generateRequestId()
    
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
apiClient.interceptors.response.use(
  (response: AxiosResponse<ApiResponse>) => {
    // 统一处理响应
    const { data } = response
    
    if (data.code !== 200) {
      message.error(data.message || '请求失败')
      return Promise.reject(new Error(data.message || '请求失败'))
    }
    
    return response
  },
  (error) => {
    // 统一处理错误
    if (error.response) {
      const { status, data } = error.response
      
      switch (status) {
        case 401:
          message.error('未授权，请重新登录')
          // 清除本地存储并跳转到登录页
          localStorage.removeItem('token')
          localStorage.removeItem('tokenExpiry')
          window.location.href = '/login'
          break
        case 403:
          message.error('权限不足')
          break
        case 404:
          message.error('请求的资源不存在')
          break
        case 500:
          message.error('服务器内部错误')
          break
        default:
          message.error(data?.message || '请求失败')
      }
    } else if (error.request) {
      message.error('网络错误，请检查网络连接')
    } else {
      message.error('请求配置错误')
    }
    
    return Promise.reject(error)
  }
)

// 生成请求ID
function generateRequestId(): string {
  return `req_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`
}

// 通用API方法
export const api = {
  get: <T = any>(url: string, config?: InternalAxiosRequestConfig) =>
    apiClient.get<ApiResponse<T>>(url, config),
    
  post: <T = any>(url: string, data?: any, config?: InternalAxiosRequestConfig) =>
    apiClient.post<ApiResponse<T>>(url, data, config),
    
  put: <T = any>(url: string, data?: any, config?: InternalAxiosRequestConfig) =>
    apiClient.put<ApiResponse<T>>(url, data, config),
    
  delete: <T = any>(url: string, config?: InternalAxiosRequestConfig) =>
    apiClient.delete<ApiResponse<T>>(url, config),
    
  patch: <T = any>(url: string, data?: any, config?: InternalAxiosRequestConfig) =>
    apiClient.patch<ApiResponse<T>>(url, data, config)
}
