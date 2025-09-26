import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit'
import { AuditLog, AuditLogQuery, PageResult } from '@/types'
import { auditService } from '@/services/auditService'

interface AuditState {
  logs: AuditLog[]
  total: number
  loading: boolean
  error: string | null
  selectedLog: AuditLog | null
}

const initialState: AuditState = {
  logs: [],
  total: 0,
  loading: false,
  error: null,
  selectedLog: null
}

// 异步actions
export const fetchAuditLogs = createAsyncThunk(
  'audit/fetchAuditLogs',
  async (query: AuditLogQuery, { rejectWithValue }) => {
    try {
      const response = await auditService.getAuditLogs(query)
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '获取审计日志失败')
    }
  }
)

export const getAuditLogById = createAsyncThunk(
  'audit/getAuditLogById',
  async (id: string, { rejectWithValue }) => {
    try {
      const response = await auditService.getAuditLogById(id)
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '获取审计日志详情失败')
    }
  }
)

export const exportAuditLogs = createAsyncThunk(
  'audit/exportAuditLogs',
  async (query: AuditLogQuery, { rejectWithValue }) => {
    try {
      const response = await auditService.exportAuditLogs(query)
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '导出审计日志失败')
    }
  }
)

const auditSlice = createSlice({
  name: 'audit',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null
    },
    setSelectedLog: (state, action: PayloadAction<AuditLog | null>) => {
      state.selectedLog = action.payload
    },
    clearSelectedLog: (state) => {
      state.selectedLog = null
    }
  },
  extraReducers: (builder) => {
    builder
      // 获取审计日志
      .addCase(fetchAuditLogs.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(fetchAuditLogs.fulfilled, (state, action) => {
        state.loading = false
        state.logs = action.payload.content
        state.total = action.payload.totalElements
        state.error = null
      })
      .addCase(fetchAuditLogs.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
      // 获取审计日志详情
      .addCase(getAuditLogById.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(getAuditLogById.fulfilled, (state, action) => {
        state.loading = false
        state.selectedLog = action.payload
        state.error = null
      })
      .addCase(getAuditLogById.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
      // 导出审计日志
      .addCase(exportAuditLogs.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(exportAuditLogs.fulfilled, (state) => {
        state.loading = false
        state.error = null
      })
      .addCase(exportAuditLogs.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
  }
})

export const { clearError, setSelectedLog, clearSelectedLog } = auditSlice.actions
export default auditSlice.reducer
