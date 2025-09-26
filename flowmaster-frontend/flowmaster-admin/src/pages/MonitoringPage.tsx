import React from 'react'
import { Typography } from 'antd'

const { Title } = Typography

const MonitoringPage: React.FC = () => {
  return (
    <div>
      <div className="page-header">
        <Title level={2} className="page-title">
          系统监控
        </Title>
        <p className="page-description">
          监控系统性能和运行状态
        </p>
      </div>
      
      <div style={{ textAlign: 'center', padding: '50px 0' }}>
        <Title level={4}>系统监控功能开发中...</Title>
        <p>即将支持性能监控、指标图表、告警等功能</p>
      </div>
    </div>
  )
}

export default MonitoringPage
