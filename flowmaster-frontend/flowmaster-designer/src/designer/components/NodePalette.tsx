import React from 'react'
import { Button, Space, Typography, Divider } from 'antd'
import {
  PlayCircleOutlined,
  StopOutlined,
  UserOutlined,
  SettingOutlined,
  GatewayOutlined,
  ClockCircleOutlined,
  MessageOutlined,
  SkinOutlined,
  AppstoreOutlined,
  TeamOutlined
} from '@ant-design/icons'
import { useSelector, useDispatch } from 'react-redux'
import { RootState, AppDispatch } from '@/store'
import { addNode } from '@/store/slices/designerSlice'
import { WorkflowNode, NodeType } from '@/types'

const { Text } = Typography

const NodePalette: React.FC = () => {
  const dispatch = useDispatch<AppDispatch>()
  const { currentWorkflow } = useSelector((state: RootState) => state.designer)

  const nodeTypes = [
    {
      type: NodeType.START,
      name: '开始',
      icon: <PlayCircleOutlined />,
      color: '#52c41a'
    },
    {
      type: NodeType.END,
      name: '结束',
      icon: <StopOutlined />,
      color: '#f5222d'
    },
    {
      type: NodeType.USER_TASK,
      name: '用户任务',
      icon: <UserOutlined />,
      color: '#1890ff'
    },
    {
      type: NodeType.SERVICE_TASK,
      name: '服务任务',
      icon: <SettingOutlined />,
      color: '#722ed1'
    },
    {
      type: NodeType.SCRIPT_TASK,
      name: '脚本任务',
      icon: <SettingOutlined />,
      color: '#fa8c16'
    },
    {
      type: NodeType.EXCLUSIVE_GATEWAY,
      name: '排他网关',
      icon: <GatewayOutlined />,
      color: '#13c2c2'
    },
    {
      type: NodeType.PARALLEL_GATEWAY,
      name: '并行网关',
      icon: <GatewayOutlined />,
      color: '#13c2c2'
    },
    {
      type: NodeType.INCLUSIVE_GATEWAY,
      name: '包容网关',
      icon: <GatewayOutlined />,
      color: '#13c2c2'
    },
    {
      type: NodeType.TIMER_EVENT,
      name: '定时事件',
      icon: <ClockCircleOutlined />,
      color: '#eb2f96'
    },
    {
      type: NodeType.MESSAGE_EVENT,
      name: '消息事件',
      icon: <MessageOutlined />,
      color: '#eb2f96'
    },
    {
      type: NodeType.SIGNAL_EVENT,
      name: '信号事件',
      icon: <SignalOutlined />,
      color: '#eb2f96'
    },
    {
      type: NodeType.SUBPROCESS,
      name: '子流程',
      icon: <AppstoreOutlined />,
      color: '#2f54eb'
    },
    {
      type: NodeType.POOL,
      name: '泳道',
      icon: <TeamOutlined />,
      color: '#52c41a'
    }
  ]

  const handleAddNode = (nodeType: NodeType, nodeName: string) => {
    if (!currentWorkflow) return

    const newNode: WorkflowNode = {
      id: `node_${Date.now()}`,
      type: nodeType,
      name: nodeName,
      description: '',
      position: { x: 100, y: 100 },
      size: { width: 120, height: 60 },
      data: {},
      style: {}
    }

    dispatch(addNode(newNode))
  }

  const renderNodeType = (nodeType: any) => (
    <Button
      key={nodeType.type}
      type="text"
      block
      style={{
        height: 'auto',
        padding: '8px',
        marginBottom: '4px',
        textAlign: 'left',
        border: '1px solid #d9d9d9',
        borderRadius: '4px'
      }}
      onClick={() => handleAddNode(nodeType.type, nodeType.name)}
    >
      <Space>
        <span style={{ color: nodeType.color }}>
          {nodeType.icon}
        </span>
        <Text style={{ fontSize: '12px' }}>
          {nodeType.name}
        </Text>
      </Space>
    </Button>
  )

  return (
    <div>
      <Text strong style={{ fontSize: '14px', marginBottom: '8px', display: 'block' }}>
        基础节点
      </Text>
      {renderNodeType(nodeTypes[0])} {/* 开始 */}
      {renderNodeType(nodeTypes[1])} {/* 结束 */}
      
      <Divider style={{ margin: '12px 0' }} />
      
      <Text strong style={{ fontSize: '14px', marginBottom: '8px', display: 'block' }}>
        任务节点
      </Text>
      {nodeTypes.slice(2, 5).map(renderNodeType)}
      
      <Divider style={{ margin: '12px 0' }} />
      
      <Text strong style={{ fontSize: '14px', marginBottom: '8px', display: 'block' }}>
        网关节点
      </Text>
      {nodeTypes.slice(5, 8).map(renderNodeType)}
      
      <Divider style={{ margin: '12px 0' }} />
      
      <Text strong style={{ fontSize: '14px', marginBottom: '8px', display: 'block' }}>
        事件节点
      </Text>
      {nodeTypes.slice(8, 11).map(renderNodeType)}
      
      <Divider style={{ margin: '12px 0' }} />
      
      <Text strong style={{ fontSize: '14px', marginBottom: '8px', display: 'block' }}>
        其他节点
      </Text>
      {nodeTypes.slice(11).map(renderNodeType)}
    </div>
  )
}

export default NodePalette
