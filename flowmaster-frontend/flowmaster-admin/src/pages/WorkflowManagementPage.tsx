import React from 'react'
import { Typography } from 'antd'

const { Title } = Typography

const WorkflowManagementPage: React.FC = () => {
  return (
    <div>
      <div className="page-header">
        <Title level={2} className="page-title">
          工作流管理
        </Title>
        <p className="page-description">
          管理工作流定义和实例
        </p>
      </div>
      
      <div style={{ textAlign: 'center', padding: '50px 0' }}>
        <Title level={4}>工作流管理功能开发中...</Title>
        <p>即将支持工作流定义、实例管理、监控等功能</p>
      </div>
    </div>
  )
}

export default WorkflowManagementPage
