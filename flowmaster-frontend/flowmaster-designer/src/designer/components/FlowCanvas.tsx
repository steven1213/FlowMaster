import React, { useEffect, useRef, useState } from 'react'
import { useSelector, useDispatch } from 'react-redux'
import { RootState, AppDispatch } from '@/store'
import { WorkflowNode, WorkflowEdge, NodeType } from '@/types'
import { addNode, addEdge, selectNodes, selectEdges, updateViewport } from '@/store/slices/designerSlice'

const FlowCanvas: React.FC = () => {
  const canvasRef = useRef<HTMLDivElement>(null)
  const dispatch = useDispatch<AppDispatch>()
  const { currentWorkflow, selectedNodes, selectedEdges, viewport } = useSelector((state: RootState) => state.designer)
  const [isDragging, setIsDragging] = useState(false)
  const [dragStart, setDragStart] = useState({ x: 0, y: 0 })

  useEffect(() => {
    // 初始化画布
    if (canvasRef.current) {
      // 这里应该初始化G6或X6图形引擎
      console.log('初始化画布')
    }
  }, [])

  const handleCanvasClick = (e: React.MouseEvent) => {
    // 点击空白区域清除选择
    if (e.target === canvasRef.current) {
      dispatch(selectNodes([]))
      dispatch(selectEdges([]))
    }
  }

  const handleCanvasMouseDown = (e: React.MouseEvent) => {
    if (e.target === canvasRef.current) {
      setIsDragging(true)
      setDragStart({ x: e.clientX, y: e.clientY })
    }
  }

  const handleCanvasMouseMove = (e: React.MouseEvent) => {
    if (isDragging) {
      const deltaX = e.clientX - dragStart.x
      const deltaY = e.clientY - dragStart.y
      dispatch(updateViewport({
        x: viewport.x + deltaX,
        y: viewport.y + deltaY
      }))
      setDragStart({ x: e.clientX, y: e.clientY })
    }
  }

  const handleCanvasMouseUp = () => {
    setIsDragging(false)
  }

  const handleWheel = (e: React.WheelEvent) => {
    e.preventDefault()
    const delta = e.deltaY > 0 ? 0.9 : 1.1
    const newZoom = Math.max(0.1, Math.min(3, viewport.zoom * delta))
    dispatch(updateViewport({ zoom: newZoom }))
  }

  const renderNode = (node: WorkflowNode) => {
    const isSelected = selectedNodes.includes(node.id)
    const style = {
      position: 'absolute' as const,
      left: node.position.x,
      top: node.position.y,
      width: node.size.width,
      height: node.size.height,
      border: isSelected ? '2px solid #1890ff' : '1px solid #d9d9d9',
      borderRadius: '4px',
      backgroundColor: '#fff',
      cursor: 'pointer',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      fontSize: '12px',
      boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
      ...node.style
    }

    return (
      <div
        key={node.id}
        style={style}
        onClick={(e) => {
          e.stopPropagation()
          dispatch(selectNodes([node.id]))
        }}
      >
        {node.name}
      </div>
    )
  }

  const renderEdge = (edge: WorkflowEdge) => {
    const sourceNode = currentWorkflow?.nodes.find(n => n.id === edge.source)
    const targetNode = currentWorkflow?.nodes.find(n => n.id === edge.target)
    
    if (!sourceNode || !targetNode) return null

    const isSelected = selectedEdges.includes(edge.id)
    const style = {
      stroke: isSelected ? '#1890ff' : '#d9d9d9',
      strokeWidth: isSelected ? 2 : 1,
      ...edge.style
    }

    // 简化的连线渲染
    return (
      <svg
        key={edge.id}
        style={{
          position: 'absolute',
          top: 0,
          left: 0,
          width: '100%',
          height: '100%',
          pointerEvents: 'none',
          zIndex: 1
        }}
      >
        <line
          x1={sourceNode.position.x + sourceNode.size.width / 2}
          y1={sourceNode.position.y + sourceNode.size.height / 2}
          x2={targetNode.position.x + targetNode.size.width / 2}
          y2={targetNode.position.y + targetNode.size.height / 2}
          stroke={style.stroke}
          strokeWidth={style.strokeWidth}
          onClick={(e) => {
            e.stopPropagation()
            dispatch(selectEdges([edge.id]))
          }}
          style={{ pointerEvents: 'auto', cursor: 'pointer' }}
        />
      </svg>
    )
  }

  return (
    <div
      ref={canvasRef}
      style={{
        width: '100%',
        height: '100%',
        position: 'relative',
        overflow: 'hidden',
        backgroundColor: '#f5f5f5',
        cursor: isDragging ? 'grabbing' : 'grab'
      }}
      onClick={handleCanvasClick}
      onMouseDown={handleCanvasMouseDown}
      onMouseMove={handleCanvasMouseMove}
      onMouseUp={handleCanvasMouseUp}
      onWheel={handleWheel}
    >
      {/* 网格背景 */}
      <div
        style={{
          position: 'absolute',
          top: 0,
          left: 0,
          width: '100%',
          height: '100%',
          backgroundImage: `
            linear-gradient(rgba(0,0,0,0.1) 1px, transparent 1px),
            linear-gradient(90deg, rgba(0,0,0,0.1) 1px, transparent 1px)
          `,
          backgroundSize: '20px 20px',
          transform: `translate(${viewport.x}px, ${viewport.y}px) scale(${viewport.zoom})`,
          transformOrigin: '0 0'
        }}
      />

      {/* 节点和连线 */}
      <div
        style={{
          position: 'absolute',
          top: 0,
          left: 0,
          width: '100%',
          height: '100%',
          transform: `translate(${viewport.x}px, ${viewport.y}px) scale(${viewport.zoom})`,
          transformOrigin: '0 0'
        }}
      >
        {/* 连线 */}
        {currentWorkflow?.edges.map(renderEdge)}
        
        {/* 节点 */}
        {currentWorkflow?.nodes.map(renderNode)}
      </div>

      {/* 缩放指示器 */}
      <div
        style={{
          position: 'absolute',
          bottom: 10,
          left: 10,
          backgroundColor: 'rgba(0,0,0,0.7)',
          color: 'white',
          padding: '4px 8px',
          borderRadius: '4px',
          fontSize: '12px'
        }}
      >
        {Math.round(viewport.zoom * 100)}%
      </div>
    </div>
  )
}

export default FlowCanvas
