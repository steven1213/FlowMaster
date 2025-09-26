import { createSlice } from '@reduxjs/toolkit'

interface UserState {
  users: any[]
  currentUser: any | null
  isLoading: boolean
}

const initialState: UserState = {
  users: [],
  currentUser: null,
  isLoading: false
}

const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    setUsers: (state, action) => {
      state.users = action.payload
    },
    setCurrentUser: (state, action) => {
      state.currentUser = action.payload
    },
    setLoading: (state, action) => {
      state.isLoading = action.payload
    }
  }
})

export const { setUsers, setCurrentUser, setLoading } = userSlice.actions
export default userSlice.reducer
