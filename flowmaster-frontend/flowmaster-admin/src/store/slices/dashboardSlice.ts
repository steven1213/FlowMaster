import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit'
import { DashboardStats, ChartData, TimeSeriesData } from '@/types'
import { dashboardService } from '@/services/dashboardService'

interface DashboardState {
  stats: DashboardStats | null
  userChartData: ChartData[]
  workflowChartData: ChartData[]
  systemMetrics: TimeSeriesData[]
  loading: boolean
  error: string | null
}

const initialState: DashboardState = {
  stats: null,
  userChartData: [],
  workflowChartData: [],
  systemMetrics: [],
  loading: false,
  error: null
}

// 异步actions
export const fetchDashboardStats = createAsyncThunk(
  'dashboard/fetchDashboardStats',
  async (_, { rejectWithValue }) => {
    try {
      const response = await dashboardService.getDashboardStats()
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '获取仪表板统计失败')
    }
  }
)

export const fetchUserChartData = createAsyncThunk(
  'dashboard/fetchUserChartData',
  async (_, { rejectWithValue }) => {
    try {
      const response = await dashboardService.getUserChartData()
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '获取用户图表数据失败')
    }
  }
)

export const fetchWorkflowChartData = createAsyncThunk(
  'dashboard/fetchWorkflowChartData',
  async (_, { rejectWithValue }) => {
    try {
      const response = await dashboardService.getWorkflowChartData()
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '获取工作流图表数据失败')
    }
  }
)

export const fetchSystemMetrics = createAsyncThunk(
  'dashboard/fetchSystemMetrics',
  async (_, { rejectWithValue }) => {
    try {
      const response = await dashboardService.getSystemMetrics()
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '获取系统指标失败')
    }
  }
)

const dashboardSlice = createSlice({
  name: 'dashboard',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null
    }
  },
  extraReducers: (builder) => {
    builder
      // 获取仪表板统计
      .addCase(fetchDashboardStats.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(fetchDashboardStats.fulfilled, (state, action) => {
        state.loading = false
        state.stats = action.payload
        state.error = null
      })
      .addCase(fetchDashboardStats.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
      // 获取用户图表数据
      .addCase(fetchUserChartData.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(fetchUserChartData.fulfilled, (state, action) => {
        state.loading = false
        state.userChartData = action.payload
        state.error = null
      })
      .addCase(fetchUserChartData.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
      // 获取工作流图表数据
      .addCase(fetchWorkflowChartData.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(fetchWorkflowChartData.fulfilled, (state, action) => {
        state.loading = false
        state.workflowChartData = action.payload
        state.error = null
      })
      .addCase(fetchWorkflowChartData.rejected, (state, action) => {
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
        state.systemMetrics = action.payload
        state.error = null
      })
      .addCase(fetchSystemMetrics.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
  }
})

export const { clearError } = dashboardSlice.actions
export default dashboardSlice.reducer
