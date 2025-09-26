import React from 'react'
import { Typography } from 'antd'

const { Title } = Typography

const SystemConfigPage: React.FC = () => {
  return (
    <div>
      <div className="page-header">
        <Title level={2} className="page-title">
          系统配置
        </Title>
        <p className="page-description">
          管理系统配置参数和设置
        </p>
      </div>
      
      <div style={{ textAlign: 'center', padding: '50px 0' }}>
        <Title level={4}>系统配置功能开发中...</Title>
        <p>即将支持配置管理、参数设置等功能</p>
      </div>
    </div>
  )
}

export default SystemConfigPage
