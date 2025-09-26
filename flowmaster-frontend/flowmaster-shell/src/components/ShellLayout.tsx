import React, { useState } from 'react'
import { Layout, Menu, Avatar, Dropdown, Button, Switch, theme } from 'antd'
import {
  DashboardOutlined,
  UserOutlined,
  SettingOutlined,
  LogoutOutlined,
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  BulbOutlined,
  GlobalOutlined
} from '@ant-design/icons'
import { useNavigate, useLocation } from 'react-router-dom'
import { authService } from '@/services/authService'
import { microAppCommunicator, MessageType } from '@/services/communication'

const { Header, Sider, Content } = Layout

interface ShellLayoutProps {
  children: React.ReactNode
}

const ShellLayout: React.FC<ShellLayoutProps> = ({ children }) => {
  const [collapsed, setCollapsed] = useState(false)
  const [darkMode, setDarkMode] = useState(false)
  const navigate = useNavigate()
  const location = useLocation()
  const { token } = theme.useToken()

  // 菜单项配置
  const menuItems = [
    {
      key: '/web',
      icon: <DashboardOutlined />,
      label: '用户工作台',
      description: '日常工作流操作与任务管理'
    },
    {
      key: '/admin',
      icon: <SettingOutlined />,
      label: '管理后台',
      description: '系统配置、用户权限与数据管理'
    },
    {
      key: '/designer',
      icon: <UserOutlined />,
      label: '流程设计器',
      description: '可视化创建与编辑工作流模型'
    }
  ]

  // 用户菜单
  const userMenuItems = [
    {
      key: 'profile',
      icon: <UserOutlined />,
      label: '个人中心'
    },
    {
      key: 'settings',
      icon: <SettingOutlined />,
      label: '系统设置'
    },
    {
      key: 'divider',
      type: 'divider' as const
    },
    {
      key: 'logout',
      icon: <LogoutOutlined />,
      label: '退出登录'
    }
  ]

  const handleMenuClick = ({ key }: { key: string }) => {
    navigate(key)
  }

  const handleUserMenuClick = ({ key }: { key: string }) => {
    switch (key) {
      case 'profile':
        navigate('/profile')
        break
      case 'settings':
        navigate('/settings')
        break
      case 'logout':
        handleLogout()
        break
    }
  }

  const handleLogout = async () => {
    try {
      await authService.logout()
      microAppCommunicator.clearAuthData()
      // 页面会通过认证状态变化自动跳转到登录页
    } catch (error) {
      console.error('Logout error:', error)
    }
  }

  const toggleDarkMode = (checked: boolean) => {
    setDarkMode(checked)
    // 这里可以添加主题切换逻辑
  }

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider 
        trigger={null} 
        collapsible 
        collapsed={collapsed}
        style={{
          background: token.colorBgContainer,
          borderRight: `1px solid ${token.colorBorder}`
        }}
      >
        <div style={{ 
          padding: '16px', 
          textAlign: 'center',
          borderBottom: `1px solid ${token.colorBorder}`
        }}>
          <div style={{ 
            fontSize: collapsed ? '16px' : '20px',
            fontWeight: 'bold',
            color: token.colorPrimary
          }}>
            {collapsed ? 'FM' : 'FlowMaster'}
          </div>
        </div>
        
        <Menu
          mode="inline"
          selectedKeys={[location.pathname]}
          items={menuItems}
          onClick={handleMenuClick}
          style={{ border: 'none' }}
        />
      </Sider>
      
      <Layout>
        <Header style={{ 
          padding: '0 24px', 
          background: token.colorBgContainer,
          borderBottom: `1px solid ${token.colorBorder}`,
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between'
        }}>
          <Button
            type="text"
            icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
            onClick={() => setCollapsed(!collapsed)}
            style={{ fontSize: '16px', width: 64, height: 64 }}
          />
          
          <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
            <Switch
              checkedChildren={<BulbOutlined />}
              unCheckedChildren={<BulbOutlined />}
              checked={darkMode}
              onChange={toggleDarkMode}
            />
            
            <Button 
              type="text" 
              icon={<GlobalOutlined />}
              onClick={() => console.log('切换语言')}
            />
            
            <Dropdown
              menu={{ 
                items: userMenuItems,
                onClick: handleUserMenuClick
              }}
              placement="bottomRight"
            >
              <div style={{ 
                display: 'flex', 
                alignItems: 'center', 
                cursor: 'pointer',
                padding: '8px 12px',
                borderRadius: '6px',
                transition: 'background-color 0.3s'
              }}>
                <Avatar 
                  size="small" 
                  icon={<UserOutlined />}
                  style={{ marginRight: '8px' }}
                />
                <span>管理员</span>
              </div>
            </Dropdown>
          </div>
        </Header>
        
        <Content style={{ 
          margin: '24px 16px',
          padding: '24px',
          background: token.colorBgContainer,
          borderRadius: '8px',
          minHeight: 'calc(100vh - 112px)'
        }}>
          {children}
        </Content>
      </Layout>
    </Layout>
  )
}

export default ShellLayout
