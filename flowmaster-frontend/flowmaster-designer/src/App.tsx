import React from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import { Layout } from 'antd'

import DesignerLayout from '@/components/DesignerLayout'
import DesignerPage from '@/pages/DesignerPage'
import TemplatePage from '@/pages/TemplatePage'
import LibraryPage from '@/pages/LibraryPage'
// import LoadingSpinner from '@/components/LoadingSpinner'

// const { Content } = Layout

const App: React.FC = () => {
  return (
    <DesignerLayout>
      <Routes>
        <Route path="/" element={<Navigate to="/designer" replace />} />
        <Route path="/designer" element={<DesignerPage />} />
        <Route path="/template" element={<TemplatePage />} />
        <Route path="/library" element={<LibraryPage />} />
        <Route path="*" element={<Navigate to="/designer" replace />} />
      </Routes>
    </DesignerLayout>
  )
}

export default App
