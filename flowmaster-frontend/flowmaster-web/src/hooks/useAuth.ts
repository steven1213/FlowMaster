import { useSelector, useDispatch } from 'react-redux'
import { useEffect } from 'react'
import { RootState, AppDispatch } from '@/store'
import { login, logout, getCurrentUser, clearAuth } from '@/store/slices/authSlice'
import { LoginRequest } from '@/types'
import { authService } from '@/services/authService'

export const useAuth = () => {
  const dispatch = useDispatch<AppDispatch>()
  const { isAuthenticated, user, token, isLoading } = useSelector(
    (state: RootState) => state.auth
  )

  useEffect(() => {
    // 初始化认证状态
    const initializeAuth = async () => {
      const storedToken = authService.getStoredToken()
      
      if (storedToken && authService.isTokenValid()) {
        try {
          // 验证token有效性
          await dispatch(getCurrentUser()).unwrap()
        } catch (error) {
          // token无效，清除认证状态
          dispatch(clearAuth())
        }
      } else {
        // 没有有效token，清除认证状态
        dispatch(clearAuth())
      }
    }

    initializeAuth()
  }, [dispatch])

  const handleLogin = async (credentials: LoginRequest) => {
    try {
      await dispatch(login(credentials)).unwrap()
      return true
    } catch (error) {
      console.error('Login failed:', error)
      return false
    }
  }

  const handleLogout = async () => {
    try {
      await dispatch(logout()).unwrap()
    } catch (error) {
      console.error('Logout failed:', error)
      // 即使logout请求失败，也要清除本地状态
      dispatch(clearAuth())
    }
  }

  return {
    isAuthenticated,
    user,
    token,
    isLoading,
    login: handleLogin,
    logout: handleLogout
  }
}
