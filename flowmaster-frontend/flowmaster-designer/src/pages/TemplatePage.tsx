import React from 'react'
import { Typography, Card } from 'antd'

const { Title } = Typography

const TemplatePage: React.FC = () => {
  return (
    <div>
      <Title level={2}>模板库</Title>
      <Card>
        <div style={{ textAlign: 'center', padding: 50, color: '#999' }}>
          模板库功能开发中...
        </div>
      </Card>
    </div>
  )
}

export default TemplatePage
