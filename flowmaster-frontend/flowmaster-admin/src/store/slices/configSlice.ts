import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit'
import { SystemConfig, SystemConfigForm, SystemMetrics } from '@/types'
import { configService } from '@/services/configService'

interface ConfigState {
  configs: SystemConfig[]
  loading: boolean
  error: string | null
  selectedConfig: SystemConfig | null
  metrics: SystemMetrics | null
}

const initialState: ConfigState = {
  configs: [],
  loading: false,
  error: null,
  selectedConfig: null,
  metrics: null
}

// 异步actions
export const fetchSystemConfigs = createAsyncThunk(
  'config/fetchSystemConfigs',
  async (_, { rejectWithValue }) => {
    try {
      const response = await configService.getSystemConfigs()
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '获取系统配置失败')
    }
  }
)

export const updateSystemConfig = createAsyncThunk(
  'config/updateSystemConfig',
  async ({ id, configForm }: { id: string; configForm: SystemConfigForm }, { rejectWithValue }) => {
    try {
      const response = await configService.updateSystemConfig(id, configForm)
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '更新系统配置失败')
    }
  }
)

export const fetchSystemMetrics = createAsyncThunk(
  'config/fetchSystemMetrics',
  async (_, { rejectWithValue }) => {
    try {
      const response = await configService.getSystemMetrics()
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '获取系统指标失败')
    }
  }
)

export const resetSystemConfig = createAsyncThunk(
  'config/resetSystemConfig',
  async (key: string, { rejectWithValue }) => {
    try {
      const response = await configService.resetSystemConfig(key)
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '重置系统配置失败')
    }
  }
)

const configSlice = createSlice({
  name: 'config',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null
    },
    setSelectedConfig: (state, action: PayloadAction<SystemConfig | null>) => {
      state.selectedConfig = action.payload
    },
    clearSelectedConfig: (state) => {
      state.selectedConfig = null
    }
  },
  extraReducers: (builder) => {
    builder
      // 获取系统配置
      .addCase(fetchSystemConfigs.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(fetchSystemConfigs.fulfilled, (state, action) => {
        state.loading = false
        state.configs = action.payload
        state.error = null
      })
      .addCase(fetchSystemConfigs.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
      // 更新系统配置
      .addCase(updateSystemConfig.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(updateSystemConfig.fulfilled, (state, action) => {
        state.loading = false
        const index = state.configs.findIndex(config => config.id === action.payload.id)
        if (index !== -1) {
          state.configs[index] = action.payload
        }
        if (state.selectedConfig?.id === action.payload.id) {
          state.selectedConfig = action.payload
        }
        state.error = null
      })
      .addCase(updateSystemConfig.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
      // 获取系统指标
      .addCase(fetchSystemMetrics.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(fetchSystemMetrics.fulfilled, (state, action) => {
        state.loading = false
        state.metrics = action.payload
        state.error = null
      })
      .addCase(fetchSystemMetrics.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
      // 重置系统配置
      .addCase(resetSystemConfig.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(resetSystemConfig.fulfilled, (state, action) => {
        state.loading = false
        const index = state.configs.findIndex(config => config.id === action.payload.id)
        if (index !== -1) {
          state.configs[index] = action.payload
        }
        if (state.selectedConfig?.id === action.payload.id) {
          state.selectedConfig = action.payload
        }
        state.error = null
      })
      .addCase(resetSystemConfig.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
  }
})

export const { clearError, setSelectedConfig, clearSelectedConfig } = configSlice.actions
export default configSlice.reducer
