import { AuthData } from './communication'

// 认证服务接口
export interface LoginRequest {
  username: string
  password: string
  remember?: boolean
}

export interface LoginResponse {
  token: string
  refreshToken: string
  user: any
  expiresIn: number
}

// 认证服务类
export class AuthService {
  private baseUrl = '/auth/api/auth'

  async login(credentials: LoginRequest): Promise<LoginResponse> {
    try {
      const response = await fetch(`${this.baseUrl}/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(credentials)
      })

      if (!response.ok) {
        throw new Error(`登录失败: ${response.statusText}`)
      }

      const data = await response.json()
      
      if (data.code === 200) {
        const { token, refreshToken, user, expiresIn } = data.data
        
        // 存储认证信息
        const authData: AuthData = {
          token,
          refreshToken,
          user,
          permissions: user.permissions || []
        }
        
        localStorage.setItem('auth_token', token)
        localStorage.setItem('auth_refresh_token', refreshToken)
        localStorage.setItem('auth_user', JSON.stringify(user))
        
        return { token, refreshToken, user, expiresIn }
      } else {
        throw new Error(data.message || '登录失败')
      }
    } catch (error) {
      console.error('Login error:', error)
      throw error
    }
  }

  async logout(): Promise<void> {
    try {
      const token = this.getToken()
      if (token) {
        await fetch(`${this.baseUrl}/logout`, {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        })
      }
    } catch (error) {
      console.error('Logout error:', error)
    } finally {
      // 清理本地存储
      localStorage.removeItem('auth_token')
      localStorage.removeItem('auth_refresh_token')
      localStorage.removeItem('auth_user')
    }
  }

  async refreshToken(): Promise<LoginResponse> {
    try {
      const refreshToken = localStorage.getItem('auth_refresh_token')
      if (!refreshToken) {
        throw new Error('No refresh token available')
      }

      const response = await fetch(`${this.baseUrl}/refresh`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${refreshToken}`,
          'Content-Type': 'application/json'
        }
      })

      if (!response.ok) {
        throw new Error(`Token refresh failed: ${response.statusText}`)
      }

      const data = await response.json()
      
      if (data.code === 200) {
        const { token, refreshToken: newRefreshToken, user, expiresIn } = data.data
        
        // 更新存储的认证信息
        localStorage.setItem('auth_token', token)
        localStorage.setItem('auth_refresh_token', newRefreshToken)
        localStorage.setItem('auth_user', JSON.stringify(user))
        
        return { token, refreshToken: newRefreshToken, user, expiresIn }
      } else {
        throw new Error(data.message || 'Token refresh failed')
      }
    } catch (error) {
      console.error('Token refresh error:', error)
      throw error
    }
  }

  getToken(): string | null {
    return localStorage.getItem('auth_token')
  }

  getUser(): any | null {
    const userStr = localStorage.getItem('auth_user')
    if (userStr) {
      try {
        return JSON.parse(userStr)
      } catch (error) {
        console.error('Failed to parse user data:', error)
        return null
      }
    }
    return null
  }

  isAuthenticated(): boolean {
    const token = this.getToken()
    if (!token) return false

    try {
      // 简单的token过期检查
      const payload = JSON.parse(atob(token.split('.')[1]))
      const now = Date.now() / 1000
      return payload.exp > now
    } catch (error) {
      console.error('Token validation error:', error)
      return false
    }
  }

  getAuthHeaders(): Record<string, string> {
    const token = this.getToken()
    return {
      'Content-Type': 'application/json',
      ...(token && { 'Authorization': `Bearer ${token}` })
    }
  }
}

// 全局认证服务实例
export const authService = new AuthService()
