import React from 'react'
import { Card, Row, Col, Statistic, Button, Space, Typography } from 'antd'
import { 
  DashboardOutlined, 
  UserOutlined, 
  SettingOutlined, 
  BulbOutlined,
  ArrowRightOutlined
} from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'

const { Title, Paragraph } = Typography

const DashboardPage: React.FC = () => {
  const navigate = useNavigate()

  const quickActions = [
    {
      title: '用户工作台',
      description: '处理日常工作流任务',
      icon: <DashboardOutlined style={{ fontSize: '24px', color: '#1890ff' }} />,
      path: '/web',
      color: '#1890ff'
    },
    {
      title: '管理后台',
      description: '系统管理和配置',
      icon: <SettingOutlined style={{ fontSize: '24px', color: '#52c41a' }} />,
      path: '/admin',
      color: '#52c41a'
    },
    {
      title: '流程设计器',
      description: '创建和编辑工作流',
      icon: <BulbOutlined style={{ fontSize: '24px', color: '#faad14' }} />,
      path: '/designer',
      color: '#faad14'
    }
  ]

  const stats = [
    { title: '活跃用户', value: 1234, icon: <UserOutlined /> },
    { title: '运行流程', value: 56, icon: <DashboardOutlined /> },
    { title: '待办任务', value: 89, icon: <BulbOutlined /> },
    { title: '系统状态', value: '正常', icon: <SettingOutlined /> }
  ]

  return (
    <div>
      <div style={{ marginBottom: '24px' }}>
        <Title level={2}>欢迎使用 FlowMaster</Title>
        <Paragraph type="secondary">
          企业级工作流管理系统 - 统一管理平台
        </Paragraph>
      </div>

      {/* 统计卡片 */}
      <Row gutter={[16, 16]} style={{ marginBottom: '24px' }}>
        {stats.map((stat, index) => (
          <Col xs={24} sm={12} lg={6} key={index}>
            <Card>
              <Statistic
                title={stat.title}
                value={stat.value}
                prefix={stat.icon}
                valueStyle={{ color: '#1890ff' }}
              />
            </Card>
          </Col>
        ))}
      </Row>

      {/* 快速操作 */}
      <Card title="快速操作" style={{ marginBottom: '24px' }}>
        <Row gutter={[16, 16]}>
          {quickActions.map((action, index) => (
            <Col xs={24} sm={8} key={index}>
              <Card
                hoverable
                style={{ 
                  textAlign: 'center',
                  border: `2px solid ${action.color}20`,
                  borderRadius: '12px'
                }}
                bodyStyle={{ padding: '24px' }}
              >
                <Space direction="vertical" size="middle" style={{ width: '100%' }}>
                  {action.icon}
                  <div>
                    <Title level={4} style={{ margin: 0, color: action.color }}>
                      {action.title}
                    </Title>
                    <Paragraph type="secondary" style={{ margin: '8px 0 0 0' }}>
                      {action.description}
                    </Paragraph>
                  </div>
                  <Button 
                    type="primary" 
                    shape="round"
                    icon={<ArrowRightOutlined />}
                    onClick={() => navigate(action.path)}
                    style={{ backgroundColor: action.color, borderColor: action.color }}
                  >
                    进入
                  </Button>
                </Space>
              </Card>
            </Col>
          ))}
        </Row>
      </Card>

      {/* 系统信息 */}
      <Card title="系统信息">
        <Row gutter={[16, 16]}>
          <Col xs={24} sm={12}>
            <div>
              <Title level={5}>微前端架构</Title>
              <Paragraph>
                • 主容器应用：FlowMaster Shell<br/>
                • 子应用：用户工作台、管理后台、流程设计器<br/>
                • 技术栈：React + TypeScript + Ant Design
              </Paragraph>
            </div>
          </Col>
          <Col xs={24} sm={12}>
            <div>
              <Title level={5}>服务状态</Title>
              <Paragraph>
                • 后端服务：用户服务、认证服务、工作流服务<br/>
                • 数据库：MySQL 8.0<br/>
                • 缓存：Redis 7.x
              </Paragraph>
            </div>
          </Col>
        </Row>
      </Card>
    </div>
  )
}

export default DashboardPage
