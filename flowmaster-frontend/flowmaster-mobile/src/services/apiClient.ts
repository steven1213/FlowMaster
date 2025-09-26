import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { Alert } from 'react-native'
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
  (config: AxiosRequestConfig) => {
    // 添加请求ID用于追踪
    config.headers = {
      ...config.headers,
      'X-Request-ID': generateRequestId()
    }
    
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
      Alert.alert('错误', data.message || '请求失败')
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
          Alert.alert('未授权', '请重新登录')
          // 清除本地存储并跳转到登录页
          // 这里需要dispatch logout action
          break
        case 403:
          Alert.alert('权限不足', '您没有权限执行此操作')
          break
        case 404:
          Alert.alert('错误', '请求的资源不存在')
          break
        case 500:
          Alert.alert('服务器错误', '服务器内部错误，请稍后重试')
          break
        default:
          Alert.alert('请求失败', data?.message || '网络错误')
      }
    } else if (error.request) {
      Alert.alert('网络错误', '请检查网络连接')
    } else {
      Alert.alert('错误', '请求配置错误')
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
  get: <T = any>(url: string, config?: AxiosRequestConfig) =>
    apiClient.get<ApiResponse<T>>(url, config),
    
  post: <T = any>(url: string, data?: any, config?: AxiosRequestConfig) =>
    apiClient.post<ApiResponse<T>>(url, data, config),
    
  put: <T = any>(url: string, data?: any, config?: AxiosRequestConfig) =>
    apiClient.put<ApiResponse<T>>(url, data, config),
    
  delete: <T = any>(url: string, config?: AxiosRequestConfig) =>
    apiClient.delete<ApiResponse<T>>(url, config),
    
  patch: <T = any>(url: string, data?: any, config?: AxiosRequestConfig) =>
    apiClient.patch<ApiResponse<T>>(url, data, config)
}
