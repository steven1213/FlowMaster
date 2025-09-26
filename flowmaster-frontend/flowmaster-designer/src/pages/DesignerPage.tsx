import React, { useEffect, useRef, useState } from 'react'
import { Row, Col, Card, Button, Space, Typography, message } from 'antd'
import {
  PlayCircleOutlined,
  EyeOutlined,
  DownloadOutlined,
  UploadOutlined,
  DeleteOutlined
} from '@ant-design/icons'
import { useSelector, useDispatch } from 'react-redux'
import { RootState, AppDispatch } from '@/store'
import { setCurrentWorkflow, pushHistory } from '@/store/slices/designerSlice'
import { WorkflowDefinition, NodeType } from '@/types'
import FlowCanvas from '@/designer/components/FlowCanvas'
import NodePalette from '@/designer/components/NodePalette'
import PropertyPanel from '@/designer/components/PropertyPanel'
import Minimap from '@/designer/components/Minimap'

const { Title, Text } = Typography

const DesignerPage: React.FC = () => {
  const dispatch = useDispatch<AppDispatch>()
  const { currentWorkflow, selectedNodes, selectedEdges } = useSelector((state: RootState) => state.designer)
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    // 初始化空的工作流
    if (!currentWorkflow) {
      const newWorkflow: WorkflowDefinition = {
        id: `workflow_${Date.now()}`,
        name: '新建工作流',
        description: '',
        version: '1.0.0',
        category: 'default',
        status: 'DRAFT' as any,
        nodes: [],
        edges: [],
        variables: [],
        properties: {},
        createdBy: 'user',
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      }
      dispatch(setCurrentWorkflow(newWorkflow))
    }
  }, [currentWorkflow, dispatch])

  const handleSave = async () => {
    if (!currentWorkflow) return
    
    setLoading(true)
    try {
      // 这里应该调用保存API
      await new Promise(resolve => setTimeout(resolve, 1000))
      message.success('保存成功')
    } catch (error) {
      message.error('保存失败')
    } finally {
      setLoading(false)
    }
  }

  const handlePreview = () => {
    if (!currentWorkflow) return
    message.info('预览功能开发中...')
  }

  const handlePublish = () => {
    if (!currentWorkflow) return
    message.info('发布功能开发中...')
  }

  const handleExport = () => {
    if (!currentWorkflow) return
    message.info('导出功能开发中...')
  }

  const handleImport = () => {
    message.info('导入功能开发中...')
  }

  const handleDelete = () => {
    if (selectedNodes.length === 0 && selectedEdges.length === 0) {
      message.warning('请先选择要删除的元素')
      return
    }
    message.info('删除功能开发中...')
  }

  return (
    <div style={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
      {/* 工具栏 */}
      <div style={{ marginBottom: 16 }}>
        <Row justify="space-between" align="middle">
          <Col>
            <Title level={4} style={{ margin: 0 }}>
              {currentWorkflow?.name || '工作流设计器'}
            </Title>
            <Text type="secondary">
              版本: {currentWorkflow?.version || '1.0.0'}
            </Text>
          </Col>
          <Col>
            <Space>
              <Button
                icon={<PlayCircleOutlined />}
                onClick={handlePreview}
                disabled={!currentWorkflow}
              >
                预览
              </Button>
              <Button
                icon={<EyeOutlined />}
                onClick={handlePublish}
                disabled={!currentWorkflow}
                type="primary"
              >
                发布
              </Button>
              <Button
                icon={<DownloadOutlined />}
                onClick={handleExport}
                disabled={!currentWorkflow}
              >
                导出
              </Button>
              <Button
                icon={<UploadOutlined />}
                onClick={handleImport}
              >
                导入
              </Button>
              <Button
                icon={<DeleteOutlined />}
                onClick={handleDelete}
                danger
                disabled={selectedNodes.length === 0 && selectedEdges.length === 0}
              >
                删除
              </Button>
              <Button
                type="primary"
                onClick={handleSave}
                loading={loading}
                disabled={!currentWorkflow}
              >
                保存
              </Button>
            </Space>
          </Col>
        </Row>
      </div>

      {/* 主设计区域 */}
      <Row gutter={16} style={{ flex: 1 }}>
        {/* 节点面板 */}
        <Col span={4}>
          <Card title="节点面板" size="small" style={{ height: '100%' }}>
            <NodePalette />
          </Card>
        </Col>

        {/* 画布区域 */}
        <Col span={16}>
          <Card 
            title="设计画布" 
            size="small" 
            style={{ height: '100%' }}
            bodyStyle={{ padding: 0, height: 'calc(100% - 57px)' }}
          >
            <FlowCanvas />
          </Card>
        </Col>

        {/* 属性面板 */}
        <Col span={4}>
          <Card title="属性面板" size="small" style={{ height: '100%' }}>
            <PropertyPanel />
          </Card>
        </Col>
      </Row>

      {/* 小地图 */}
      <div style={{ position: 'fixed', bottom: 20, right: 20, zIndex: 1000 }}>
        <Minimap />
      </div>
    </div>
  )
}

export default DesignerPage
