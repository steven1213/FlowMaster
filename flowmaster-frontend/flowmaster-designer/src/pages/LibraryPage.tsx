import React from 'react'
import { Typography, Card } from 'antd'

const { Title } = Typography

const LibraryPage: React.FC = () => {
  return (
    <div>
      <Title level={2}>组件库</Title>
      <Card>
        <div style={{ textAlign: 'center', padding: 50, color: '#999' }}>
          组件库功能开发中...
        </div>
      </Card>
    </div>
  )
}

export default LibraryPage
