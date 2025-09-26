import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import { DesignerState, WorkflowDefinition, WorkflowNode, WorkflowEdge, ClipboardData, ViewportState, DesignerSettings } from '@/types'

const initialState: DesignerState = {
  currentWorkflow: null,
  selectedNodes: [],
  selectedEdges: [],
  clipboard: null,
  history: {
    past: [],
    present: null,
    future: []
  },
  viewport: {
    x: 0,
    y: 0,
    zoom: 1
  },
  settings: {
    snapToGrid: true,
    gridSize: 20,
    showGrid: true,
    showMinimap: true,
    autoSave: true,
    theme: 'light'
  }
}

const designerSlice = createSlice({
  name: 'designer',
  initialState,
  reducers: {
    // 工作流操作
    setCurrentWorkflow: (state, action: PayloadAction<WorkflowDefinition | null>) => {
      state.currentWorkflow = action.payload
    },
    
    updateWorkflow: (state, action: PayloadAction<Partial<WorkflowDefinition>>) => {
      if (state.currentWorkflow) {
        state.currentWorkflow = { ...state.currentWorkflow, ...action.payload }
      }
    },

    // 节点操作
    addNode: (state, action: PayloadAction<WorkflowNode>) => {
      if (state.currentWorkflow) {
        state.currentWorkflow.nodes.push(action.payload)
      }
    },

    updateNode: (state, action: PayloadAction<{ id: string; updates: Partial<WorkflowNode> }>) => {
      if (state.currentWorkflow) {
        const nodeIndex = state.currentWorkflow.nodes.findIndex(node => node.id === action.payload.id)
        if (nodeIndex !== -1) {
          state.currentWorkflow.nodes[nodeIndex] = {
            ...state.currentWorkflow.nodes[nodeIndex],
            ...action.payload.updates
          }
        }
      }
    },

    removeNode: (state, action: PayloadAction<string>) => {
      if (state.currentWorkflow) {
        state.currentWorkflow.nodes = state.currentWorkflow.nodes.filter(node => node.id !== action.payload)
        state.currentWorkflow.edges = state.currentWorkflow.edges.filter(
          edge => edge.source !== action.payload && edge.target !== action.payload
        )
      }
    },

    // 连线操作
    addEdge: (state, action: PayloadAction<WorkflowEdge>) => {
      if (state.currentWorkflow) {
        state.currentWorkflow.edges.push(action.payload)
      }
    },

    updateEdge: (state, action: PayloadAction<{ id: string; updates: Partial<WorkflowEdge> }>) => {
      if (state.currentWorkflow) {
        const edgeIndex = state.currentWorkflow.edges.findIndex(edge => edge.id === action.payload.id)
        if (edgeIndex !== -1) {
          state.currentWorkflow.edges[edgeIndex] = {
            ...state.currentWorkflow.edges[edgeIndex],
            ...action.payload.updates
          }
        }
      }
    },

    removeEdge: (state, action: PayloadAction<string>) => {
      if (state.currentWorkflow) {
        state.currentWorkflow.edges = state.currentWorkflow.edges.filter(edge => edge.id !== action.payload)
      }
    },

    // 选择操作
    selectNodes: (state, action: PayloadAction<string[]>) => {
      state.selectedNodes = action.payload
      state.selectedEdges = []
    },

    selectEdges: (state, action: PayloadAction<string[]>) => {
      state.selectedEdges = action.payload
      state.selectedNodes = []
    },

    clearSelection: (state) => {
      state.selectedNodes = []
      state.selectedEdges = []
    },

    // 剪贴板操作
    copyToClipboard: (state, action: PayloadAction<ClipboardData>) => {
      state.clipboard = action.payload
    },

    clearClipboard: (state) => {
      state.clipboard = null
    },

    // 历史操作
    pushHistory: (state, action: PayloadAction<WorkflowDefinition>) => {
      if (state.history.present) {
        state.history.past.push(state.history.present)
      }
      state.history.present = action.payload
      state.history.future = []
    },

    undo: (state) => {
      if (state.history.past.length > 0) {
        const previous = state.history.past.pop()!
        if (state.history.present) {
          state.history.future.unshift(state.history.present)
        }
        state.history.present = previous
        state.currentWorkflow = previous
      }
    },

    redo: (state) => {
      if (state.history.future.length > 0) {
        const next = state.history.future.shift()!
        if (state.history.present) {
          state.history.past.push(state.history.present)
        }
        state.history.present = next
        state.currentWorkflow = next
      }
    },

    // 视口操作
    updateViewport: (state, action: PayloadAction<Partial<ViewportState>>) => {
      state.viewport = { ...state.viewport, ...action.payload }
    },

    resetViewport: (state) => {
      state.viewport = { x: 0, y: 0, zoom: 1 }
    },

    // 设置操作
    updateSettings: (state, action: PayloadAction<Partial<DesignerSettings>>) => {
      state.settings = { ...state.settings, ...action.payload }
    }
  }
})

export const {
  setCurrentWorkflow,
  updateWorkflow,
  addNode,
  updateNode,
  removeNode,
  addEdge,
  updateEdge,
  removeEdge,
  selectNodes,
  selectEdges,
  clearSelection,
  copyToClipboard,
  clearClipboard,
  pushHistory,
  undo,
  redo,
  updateViewport,
  resetViewport,
  updateSettings
} = designerSlice.actions

export default designerSlice.reducer
