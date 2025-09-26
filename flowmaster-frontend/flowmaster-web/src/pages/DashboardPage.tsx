import React, { useEffect, useState } from 'react'
import { Row, Col, Card, Statistic, Typography, Spin } from 'antd'
import {
  UserOutlined,
  ForkOutlined,
  CheckCircleOutlined,
  ClockCircleOutlined
} from '@ant-design/icons'
import { useAuth } from '@/hooks/useAuth'

const { Title } = Typography

interface DashboardStats {
  totalUsers: number
  activeWorkflows: number
  pendingApprovals: number
  completedTasks: number
}

const DashboardPage: React.FC = () => {
  const { user } = useAuth()
  const [loading, setLoading] = useState(true)
  const [stats, setStats] = useState<DashboardStats>({
    totalUsers: 0,
    activeWorkflows: 0,
    pendingApprovals: 0,
    completedTasks: 0
  })

  useEffect(() => {
    // 模拟数据加载
    const loadDashboardData = async () => {
      setLoading(true)
      try {
        // 这里应该调用实际的API
        await new Promise(resolve => setTimeout(resolve, 1000))
        
        setStats({
          totalUsers: 156,
          activeWorkflows: 23,
          pendingApprovals: 8,
          completedTasks: 1247
        })
      } catch (error) {
        console.error('Failed to load dashboard data:', error)
      } finally {
        setLoading(false)
      }
    }

    loadDashboardData()
  }, [])

  if (loading) {
    return (
      <div style={{ textAlign: 'center', padding: 50 }}>
        <Spin size="large" />
      </div>
    )
  }

  return (
    <div>
      <Title level={2} style={{ marginBottom: 24 }}>
        欢迎回来，{user?.username}！
      </Title>
      
      <Row gutter={[16, 16]}>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="总用户数"
              value={stats.totalUsers}
              prefix={<UserOutlined />}
              valueStyle={{ color: '#3f8600' }}
            />
          </Card>
        </Col>
        
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="活跃工作流"
              value={stats.activeWorkflows}
              prefix={<ForkOutlined />}
              valueStyle={{ color: '#1890ff' }}
            />
          </Card>
        </Col>
        
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="待审批"
              value={stats.pendingApprovals}
              prefix={<ClockCircleOutlined />}
              valueStyle={{ color: '#faad14' }}
            />
          </Card>
        </Col>
        
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="已完成任务"
              value={stats.completedTasks}
              prefix={<CheckCircleOutlined />}
              valueStyle={{ color: '#52c41a' }}
            />
          </Card>
        </Col>
      </Row>

      <Row gutter={[16, 16]} style={{ marginTop: 24 }}>
        <Col xs={24} lg={12}>
          <Card title="最近活动" style={{ height: 400 }}>
            <div style={{ textAlign: 'center', padding: 50, color: '#999' }}>
              暂无最近活动
            </div>
          </Card>
        </Col>
        
        <Col xs={24} lg={12}>
          <Card title="系统状态" style={{ height: 400 }}>
            <div style={{ textAlign: 'center', padding: 50, color: '#999' }}>
              系统运行正常
            </div>
          </Card>
        </Col>
      </Row>
    </div>
  )
}

export default DashboardPage
