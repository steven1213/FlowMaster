// import axios from 'axios'
import { LoginRequest, LoginResponse, User, ApiResponse } from '@/types'
import { apiClient } from './apiClient'

class AuthService {
  private readonly baseUrl = '/auth/api/auth'

  async login(credentials: LoginRequest): Promise<LoginResponse> {
    const response = await apiClient.post<ApiResponse<LoginResponse>>(
      `${this.baseUrl}/login`,
      credentials
    )
    
    if (response.data.code === 200) {
      const { token, expiresIn } = response.data.data
      
      // 存储token到localStorage
      localStorage.setItem('token', token)
      localStorage.setItem('tokenExpiry', String(Date.now() + expiresIn * 1000))
      
      // 设置axios默认header
      apiClient.defaults.headers.common['Authorization'] = `Bearer ${token}`
      
      return response.data.data
    } else {
      throw new Error(response.data.message || '登录失败')
    }
  }

  async logout(): Promise<void> {
    try {
      await apiClient.post(`${this.baseUrl}/logout`)
    } catch (error) {
      console.warn('Logout request failed:', error)
    } finally {
      // 清除本地存储
      localStorage.removeItem('token')
      localStorage.removeItem('tokenExpiry')
      delete apiClient.defaults.headers.common['Authorization']
    }
  }

  async refreshToken(): Promise<LoginResponse> {
    const response = await apiClient.post<ApiResponse<LoginResponse>>(
      `${this.baseUrl}/refresh`
    )
    
    if (response.data.code === 200) {
      const { token, expiresIn } = response.data.data
      
      // 更新token
      localStorage.setItem('token', token)
      localStorage.setItem('tokenExpiry', String(Date.now() + expiresIn * 1000))
      apiClient.defaults.headers.common['Authorization'] = `Bearer ${token}`
      
      return response.data.data
    } else {
      throw new Error(response.data.message || '刷新token失败')
    }
  }

  async getCurrentUser(): Promise<User> {
    const response = await apiClient.get<ApiResponse<User>>(`${this.baseUrl}/me`)
    
    if (response.data.code === 200) {
      return response.data.data
    } else {
      throw new Error(response.data.message || '获取用户信息失败')
    }
  }

  isTokenValid(): boolean {
    const token = localStorage.getItem('token')
    const expiry = localStorage.getItem('tokenExpiry')
    
    if (!token || !expiry) {
      return false
    }
    
    const now = Date.now()
    const tokenExpiry = parseInt(expiry)
    
    return now < tokenExpiry
  }

  getStoredToken(): string | null {
    return localStorage.getItem('token')
  }

  initializeAuth(): void {
    const token = this.getStoredToken()
    if (token && this.isTokenValid()) {
      apiClient.defaults.headers.common['Authorization'] = `Bearer ${token}`
    }
  }
}

export const authService = new AuthService()
