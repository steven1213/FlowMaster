import React from 'react'
import { Typography, Card } from 'antd'

const { Title } = Typography

const WorkflowPage: React.FC = () => {
  return (
    <div>
      <Title level={2}>工作流管理</Title>
      <Card>
        <div style={{ textAlign: 'center', padding: 50, color: '#999' }}>
          工作流管理功能开发中...
        </div>
      </Card>
    </div>
  )
}

export default WorkflowPage
