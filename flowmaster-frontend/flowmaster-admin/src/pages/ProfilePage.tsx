import React from 'react'
import { Typography } from 'antd'

const { Title } = Typography

const ProfilePage: React.FC = () => {
  return (
    <div>
      <div className="page-header">
        <Title level={2} className="page-title">
          个人资料
        </Title>
        <p className="page-description">
          管理个人信息和账户设置
        </p>
      </div>
      
      <div style={{ textAlign: 'center', padding: '50px 0' }}>
        <Title level={4}>个人资料功能开发中...</Title>
        <p>即将支持个人信息编辑、密码修改等功能</p>
      </div>
    </div>
  )
}

export default ProfilePage
