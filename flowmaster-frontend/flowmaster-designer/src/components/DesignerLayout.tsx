import React, { useState } from 'react'
import { Layout, Menu, Button, Space, Typography, theme } from 'antd'
import {
  AppstoreOutlined,
  FileTextOutlined,
  DatabaseOutlined,
  SaveOutlined,
  UndoOutlined,
  RedoOutlined,
  ZoomInOutlined,
  ZoomOutOutlined,
  FullscreenOutlined,
  SettingOutlined
} from '@ant-design/icons'
import { useNavigate, useLocation } from 'react-router-dom'
import { useSelector, useDispatch } from 'react-redux'
import { RootState, AppDispatch } from '@/store'
import { undo, redo, updateViewport, resetViewport } from '@/store/slices/designerSlice'

const { Header, Sider, Content } = Layout
const { Title } = Typography

interface DesignerLayoutProps {
  children: React.ReactNode
}

const DesignerLayout: React.FC<DesignerLayoutProps> = ({ children }) => {
  const [collapsed, setCollapsed] = useState(false)
  const navigate = useNavigate()
  const location = useLocation()
  const dispatch = useDispatch<AppDispatch>()
  const { viewport, history } = useSelector((state: RootState) => state.designer)
  const {
    token: { colorBgContainer, borderRadiusLG }
  } = theme.useToken()

  const menuItems = [
    {
      key: '/designer',
      icon: <AppstoreOutlined />,
      label: '设计器'
    },
    {
      key: '/template',
      icon: <FileTextOutlined />,
      label: '模板库'
    },
    {
      key: '/library',
      icon: <DatabaseOutlined />,
      label: '组件库'
    }
  ]

  const handleMenuClick = ({ key }: { key: string }) => {
    navigate(key)
  }

  const handleUndo = () => {
    dispatch(undo())
  }

  const handleRedo = () => {
    dispatch(redo())
  }

  const handleZoomIn = () => {
    dispatch(updateViewport({ zoom: Math.min(viewport.zoom * 1.2, 3) }))
  }

  const handleZoomOut = () => {
    dispatch(updateViewport({ zoom: Math.max(viewport.zoom / 1.2, 0.1) }))
  }

  const handleResetView = () => {
    dispatch(resetViewport())
  }

  const canUndo = history.past.length > 0
  const canRedo = history.future.length > 0

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider
        trigger={null}
        collapsible
        collapsed={collapsed}
        style={{
          background: colorBgContainer
        }}
      >
        <div
          style={{
            height: 32,
            margin: 16,
            background: 'rgba(0, 0, 0, 0.2)',
            borderRadius: 6,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: '#1890ff',
            fontWeight: 'bold',
            fontSize: collapsed ? 12 : 16
          }}
        >
          {collapsed ? 'FD' : 'FlowMaster Designer'}
        </div>
        <Menu
          theme="light"
          mode="inline"
          selectedKeys={[location.pathname]}
          items={menuItems}
          onClick={handleMenuClick}
        />
      </Sider>
      <Layout>
        <Header
          style={{
            padding: 0,
            background: colorBgContainer,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between',
            paddingRight: 24
          }}
        >
          <div style={{ display: 'flex', alignItems: 'center' }}>
            <Button
              type="text"
              icon={collapsed ? <FullscreenOutlined /> : <AppstoreOutlined />}
              onClick={() => setCollapsed(!collapsed)}
              style={{
                fontSize: '16px',
                width: 64,
                height: 64
              }}
            />
            
            {location.pathname === '/designer' && (
              <Space style={{ marginLeft: 16 }}>
                <Button
                  icon={<UndoOutlined />}
                  onClick={handleUndo}
                  disabled={!canUndo}
                  title="撤销"
                />
                <Button
                  icon={<RedoOutlined />}
                  onClick={handleRedo}
                  disabled={!canRedo}
                  title="重做"
                />
                <Button
                  icon={<ZoomInOutlined />}
                  onClick={handleZoomIn}
                  title="放大"
                />
                <Button
                  icon={<ZoomOutOutlined />}
                  onClick={handleZoomOut}
                  title="缩小"
                />
                <Button
                  icon={<FullscreenOutlined />}
                  onClick={handleResetView}
                  title="重置视图"
                />
              </Space>
            )}
          </div>
          
          <Space>
            <Button icon={<SaveOutlined />} type="primary">
              保存
            </Button>
            <Button icon={<SettingOutlined />}>
              设置
            </Button>
          </Space>
        </Header>
        <Content
          style={{
            margin: '24px 16px',
            padding: 24,
            minHeight: 280,
            background: colorBgContainer,
            borderRadius: borderRadiusLG
          }}
        >
          {children}
        </Content>
      </Layout>
    </Layout>
  )
}

export default DesignerLayout
