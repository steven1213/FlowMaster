import React from 'react'
import { Typography, Card } from 'antd'

const { Title } = Typography

const ProfilePage: React.FC = () => {
  return (
    <div>
      <Title level={2}>个人资料</Title>
      <Card>
        <div style={{ textAlign: 'center', padding: 50, color: '#999' }}>
          个人资料功能开发中...
        </div>
      </Card>
    </div>
  )
}

export default ProfilePage
