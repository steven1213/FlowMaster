import React, { useEffect } from 'react'
import { Row, Col, Card, Statistic, Typography, Spin } from 'antd'
import {
  UserOutlined,
  TeamOutlined,
  ApartmentOutlined,
  FileTextOutlined,
  CheckCircleOutlined,
  ClockCircleOutlined
} from '@ant-design/icons'
import { useDispatch, useSelector } from 'react-redux'
import { AppDispatch, RootState } from '@/store'
import { fetchDashboardStats } from '@/store/slices/dashboardSlice'

const { Title } = Typography

const DashboardPage: React.FC = () => {
  const dispatch = useDispatch<AppDispatch>()
  const { stats, loading } = useSelector((state: RootState) => state.dashboard)

  useEffect(() => {
    dispatch(fetchDashboardStats())
  }, [dispatch])

  if (loading) {
    return (
      <div style={{ textAlign: 'center', padding: '50px' }}>
        <Spin size="large" />
      </div>
    )
  }

  return (
    <div>
      <div className="page-header">
        <Title level={2} className="page-title">
          仪表板
        </Title>
        <p className="page-description">
          系统概览和关键指标监控
        </p>
      </div>

      <Row gutter={[16, 16]}>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="总用户数"
              value={stats?.totalUsers || 0}
              prefix={<UserOutlined />}
              valueStyle={{ color: '#3f8600' }}
            />
          </Card>
        </Col>
        
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="活跃用户"
              value={stats?.activeUsers || 0}
              prefix={<CheckCircleOutlined />}
              valueStyle={{ color: '#1890ff' }}
            />
          </Card>
        </Col>
        
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="总角色数"
              value={stats?.totalRoles || 0}
              prefix={<TeamOutlined />}
              valueStyle={{ color: '#722ed1' }}
            />
          </Card>
        </Col>
        
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="总权限数"
              value={stats?.totalPermissions || 0}
              prefix={<FileTextOutlined />}
              valueStyle={{ color: '#fa8c16' }}
            />
          </Card>
        </Col>
        
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="工作流定义"
              value={stats?.totalWorkflows || 0}
              prefix={<ApartmentOutlined />}
              valueStyle={{ color: '#13c2c2' }}
            />
          </Card>
        </Col>
        
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="运行中工作流"
              value={stats?.runningWorkflows || 0}
              prefix={<ClockCircleOutlined />}
              valueStyle={{ color: '#eb2f96' }}
            />
          </Card>
        </Col>
        
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="审计日志"
              value={stats?.totalAuditLogs || 0}
              prefix={<FileTextOutlined />}
              valueStyle={{ color: '#52c41a' }}
            />
          </Card>
        </Col>
        
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="系统运行时间"
              value={stats?.systemUptime || 0}
              suffix="小时"
              valueStyle={{ color: '#fa541c' }}
            />
          </Card>
        </Col>
      </Row>

      <Row gutter={[16, 16]} style={{ marginTop: 24 }}>
        <Col xs={24} lg={12}>
          <Card title="系统状态" style={{ height: 300 }}>
            <div style={{ textAlign: 'center', padding: '50px 0' }}>
              <CheckCircleOutlined style={{ fontSize: 48, color: '#52c41a' }} />
              <p style={{ marginTop: 16, fontSize: 16 }}>系统运行正常</p>
              <p style={{ color: '#666' }}>所有服务状态良好</p>
            </div>
          </Card>
        </Col>
        
        <Col xs={24} lg={12}>
          <Card title="最近活动" style={{ height: 300 }}>
            <div style={{ padding: '20px 0' }}>
              <p>• 用户 admin 登录系统</p>
              <p>• 工作流 "请假申请" 被部署</p>
              <p>• 角色 "管理员" 权限更新</p>
              <p>• 系统配置 "邮件服务" 修改</p>
              <p>• 审计日志导出完成</p>
            </div>
          </Card>
        </Col>
      </Row>
    </div>
  )
}

export default DashboardPage
