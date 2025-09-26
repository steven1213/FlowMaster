import { createSlice } from '@reduxjs/toolkit'

interface MonitorState {
  systemMetrics: any | null
  applicationMetrics: any | null
  isLoading: boolean
}

const initialState: MonitorState = {
  systemMetrics: null,
  applicationMetrics: null,
  isLoading: false
}

const monitorSlice = createSlice({
  name: 'monitor',
  initialState,
  reducers: {
    setSystemMetrics: (state, action) => {
      state.systemMetrics = action.payload
    },
    setApplicationMetrics: (state, action) => {
      state.applicationMetrics = action.payload
    },
    setLoading: (state, action) => {
      state.isLoading = action.payload
    }
  }
})

export const { setSystemMetrics, setApplicationMetrics, setLoading } = monitorSlice.actions
export default monitorSlice.reducer
