import React from 'react'
import { Typography } from 'antd'

const { Title } = Typography

const UserManagementPage: React.FC = () => {
  return (
    <div>
      <div className="page-header">
        <Title level={2} className="page-title">
          用户管理
        </Title>
        <p className="page-description">
          管理系统用户账号和权限分配
        </p>
      </div>
      
      <div style={{ textAlign: 'center', padding: '50px 0' }}>
        <Title level={4}>用户管理功能开发中...</Title>
        <p>即将支持用户列表、创建、编辑、删除等功能</p>
      </div>
    </div>
  )
}

export default UserManagementPage
