import React from 'react'
import { Form, Input, Select, InputNumber, Switch, Button, Space, Typography, Divider } from 'antd'
import { useSelector, useDispatch } from 'react-redux'
import { RootState, AppDispatch } from '@/store'
import { updateNode, updateEdge, updateWorkflow } from '@/store/slices/designerSlice'
import { WorkflowNode, WorkflowEdge, NodeType } from '@/types'

const { TextArea } = Input
const { Option } = Select
const { Title, Text } = Typography

const PropertyPanel: React.FC = () => {
  const dispatch = useDispatch<AppDispatch>()
  const { currentWorkflow, selectedNodes, selectedEdges } = useSelector((state: RootState) => state.designer)
  const [form] = Form.useForm()

  const selectedNode = selectedNodes.length === 1 
    ? currentWorkflow?.nodes.find(n => n.id === selectedNodes[0])
    : null

  const selectedEdge = selectedEdges.length === 1
    ? currentWorkflow?.edges.find(e => e.id === selectedEdges[0])
    : null

  const handleNodeUpdate = (values: any) => {
    if (selectedNode) {
      dispatch(updateNode({
        id: selectedNode.id,
        updates: values
      }))
    }
  }

  const handleEdgeUpdate = (values: any) => {
    if (selectedEdge) {
      dispatch(updateEdge({
        id: selectedEdge.id,
        updates: values
      }))
    }
  }

  const handleWorkflowUpdate = (values: any) => {
    if (currentWorkflow) {
      dispatch(updateWorkflow(values))
    }
  }

  React.useEffect(() => {
    if (selectedNode) {
      form.setFieldsValue(selectedNode)
    } else if (selectedEdge) {
      form.setFieldsValue(selectedEdge)
    } else if (currentWorkflow) {
      form.setFieldsValue({
        name: currentWorkflow.name,
        description: currentWorkflow.description,
        version: currentWorkflow.version,
        category: currentWorkflow.category
      })
    }
  }, [selectedNode, selectedEdge, currentWorkflow, form])

  const renderNodeProperties = () => {
    if (!selectedNode) return null

    return (
      <div>
        <Title level={5}>节点属性</Title>
        <Form
          form={form}
          layout="vertical"
          size="small"
          onValuesChange={handleNodeUpdate}
        >
          <Form.Item label="节点名称" name="name">
            <Input />
          </Form.Item>
          
          <Form.Item label="节点描述" name="description">
            <TextArea rows={3} />
          </Form.Item>
          
          <Form.Item label="节点类型" name="type">
            <Select disabled>
              <Option value={NodeType.START}>开始</Option>
              <Option value={NodeType.END}>结束</Option>
              <Option value={NodeType.USER_TASK}>用户任务</Option>
              <Option value={NodeType.SERVICE_TASK}>服务任务</Option>
              <Option value={NodeType.SCRIPT_TASK}>脚本任务</Option>
              <Option value={NodeType.EXCLUSIVE_GATEWAY}>排他网关</Option>
              <Option value={NodeType.PARALLEL_GATEWAY}>并行网关</Option>
              <Option value={NodeType.INCLUSIVE_GATEWAY}>包容网关</Option>
            </Select>
          </Form.Item>

          {selectedNode.type === NodeType.USER_TASK && (
            <>
              <Form.Item label="处理人" name={['data', 'assignee']}>
                <Input placeholder="请输入处理人" />
              </Form.Item>
              
              <Form.Item label="候选用户" name={['data', 'candidateUsers']}>
                <Input placeholder="多个用户用逗号分隔" />
              </Form.Item>
              
              <Form.Item label="候选组" name={['data', 'candidateGroups']}>
                <Input placeholder="多个组用逗号分隔" />
              </Form.Item>
              
              <Form.Item label="表单Key" name={['data', 'formKey']}>
                <Input placeholder="请输入表单Key" />
              </Form.Item>
            </>
          )}

          {selectedNode.type === NodeType.SERVICE_TASK && (
            <>
              <Form.Item label="服务类" name={['data', 'serviceClass']}>
                <Input placeholder="请输入服务类名" />
              </Form.Item>
              
              <Form.Item label="方法名" name={['data', 'method']}>
                <Input placeholder="请输入方法名" />
              </Form.Item>
            </>
          )}

          {selectedNode.type === NodeType.SCRIPT_TASK && (
            <Form.Item label="脚本" name={['data', 'script']}>
              <TextArea rows={6} placeholder="请输入脚本内容" />
            </Form.Item>
          )}

          <Form.Item label="优先级" name={['data', 'priority']}>
            <InputNumber min={1} max={10} placeholder="1-10" />
          </Form.Item>
        </Form>
      </div>
    )
  }

  const renderEdgeProperties = () => {
    if (!selectedEdge) return null

    return (
      <div>
        <Title level={5}>连线属性</Title>
        <Form
          form={form}
          layout="vertical"
          size="small"
          onValuesChange={handleEdgeUpdate}
        >
          <Form.Item label="连线标签" name="label">
            <Input placeholder="请输入连线标签" />
          </Form.Item>
          
          <Form.Item label="条件表达式" name="condition">
            <TextArea rows={3} placeholder="请输入条件表达式" />
          </Form.Item>
        </Form>
      </div>
    )
  }

  const renderWorkflowProperties = () => {
    if (!currentWorkflow || selectedNode || selectedEdge) return null

    return (
      <div>
        <Title level={5}>工作流属性</Title>
        <Form
          form={form}
          layout="vertical"
          size="small"
          onValuesChange={handleWorkflowUpdate}
        >
          <Form.Item label="工作流名称" name="name">
            <Input />
          </Form.Item>
          
          <Form.Item label="工作流描述" name="description">
            <TextArea rows={3} />
          </Form.Item>
          
          <Form.Item label="版本号" name="version">
            <Input />
          </Form.Item>
          
          <Form.Item label="分类" name="category">
            <Select>
              <Option value="default">默认</Option>
              <Option value="approval">审批</Option>
              <Option value="notification">通知</Option>
              <Option value="integration">集成</Option>
            </Select>
          </Form.Item>
        </Form>
      </div>
    )
  }

  const renderEmptyState = () => {
    if (selectedNode || selectedEdge || currentWorkflow) return null

    return (
      <div style={{ textAlign: 'center', padding: '20px', color: '#999' }}>
        <Text>请选择节点、连线或工作流来编辑属性</Text>
      </div>
    )
  }

  return (
    <div>
      {renderNodeProperties()}
      {renderEdgeProperties()}
      {renderWorkflowProperties()}
      {renderEmptyState()}
    </div>
  )
}

export default PropertyPanel
