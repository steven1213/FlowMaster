import React from 'react'
import { Typography } from 'antd'

const { Title } = Typography

const AuditLogPage: React.FC = () => {
  return (
    <div>
      <div className="page-header">
        <Title level={2} className="page-title">
          审计日志
        </Title>
        <p className="page-description">
          查看系统操作日志和审计记录
        </p>
      </div>
      
      <div style={{ textAlign: 'center', padding: '50px 0' }}>
        <Title level={4}>审计日志功能开发中...</Title>
        <p>即将支持日志查询、导出、统计等功能</p>
      </div>
    </div>
  )
}

export default AuditLogPage
