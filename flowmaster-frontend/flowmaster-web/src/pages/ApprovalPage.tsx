import React from 'react'
import { Typography, Card } from 'antd'

const { Title } = Typography

const ApprovalPage: React.FC = () => {
  return (
    <div>
      <Title level={2}>审批管理</Title>
      <Card>
        <div style={{ textAlign: 'center', padding: 50, color: '#999' }}>
          审批管理功能开发中...
        </div>
      </Card>
    </div>
  )
}

export default ApprovalPage
