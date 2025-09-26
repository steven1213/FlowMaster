import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit'
import { Role, RoleForm, RoleQuery, PageResult } from '@/types'
import { roleService } from '@/services/roleService'

interface RoleState {
  roles: Role[]
  total: number
  loading: boolean
  error: string | null
  selectedRole: Role | null
}

const initialState: RoleState = {
  roles: [],
  total: 0,
  loading: false,
  error: null,
  selectedRole: null
}

// 异步actions
export const fetchRoles = createAsyncThunk(
  'role/fetchRoles',
  async (query: RoleQuery, { rejectWithValue }) => {
    try {
      const response = await roleService.getRoles(query)
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '获取角色列表失败')
    }
  }
)

export const createRole = createAsyncThunk(
  'role/createRole',
  async (roleForm: RoleForm, { rejectWithValue }) => {
    try {
      const response = await roleService.createRole(roleForm)
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '创建角色失败')
    }
  }
)

export const updateRole = createAsyncThunk(
  'role/updateRole',
  async ({ id, roleForm }: { id: string; roleForm: RoleForm }, { rejectWithValue }) => {
    try {
      const response = await roleService.updateRole(id, roleForm)
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '更新角色失败')
    }
  }
)

export const deleteRole = createAsyncThunk(
  'role/deleteRole',
  async (id: string, { rejectWithValue }) => {
    try {
      await roleService.deleteRole(id)
      return id
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '删除角色失败')
    }
  }
)

export const getRoleById = createAsyncThunk(
  'role/getRoleById',
  async (id: string, { rejectWithValue }) => {
    try {
      const response = await roleService.getRoleById(id)
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '获取角色详情失败')
    }
  }
)

const roleSlice = createSlice({
  name: 'role',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null
    },
    setSelectedRole: (state, action: PayloadAction<Role | null>) => {
      state.selectedRole = action.payload
    },
    clearSelectedRole: (state) => {
      state.selectedRole = null
    }
  },
  extraReducers: (builder) => {
    builder
      // 获取角色列表
      .addCase(fetchRoles.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(fetchRoles.fulfilled, (state, action) => {
        state.loading = false
        state.roles = action.payload.content
        state.total = action.payload.totalElements
        state.error = null
      })
      .addCase(fetchRoles.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
      // 创建角色
      .addCase(createRole.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(createRole.fulfilled, (state, action) => {
        state.loading = false
        state.roles.unshift(action.payload)
        state.total += 1
        state.error = null
      })
      .addCase(createRole.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
      // 更新角色
      .addCase(updateRole.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(updateRole.fulfilled, (state, action) => {
        state.loading = false
        const index = state.roles.findIndex(role => role.id === action.payload.id)
        if (index !== -1) {
          state.roles[index] = action.payload
        }
        if (state.selectedRole?.id === action.payload.id) {
          state.selectedRole = action.payload
        }
        state.error = null
      })
      .addCase(updateRole.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
      // 删除角色
      .addCase(deleteRole.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(deleteRole.fulfilled, (state, action) => {
        state.loading = false
        state.roles = state.roles.filter(role => role.id !== action.payload)
        state.total -= 1
        if (state.selectedRole?.id === action.payload) {
          state.selectedRole = null
        }
        state.error = null
      })
      .addCase(deleteRole.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
      // 获取角色详情
      .addCase(getRoleById.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(getRoleById.fulfilled, (state, action) => {
        state.loading = false
        state.selectedRole = action.payload
        state.error = null
      })
      .addCase(getRoleById.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
  }
})

export const { clearError, setSelectedRole, clearSelectedRole } = roleSlice.actions
export default roleSlice.reducer
