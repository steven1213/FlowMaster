import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit'
import { Permission, PermissionForm, PermissionQuery, PageResult } from '@/types'
import { permissionService } from '@/services/permissionService'

interface PermissionState {
  permissions: Permission[]
  total: number
  loading: boolean
  error: string | null
  selectedPermission: Permission | null
  permissionTree: Permission[]
}

const initialState: PermissionState = {
  permissions: [],
  total: 0,
  loading: false,
  error: null,
  selectedPermission: null,
  permissionTree: []
}

// 异步actions
export const fetchPermissions = createAsyncThunk(
  'permission/fetchPermissions',
  async (query: PermissionQuery, { rejectWithValue }) => {
    try {
      const response = await permissionService.getPermissions(query)
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '获取权限列表失败')
    }
  }
)

export const fetchPermissionTree = createAsyncThunk(
  'permission/fetchPermissionTree',
  async (_, { rejectWithValue }) => {
    try {
      const response = await permissionService.getPermissionTree()
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '获取权限树失败')
    }
  }
)

export const createPermission = createAsyncThunk(
  'permission/createPermission',
  async (permissionForm: PermissionForm, { rejectWithValue }) => {
    try {
      const response = await permissionService.createPermission(permissionForm)
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '创建权限失败')
    }
  }
)

export const updatePermission = createAsyncThunk(
  'permission/updatePermission',
  async ({ id, permissionForm }: { id: string; permissionForm: PermissionForm }, { rejectWithValue }) => {
    try {
      const response = await permissionService.updatePermission(id, permissionForm)
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '更新权限失败')
    }
  }
)

export const deletePermission = createAsyncThunk(
  'permission/deletePermission',
  async (id: string, { rejectWithValue }) => {
    try {
      await permissionService.deletePermission(id)
      return id
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '删除权限失败')
    }
  }
)

export const getPermissionById = createAsyncThunk(
  'permission/getPermissionById',
  async (id: string, { rejectWithValue }) => {
    try {
      const response = await permissionService.getPermissionById(id)
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '获取权限详情失败')
    }
  }
)

const permissionSlice = createSlice({
  name: 'permission',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null
    },
    setSelectedPermission: (state, action: PayloadAction<Permission | null>) => {
      state.selectedPermission = action.payload
    },
    clearSelectedPermission: (state) => {
      state.selectedPermission = null
    }
  },
  extraReducers: (builder) => {
    builder
      // 获取权限列表
      .addCase(fetchPermissions.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(fetchPermissions.fulfilled, (state, action) => {
        state.loading = false
        state.permissions = action.payload.content
        state.total = action.payload.totalElements
        state.error = null
      })
      .addCase(fetchPermissions.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
      // 获取权限树
      .addCase(fetchPermissionTree.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(fetchPermissionTree.fulfilled, (state, action) => {
        state.loading = false
        state.permissionTree = action.payload
        state.error = null
      })
      .addCase(fetchPermissionTree.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
      // 创建权限
      .addCase(createPermission.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(createPermission.fulfilled, (state, action) => {
        state.loading = false
        state.permissions.unshift(action.payload)
        state.total += 1
        state.error = null
      })
      .addCase(createPermission.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
      // 更新权限
      .addCase(updatePermission.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(updatePermission.fulfilled, (state, action) => {
        state.loading = false
        const index = state.permissions.findIndex(permission => permission.id === action.payload.id)
        if (index !== -1) {
          state.permissions[index] = action.payload
        }
        if (state.selectedPermission?.id === action.payload.id) {
          state.selectedPermission = action.payload
        }
        state.error = null
      })
      .addCase(updatePermission.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
      // 删除权限
      .addCase(deletePermission.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(deletePermission.fulfilled, (state, action) => {
        state.loading = false
        state.permissions = state.permissions.filter(permission => permission.id !== action.payload)
        state.total -= 1
        if (state.selectedPermission?.id === action.payload) {
          state.selectedPermission = null
        }
        state.error = null
      })
      .addCase(deletePermission.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
      // 获取权限详情
      .addCase(getPermissionById.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(getPermissionById.fulfilled, (state, action) => {
        state.loading = false
        state.selectedPermission = action.payload
        state.error = null
      })
      .addCase(getPermissionById.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
  }
})

export const { clearError, setSelectedPermission, clearSelectedPermission } = permissionSlice.actions
export default permissionSlice.reducer
