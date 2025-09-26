import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit'
import { User, UserForm, UserQuery, PageResult, ApiResponse } from '@/types'
import { userService } from '@/services/userService'

interface UserState {
  users: User[]
  total: number
  loading: boolean
  error: string | null
  selectedUser: User | null
}

const initialState: UserState = {
  users: [],
  total: 0,
  loading: false,
  error: null,
  selectedUser: null
}

// 异步actions
export const fetchUsers = createAsyncThunk(
  'user/fetchUsers',
  async (query: UserQuery, { rejectWithValue }) => {
    try {
      const response = await userService.getUsers(query)
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '获取用户列表失败')
    }
  }
)

export const createUser = createAsyncThunk(
  'user/createUser',
  async (userForm: UserForm, { rejectWithValue }) => {
    try {
      const response = await userService.createUser(userForm)
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '创建用户失败')
    }
  }
)

export const updateUser = createAsyncThunk(
  'user/updateUser',
  async ({ id, userForm }: { id: string; userForm: UserForm }, { rejectWithValue }) => {
    try {
      const response = await userService.updateUser(id, userForm)
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '更新用户失败')
    }
  }
)

export const deleteUser = createAsyncThunk(
  'user/deleteUser',
  async (id: string, { rejectWithValue }) => {
    try {
      await userService.deleteUser(id)
      return id
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '删除用户失败')
    }
  }
)

export const getUserById = createAsyncThunk(
  'user/getUserById',
  async (id: string, { rejectWithValue }) => {
    try {
      const response = await userService.getUserById(id)
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '获取用户详情失败')
    }
  }
)

export const resetPassword = createAsyncThunk(
  'user/resetPassword',
  async ({ id, newPassword }: { id: string; newPassword: string }, { rejectWithValue }) => {
    try {
      await userService.resetPassword(id, newPassword)
      return id
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '重置密码失败')
    }
  }
)

export const changeUserStatus = createAsyncThunk(
  'user/changeUserStatus',
  async ({ id, status }: { id: string; status: string }, { rejectWithValue }) => {
    try {
      const response = await userService.changeUserStatus(id, status)
      return response.data
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || '修改用户状态失败')
    }
  }
)

const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null
    },
    setSelectedUser: (state, action: PayloadAction<User | null>) => {
      state.selectedUser = action.payload
    },
    clearSelectedUser: (state) => {
      state.selectedUser = null
    }
  },
  extraReducers: (builder) => {
    builder
      // 获取用户列表
      .addCase(fetchUsers.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(fetchUsers.fulfilled, (state, action) => {
        state.loading = false
        state.users = action.payload.content
        state.total = action.payload.totalElements
        state.error = null
      })
      .addCase(fetchUsers.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
      // 创建用户
      .addCase(createUser.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(createUser.fulfilled, (state, action) => {
        state.loading = false
        state.users.unshift(action.payload)
        state.total += 1
        state.error = null
      })
      .addCase(createUser.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
      // 更新用户
      .addCase(updateUser.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(updateUser.fulfilled, (state, action) => {
        state.loading = false
        const index = state.users.findIndex(user => user.id === action.payload.id)
        if (index !== -1) {
          state.users[index] = action.payload
        }
        if (state.selectedUser?.id === action.payload.id) {
          state.selectedUser = action.payload
        }
        state.error = null
      })
      .addCase(updateUser.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
      // 删除用户
      .addCase(deleteUser.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(deleteUser.fulfilled, (state, action) => {
        state.loading = false
        state.users = state.users.filter(user => user.id !== action.payload)
        state.total -= 1
        if (state.selectedUser?.id === action.payload) {
          state.selectedUser = null
        }
        state.error = null
      })
      .addCase(deleteUser.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
      // 获取用户详情
      .addCase(getUserById.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(getUserById.fulfilled, (state, action) => {
        state.loading = false
        state.selectedUser = action.payload
        state.error = null
      })
      .addCase(getUserById.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
      // 重置密码
      .addCase(resetPassword.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(resetPassword.fulfilled, (state) => {
        state.loading = false
        state.error = null
      })
      .addCase(resetPassword.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
      // 修改用户状态
      .addCase(changeUserStatus.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(changeUserStatus.fulfilled, (state, action) => {
        state.loading = false
        const index = state.users.findIndex(user => user.id === action.payload.id)
        if (index !== -1) {
          state.users[index] = action.payload
        }
        if (state.selectedUser?.id === action.payload.id) {
          state.selectedUser = action.payload
        }
        state.error = null
      })
      .addCase(changeUserStatus.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload as string
      })
  }
})

export const { clearError, setSelectedUser, clearSelectedUser } = userSlice.actions
export default userSlice.reducer
