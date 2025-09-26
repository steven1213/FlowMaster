import React from 'react'
import { Typography } from 'antd'

const { Title } = Typography

const PermissionManagementPage: React.FC = () => {
  return (
    <div>
      <div className="page-header">
        <Title level={2} className="page-title">
          权限管理
        </Title>
        <p className="page-description">
          管理系统权限和菜单配置
        </p>
      </div>
      
      <div style={{ textAlign: 'center', padding: '50px 0' }}>
        <Title level={4}>权限管理功能开发中...</Title>
        <p>即将支持权限树、创建、编辑、删除等功能</p>
      </div>
    </div>
  )
}

export default PermissionManagementPage
