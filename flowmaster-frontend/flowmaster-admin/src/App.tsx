import React from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from 'react-query'

import AdminLayout from './components/AdminLayout'
import LoginPage from './pages/LoginPage'
import DashboardPage from './pages/DashboardPage'
import UserManagementPage from './pages/UserManagementPage'
import RoleManagementPage from './pages/RoleManagementPage'
import PermissionManagementPage from './pages/PermissionManagementPage'
import SystemConfigPage from './pages/SystemConfigPage'
import AuditLogPage from './pages/AuditLogPage'
import WorkflowManagementPage from './pages/WorkflowManagementPage'
import MonitoringPage from './pages/MonitoringPage'
import ProfilePage from './pages/ProfilePage'

// 创建React Query客户端
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 1,
      refetchOnWindowFocus: false,
    },
  },
})

const App: React.FC = () => {
  return (
    <QueryClientProvider client={queryClient}>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/" element={<AdminLayout />}>
          <Route index element={<Navigate to="/dashboard" replace />} />
          <Route path="dashboard" element={<DashboardPage />} />
          <Route path="users" element={<UserManagementPage />} />
          <Route path="roles" element={<RoleManagementPage />} />
          <Route path="permissions" element={<PermissionManagementPage />} />
          <Route path="system" element={<SystemConfigPage />} />
          <Route path="audit" element={<AuditLogPage />} />
          <Route path="workflows" element={<WorkflowManagementPage />} />
          <Route path="monitoring" element={<MonitoringPage />} />
          <Route path="profile" element={<ProfilePage />} />
        </Route>
      </Routes>
    </QueryClientProvider>
  )
}

export default App
