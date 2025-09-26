import React from 'react'
import { Typography, Card } from 'antd'

const { Title } = Typography

const MonitorPage: React.FC = () => {
  return (
    <div>
      <Title level={2}>系统监控</Title>
      <Card>
        <div style={{ textAlign: 'center', padding: 50, color: '#999' }}>
          系统监控功能开发中...
        </div>
      </Card>
    </div>
  )
}

export default MonitorPage
