import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit'
import { WorkflowDefinition, WorkflowInstance, WorkflowQuery, PageResult } from '@/types'
import { workflowService } from '@/services/workflowService'

interface WorkflowState {
  definitions: WorkflowDefinition[]
  instances: WorkflowInstance[]
  totalDefinitions: number
  totalInstances: number
  loading: boolean
  error: string | null
  selectedDefinition: WorkflowDefinition | null
  selectedInstance: WorkflowInstance | null
}

const initialState: WorkflowState = {
  definitions: [],
  instances: [],
  totalDefinitions: 0,
  totalInstances: 0,
  loading: false,
  error: null,
  selectedDefinition: null,
  selectedInstance: null
}

// 异步actions
export const fetchWorkflowDefinitions = createAsyncThunk(
  'workflow/fetchWorkflowDefinitions',
  async (query: WorkflowQuery, { rejectWithValue }) => {
    try {
      const response = await workflowService.getWorkflowDefinitions(query)
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '获取工作流定义失败')
    }
  }
)

export const fetchWorkflowInstances = createAsyncThunk(
  'workflow/fetchWorkflowInstances',
  async (query: WorkflowQuery, { rejectWithValue }) => {
    try {
      const response = await workflowService.getWorkflowInstances(query)
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '获取工作流实例失败')
    }
  }
)

export const deployWorkflow = createAsyncThunk(
  'workflow/deployWorkflow',
  async (definitionId: string, { rejectWithValue }) => {
    try {
      const response = await workflowService.deployWorkflow(definitionId)
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '部署工作流失败')
    }
  }
)

export const suspendWorkflow = createAsyncThunk(
  'workflow/suspendWorkflow',
  async (instanceId: string, { rejectWithValue }) => {
    try {
      const response = await workflowService.suspendWorkflow(instanceId)
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '挂起工作流失败')
    }
  }
)

export const activateWorkflow = createAsyncThunk(
  'workflow/activateWorkflow',
  async (instanceId: string, { rejectWithValue }) => {
    try {
      const response = await workflowService.activateWorkflow(instanceId)
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '激活工作流失败')
    }
  }
)

export const terminateWorkflow = createAsyncThunk(
  'workflow/terminateWorkflow',
  async (instanceId: string, { rejectWithValue }) => {
    try {
      const response = await workflowService.terminateWorkflow(instanceId)
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '终止工作流失败')
    }
  }
)

const workflowSlice = createSlice({
  name: 'workflow',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null
    },
    setSelectedDefinition: (state, action: PayloadAction<WorkflowDefinition | null>) => {
      state.selectedDefinition = action.payload
    },
    setSelectedInstance: (state, action: PayloadAction<WorkflowInstance | null>) => {
      state.selectedInstance = action.payload
    },
    clearSelectedDefinition: (state) => {
      state.selectedDefinition = null
    },
    clearSelectedInstance: (state) => {
      state.selectedInstance = null
    }
  },
  extraReducers: (builder) => {
    builder
      // 获取工作流定义
      .addCase(fetchWorkflowDefinitions.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(fetchWorkflowDefinitions.fulfilled, (state, action) => {
        state.loading = false
        state.definitions = action.payload.content
        state.totalDefinitions = action.payload.totalElements
        state.error = null
      })
      .addCase(fetchWorkflowDefinitions.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
      // 获取工作流实例
      .addCase(fetchWorkflowInstances.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(fetchWorkflowInstances.fulfilled, (state, action) => {
        state.loading = false
        state.instances = action.payload.content
        state.totalInstances = action.payload.totalElements
        state.error = null
      })
      .addCase(fetchWorkflowInstances.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
      // 部署工作流
      .addCase(deployWorkflow.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(deployWorkflow.fulfilled, (state, action) => {
        state.loading = false
        const index = state.definitions.findIndex(def => def.id === action.payload.id)
        if (index !== -1) {
          state.definitions[index] = action.payload
        }
        state.error = null
      })
      .addCase(deployWorkflow.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
      // 挂起工作流
      .addCase(suspendWorkflow.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(suspendWorkflow.fulfilled, (state, action) => {
        state.loading = false
        const index = state.instances.findIndex(instance => instance.id === action.payload.id)
        if (index !== -1) {
          state.instances[index] = action.payload
        }
        state.error = null
      })
      .addCase(suspendWorkflow.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
      // 激活工作流
      .addCase(activateWorkflow.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(activateWorkflow.fulfilled, (state, action) => {
        state.loading = false
        const index = state.instances.findIndex(instance => instance.id === action.payload.id)
        if (index !== -1) {
          state.instances[index] = action.payload
        }
        state.error = null
      })
      .addCase(activateWorkflow.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
      // 终止工作流
      .addCase(terminateWorkflow.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(terminateWorkflow.fulfilled, (state, action) => {
        state.loading = false
        const index = state.instances.findIndex(instance => instance.id === action.payload.id)
        if (index !== -1) {
          state.instances[index] = action.payload
        }
        state.error = null
      })
      .addCase(terminateWorkflow.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
  }
})

export const { 
  clearError, 
  setSelectedDefinition, 
  setSelectedInstance, 
  clearSelectedDefinition, 
  clearSelectedInstance 
} = workflowSlice.actions
export default workflowSlice.reducer
