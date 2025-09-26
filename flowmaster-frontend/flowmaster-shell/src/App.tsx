import React, { useEffect, useState } from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import ShellLayout from '@/components/ShellLayout'
import MicroAppContainer from '@/components/MicroAppContainer'
import DashboardPage from '@/pages/DashboardPage'
import LoginPage from '@/pages/LoginPage'
import { authService } from '@/services/authService'
import { microAppCommunicator, MessageType } from '@/services/communication'

const App: React.FC = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(false)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    // 检查认证状态
    const checkAuth = async () => {
      try {
        const authenticated = authService.isAuthenticated()
        setIsAuthenticated(authenticated)
        
        if (authenticated) {
          // 同步认证信息到微应用
          const user = authService.getUser()
          const token = authService.getToken()
          if (user && token) {
            microAppCommunicator.setAuthData({
              token,
              refreshToken: localStorage.getItem('auth_refresh_token') || '',
              user,
              permissions: user.permissions || []
            })
          }
        }
      } catch (error) {
        console.error('Auth check error:', error)
        setIsAuthenticated(false)
      } finally {
        setLoading(false)
      }
    }

    checkAuth()

    // 监听认证状态变化
    const handleAuthChange = (data: any) => {
      if (data.type === MessageType.AUTH_LOGOUT) {
        setIsAuthenticated(false)
      }
    }

    microAppCommunicator.registerHandler(MessageType.AUTH_LOGOUT, handleAuthChange)

    return () => {
      microAppCommunicator.cleanup()
    }
  }, [])

  if (loading) {
    return (
      <div style={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh',
        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
      }}>
        <div style={{ textAlign: 'center', color: 'white' }}>
          <div style={{ fontSize: '24px', marginBottom: '16px' }}>🚀</div>
          <div>FlowMaster 正在加载...</div>
        </div>
      </div>
    )
  }

  if (!isAuthenticated) {
    return <LoginPage />
  }

  return (
    <ShellLayout>
      <Routes>
        <Route path="/" element={<Navigate to="/dashboard" replace />} />
        <Route path="/dashboard" element={<DashboardPage />} />
        
        {/* 微前端应用路由 */}
        <Route 
          path="/web/*" 
          element={
            <MicroAppContainer 
              appName="flowmaster-web"
              onAppLoad={(appName) => console.log(`${appName} 加载完成`)}
              onAppError={(appName, error) => console.error(`${appName} 加载失败:`, error)}
            />
          } 
        />
        
        <Route 
          path="/admin/*" 
          element={
            <MicroAppContainer 
              appName="flowmaster-admin"
              onAppLoad={(appName) => console.log(`${appName} 加载完成`)}
              onAppError={(appName, error) => console.error(`${appName} 加载失败:`, error)}
            />
          } 
        />
        
        <Route 
          path="/designer/*" 
          element={
            <MicroAppContainer 
              appName="flowmaster-designer"
              onAppLoad={(appName) => console.log(`${appName} 加载完成`)}
              onAppError={(appName, error) => console.error(`${appName} 加载失败:`, error)}
            />
          } 
        />
        
        {/* 其他页面 */}
        <Route path="/profile" element={<div>个人中心页面</div>} />
        <Route path="/settings" element={<div>系统设置页面</div>} />
        
        {/* 404 */}
        <Route path="*" element={<div>页面未找到</div>} />
      </Routes>
    </ShellLayout>
  )
}

export default App
