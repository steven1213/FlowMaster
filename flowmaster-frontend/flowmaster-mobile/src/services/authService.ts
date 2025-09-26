import axios from 'axios'
import { LoginRequest, LoginResponse, User, ApiResponse } from '@/types'
import { apiClient } from './apiClient'
import * as Keychain from 'react-native-keychain'

class AuthService {
  private readonly baseUrl = '/api/auth'

  async login(credentials: LoginRequest): Promise<LoginResponse> {
    const response = await apiClient.post<ApiResponse<LoginResponse>>(
      `${this.baseUrl}/login`,
      credentials
    )
    
    if (response.data.code === 200) {
      const { token, user, expiresIn } = response.data.data
      
      // 存储token到Keychain
      await Keychain.setInternetCredentials(
        'flowmaster_token',
        'token',
        token
      )
      
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
      // 清除Keychain存储
      await Keychain.resetInternetCredentials('flowmaster_token')
      delete apiClient.defaults.headers.common['Authorization']
    }
  }

  async refreshToken(): Promise<LoginResponse> {
    const response = await apiClient.post<ApiResponse<LoginResponse>>(
      `${this.baseUrl}/refresh`
    )
    
    if (response.data.code === 200) {
      const { token, user, expiresIn } = response.data.data
      
      // 更新token
      await Keychain.setInternetCredentials(
        'flowmaster_token',
        'token',
        token
      )
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

  async getStoredToken(): Promise<string | null> {
    try {
      const credentials = await Keychain.getInternetCredentials('flowmaster_token')
      if (credentials && credentials.password) {
        return credentials.password
      }
      return null
    } catch (error) {
      console.warn('Failed to get stored token:', error)
      return null
    }
  }

  async initializeAuth(): Promise<void> {
    const token = await this.getStoredToken()
    if (token) {
      apiClient.defaults.headers.common['Authorization'] = `Bearer ${token}`
    }
  }
}

export const authService = new AuthService()
