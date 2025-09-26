import { useSelector, useDispatch } from 'react-redux'
import { useCallback } from 'react'
import { RootState, AppDispatch } from '@/store'
import { 
  login, 
  logout, 
  getCurrentUser, 
  refreshToken, 
  clearError 
} from '@/store/slices/authSlice'
import { LoginForm } from '@/types'

export const useAuth = () => {
  const dispatch = useDispatch<AppDispatch>()
  const { user, token, isAuthenticated, loading, error } = useSelector((state: RootState) => state.auth)

  const handleLogin = useCallback((loginForm: LoginForm) => {
    return dispatch(login(loginForm))
  }, [dispatch])

  const handleLogout = useCallback(() => {
    return dispatch(logout())
  }, [dispatch])

  const handleGetCurrentUser = useCallback(() => {
    return dispatch(getCurrentUser())
  }, [dispatch])

  const handleRefreshToken = useCallback(() => {
    return dispatch(refreshToken())
  }, [dispatch])

  const handleClearError = useCallback(() => {
    dispatch(clearError())
  }, [dispatch])

  return {
    user,
    token,
    isAuthenticated,
    loading,
    error,
    login: handleLogin,
    logout: handleLogout,
    getCurrentUser: handleGetCurrentUser,
    refreshToken: handleRefreshToken,
    clearError: handleClearError
  }
}
