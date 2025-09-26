import React from 'react'
import { Card } from 'antd'
import { useSelector } from 'react-redux'
import { RootState } from '@/store'

const Minimap: React.FC = () => {
  const { currentWorkflow, viewport } = useSelector((state: RootState) => state.designer)

  if (!currentWorkflow) return null

  const renderMinimapNode = (node: any) => {
    const scale = 0.1 // 缩放比例
    const style = {
      position: 'absolute' as const,
      left: node.position.x * scale,
      top: node.position.y * scale,
      width: node.size.width * scale,
      height: node.size.height * scale,
      backgroundColor: '#1890ff',
      borderRadius: '2px',
      fontSize: '8px',
      color: 'white',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      border: '1px solid #fff'
    }

    return (
      <div key={node.id} style={style}>
        {node.name.length > 6 ? node.name.substring(0, 6) + '...' : node.name}
      </div>
    )
  }

  const renderMinimapEdge = (edge: any) => {
    const sourceNode = currentWorkflow?.nodes.find(n => n.id === edge.source)
    const targetNode = currentWorkflow?.nodes.find(n => n.id === edge.target)
    
    if (!sourceNode || !targetNode) return null

    const scale = 0.1
    const style = {
      position: 'absolute' as const,
      top: 0,
      left: 0,
      width: '100%',
      height: '100%',
      pointerEvents: 'none' as const,
      zIndex: 1
    }

    return (
      <svg key={edge.id} style={style}>
        <line
          x1={sourceNode.position.x * scale + sourceNode.size.width * scale / 2}
          y1={sourceNode.position.y * scale + sourceNode.size.height * scale / 2}
          x2={targetNode.position.x * scale + targetNode.size.width * scale / 2}
          y2={targetNode.position.y * scale + targetNode.size.height * scale / 2}
          stroke="#d9d9d9"
          strokeWidth="1"
        />
      </svg>
    )
  }

  return (
    <Card
      size="small"
      style={{
        width: 200,
        height: 150,
        position: 'relative',
        overflow: 'hidden'
      }}
      bodyStyle={{ padding: 8 }}
    >
      <div
        style={{
          width: '100%',
          height: '100%',
          position: 'relative',
          backgroundColor: '#f5f5f5',
          borderRadius: '4px'
        }}
      >
        {/* 连线 */}
        {currentWorkflow.edges.map(renderMinimapEdge)}
        
        {/* 节点 */}
        {currentWorkflow.nodes.map(renderMinimapNode)}
        
        {/* 视口指示器 */}
        <div
          style={{
            position: 'absolute',
            border: '2px solid #1890ff',
            backgroundColor: 'rgba(24, 144, 255, 0.1)',
            borderRadius: '2px',
            pointerEvents: 'none'
          }}
        />
      </div>
    </Card>
  )
}

export default Minimap
