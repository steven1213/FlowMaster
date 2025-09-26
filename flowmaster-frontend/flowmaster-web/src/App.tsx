import React from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
// import { Layout } from 'antd'

import { useAuth } from '@/hooks/useAuth'
import LoginPage from '@/pages/LoginPage'
import DashboardPage from '@/pages/DashboardPage'
import WorkflowPage from '@/pages/WorkflowPage'
import ApprovalPage from '@/pages/ApprovalPage'
import MonitorPage from '@/pages/MonitorPage'
import ProfilePage from '@/pages/ProfilePage'
import ApiTestPage from '@/pages/ApiTestPage'
import AppLayout from '@/components/AppLayout'
import LoadingSpinner from '@/components/LoadingSpinner'

// const { Content } = Layout

const App: React.FC = () => {
  const { isAuthenticated, isLoading } = useAuth()

  if (isLoading) {
    return <LoadingSpinner />
  }

  if (!isAuthenticated) {
    return (
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    )
  }

  return (
    <AppLayout>
      <Routes>
        <Route path="/" element={<Navigate to="/dashboard" replace />} />
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/workflow" element={<WorkflowPage />} />
        <Route path="/approval" element={<ApprovalPage />} />
        <Route path="/monitor" element={<MonitorPage />} />
        <Route path="/profile" element={<ProfilePage />} />
        <Route path="/api-test" element={<ApiTestPage />} />
        <Route path="/login" element={<Navigate to="/dashboard" replace />} />
        <Route path="*" element={<Navigate to="/dashboard" replace />} />
      </Routes>
    </AppLayout>
  )
}

export default App
